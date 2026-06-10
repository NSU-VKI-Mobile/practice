const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
app.use(express.json());

const PORT = 8080;
const DATA_FILE = path.join(__dirname, 'data.json');

function loadData() {
  try {
    return JSON.parse(fs.readFileSync(DATA_FILE, 'utf-8'));
  } catch {
    return {
      users: [
        {
          id: 1, login: 'admin', password: 'admin',
          firstName: 'Админ', lastName: 'Админов', email: 'admin@test.ru'
        },
        {
          id: 2, login: '123', password: '1',
          firstName: 'Тест', lastName: 'Тестов', email: 'test@test.ru'
        }
      ],
      groups: [
        { groupId: 1, groupName: 'ИС-201' },
        { groupId: 2, groupName: 'ИС-202' },
        { groupId: 3, groupName: 'ПО-101' }
      ],
      nextId: 3
    };
  }
}

function saveData(data) {
  fs.writeFileSync(DATA_FILE, JSON.stringify(data, null, 2), 'utf-8');
}

let data = loadData();

app.post('/api/auth/login', (req, res) => {
  const { login, password } = req.body;
  const user = data.users.find(u => u.login === login && u.password === password);
  if (user) {
    const { password, ...safeUser } = user;
    res.json({ ...safeUser, token: 'test-jwt-token-123' });
  } else {
    res.status(401).json({ error: 'Неверный логин или пароль' });
  }
});

app.post('/api/auth/register', (req, res) => {
  const { login, password, email } = req.body;
  const person = req.body.person || {};
  if (data.users.find(u => u.login === login)) {
    return res.status(409).json({ error: 'Логин уже занят' });
  }
  const newUser = {
    id: data.nextId++,
    login,
    password,
    email: email || '',
    firstName: person.firstName || '',
    lastName: person.lastName || '',
  };
  data.users.push(newUser);
  saveData(data);
  res.status(201).json({ message: 'Пользователь зарегистрирован' });
});

app.get('/api/groups', (req, res) => {
  res.json(data.groups);
});

app.get('/api/users', (req, res) => {
  const safeUsers = data.users.map(({ password, ...u }) => u);
  res.json(safeUsers);
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Сервер запущен на http://localhost:${PORT}`);
  console.log(`Для эмулятора Android: http://10.0.2.2:${PORT}`);
  console.log(`Тестовый вход: admin/admin или 123/1`);
});
