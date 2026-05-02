import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MockApiServer {
    private static final List<User> USERS = new ArrayList<>();

    static {
        USERS.add(new User(1, "student", "student", "student@example.com", "+70000000000", "Иванов", "Иван", "Иванович", "1"));
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        server.createContext("/api/groups", MockApiServer::groups);
        server.createContext("/api/users", MockApiServer::users);
        server.createContext("/api/auth/login", MockApiServer::login);
        server.createContext("/api/auth/register", MockApiServer::register);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Mock API started at http://0.0.0.0:8080/api/");
    }

    private static void groups(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }
        send(exchange, 200, "[{\"groupId\":1,\"groupName\":\"2307д2\"},{\"groupId\":2,\"groupName\":\"2307д1\"},{\"groupId\":3,\"groupName\":\"2308д\"}]");
    }

    private static void users(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < USERS.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(USERS.get(i).toJson());
        }
        json.append(']');
        send(exchange, 200, json.toString());
    }

    private static void login(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        String body = readBody(exchange);
        String login = valueOf(body, "login", "student");
        String password = valueOf(body, "password", "");
        User user = USERS.stream()
            .filter(candidate -> candidate.login.equals(login) && candidate.password.equals(password))
            .findFirst()
            .orElse(null);

        if (user == null) {
            send(exchange, 401, "{\"message\":\"Invalid login or password\"}");
            return;
        }

        send(exchange, 200, "{\"token\":\"mock-jwt-token\",\"user\":" + user.toJson() + "}");
    }

    private static void register(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"message\":\"Method not allowed\"}");
            return;
        }

        String body = readBody(exchange);
        User user = new User(
            USERS.size() + 1,
            valueOf(body, "login", "user" + (USERS.size() + 1)),
            valueOf(body, "password", ""),
            valueOf(body, "email", "user@example.com"),
            valueOf(body, "phoneNumber", "+70000000000"),
            valueOf(body, "lastName", "Фамилия"),
            valueOf(body, "firstName", "Имя"),
            valueOf(body, "middleName", ""),
            valueOf(body, "groupId", "1")
        );
        USERS.add(user);
        send(exchange, 200, "");
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static String valueOf(String json, String key, String fallback) {
        Pattern stringPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher stringMatcher = stringPattern.matcher(json);
        if (stringMatcher.find()) {
            return stringMatcher.group(1);
        }

        Pattern numberPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(\\d+)");
        Matcher numberMatcher = numberPattern.matcher(json);
        if (numberMatcher.find()) {
            return numberMatcher.group(1);
        }

        return fallback;
    }

    private static void send(HttpExchange exchange, int code, String body) throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    private record User(
        int id,
        String login,
        String password,
        String email,
        String phoneNumber,
        String lastName,
        String firstName,
        String middleName,
        String groupId
    ) {
        String toJson() {
            return "{"
                + "\"userId\":" + id + ","
                + "\"login\":\"" + escape(login) + "\","
                + "\"email\":\"" + escape(email) + "\","
                + "\"phoneNumber\":\"" + escape(phoneNumber) + "\","
                + "\"roleId\":1,"
                + "\"authAllowed\":true,"
                + "\"person\":{"
                + "\"personId\":" + id + ","
                + "\"firstName\":\"" + escape(firstName) + "\","
                + "\"lastName\":\"" + escape(lastName) + "\","
                + "\"middleName\":\"" + escape(middleName) + "\","
                + "\"birthDate\":\"" + LocalDate.now() + "\","
                + "\"gender\":\"m\","
                + "\"groupId\":" + groupId + ","
                + "\"groupName\":\"2307д2\""
                + "}"
                + "}";
        }

        private static String escape(String value) {
            return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}
