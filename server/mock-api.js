const http = require("http");
const crypto = require("crypto");

const HOST = "0.0.0.0";
const PORT = 8080;
const API_PREFIX = "/api";
const JWT_SECRET = "practice2-lab-secret";

const groups = [
  { groupId: 1, groupName: "ПИ-101" },
  { groupId: 2, groupName: "ПИ-102" },
  { groupId: 3, groupName: "ПИ-103" }
];

const users = [
  {
    id: 1,
    login: "student",
    email: "student@example.com",
    phoneNumber: "+79990000001",
    roleId: 1,
    authAllowed: true,
    person: {
      firstName: "Иван",
      lastName: "Иванов",
      middleName: "Иванович",
      birthDate: "2003-05-12",
      gender: "Male",
      groupId: 1
    },
    password: "1234"
  }
];

function sendJson(res, statusCode, payload) {
  res.writeHead(statusCode, {
    "Content-Type": "application/json; charset=utf-8"
  });
  res.end(JSON.stringify(payload));
}

function sendEmpty(res, statusCode) {
  res.writeHead(statusCode);
  res.end();
}

function readJsonBody(req) {
  return new Promise((resolve, reject) => {
    let raw = "";
    req.on("data", (chunk) => {
      raw += chunk;
    });
    req.on("end", () => {
      if (!raw) {
        resolve({});
        return;
      }
      try {
        resolve(JSON.parse(raw));
      } catch (error) {
        reject(error);
      }
    });
    req.on("error", reject);
  });
}

function base64UrlEncode(value) {
  return Buffer.from(value)
    .toString("base64")
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
}

function signToken(payload) {
  const header = base64UrlEncode(JSON.stringify({ alg: "HS256", typ: "JWT" }));
  const body = base64UrlEncode(JSON.stringify(payload));
  const signature = crypto
    .createHmac("sha256", JWT_SECRET)
    .update(`${header}.${body}`)
    .digest("base64")
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
  return `${header}.${body}.${signature}`;
}

function verifyToken(token) {
  const parts = token.split(".");
  if (parts.length !== 3) {
    return null;
  }

  const [header, body, signature] = parts;
  const expected = crypto
    .createHmac("sha256", JWT_SECRET)
    .update(`${header}.${body}`)
    .digest("base64")
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");

  if (signature !== expected) {
    return null;
  }

  try {
    const json = JSON.parse(Buffer.from(body, "base64url").toString("utf8"));
    if (json.exp && Date.now() >= json.exp * 1000) {
      return null;
    }
    return json;
  } catch {
    return null;
  }
}

function authorize(req) {
  const authHeader = req.headers.authorization || "";
  if (!authHeader.startsWith("Bearer ")) {
    return null;
  }
  const token = authHeader.substring("Bearer ".length);
  return verifyToken(token);
}

function toUserDto(user) {
  return {
    id: user.id,
    login: user.login,
    email: user.email,
    person: user.person
  };
}

function validateRegistration(body) {
  const requiredFields = ["login", "password", "email", "phoneNumber", "person"];
  for (const field of requiredFields) {
    if (!body[field]) {
      return `Поле ${field} обязательно`;
    }
  }

  const person = body.person || {};
  const personFields = ["firstName", "lastName", "birthDate", "gender", "groupId"];
  for (const field of personFields) {
    if (!person[field]) {
      return `Поле person.${field} обязательно`;
    }
  }

  if (!groups.some((group) => group.groupId === Number(person.groupId))) {
    return "Указана несуществующая группа";
  }

  if (users.some((user) => user.login === body.login)) {
    return "Пользователь с таким логином уже существует";
  }

  if (users.some((user) => user.email === body.email)) {
    return "Пользователь с таким email уже существует";
  }

  return null;
}

async function handleRequest(req, res) {
  const { method, url } = req;

  if (method === "GET" && url === `${API_PREFIX}/groups`) {
    sendJson(res, 200, groups);
    return;
  }

  if (method === "POST" && url === `${API_PREFIX}/auth/register`) {
    try {
      const body = await readJsonBody(req);
      const validationError = validateRegistration(body);

      if (validationError) {
        sendJson(res, 400, { message: validationError });
        return;
      }

      const newUser = {
        id: users.length + 1,
        login: body.login,
        email: body.email,
        phoneNumber: body.phoneNumber,
        roleId: body.roleId ?? 1,
        authAllowed: body.authAllowed ?? true,
        person: {
          firstName: body.person.firstName,
          lastName: body.person.lastName,
          middleName: body.person.middleName || "",
          birthDate: body.person.birthDate,
          gender: body.person.gender,
          groupId: Number(body.person.groupId)
        },
        password: body.password
      };

      users.push(newUser);
      sendEmpty(res, 201);
    } catch {
      sendJson(res, 400, { message: "Некорректный JSON" });
    }
    return;
  }

  if (method === "POST" && url === `${API_PREFIX}/auth/login`) {
    try {
      const body = await readJsonBody(req);
      const user = users.find(
        (item) => item.login === body.login && item.password === body.password
      );

      if (!user) {
        sendJson(res, 401, { message: "Неверный логин или пароль" });
        return;
      }

      const token = signToken({
        sub: String(user.id),
        login: user.login,
        exp: Math.floor(Date.now() / 1000) + 60 * 60 * 24
      });

      sendJson(res, 200, {
        token,
        user: toUserDto(user)
      });
    } catch {
      sendJson(res, 400, { message: "Некорректный JSON" });
    }
    return;
  }

  if (method === "GET" && url === `${API_PREFIX}/users`) {
    const session = authorize(req);
    if (!session) {
      sendJson(res, 401, { message: "Требуется авторизация" });
      return;
    }

    sendJson(res, 200, users.map(toUserDto));
    return;
  }

  sendJson(res, 404, { message: "Маршрут не найден" });
}

const server = http.createServer((req, res) => {
  handleRequest(req, res).catch(() => {
    sendJson(res, 500, { message: "Внутренняя ошибка сервера" });
  });
});

server.listen(PORT, HOST, () => {
  console.log(`Mock API started at http://${HOST}:${PORT}${API_PREFIX}/`);
  console.log("Test user: student / 1234");
});
