import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserHandler {

    // Handles user registration
    public static void handleRegister(HttpExchange exchange) throws IOException {

        // Allow the web frontend to communicate with the server
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Get the data sent by the client
        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );

        // Convert the JSON data into a User object
        Gson gson = new Gson();

        User user = gson.fromJson(requestBody, User.class);

        System.out.println("Received user:");
        System.out.println("Name: " + user.getFirst_name() + " " + user.getLast_name());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Phone: " + user.getPhone());

        // Save the user to MySQL
        try {

            UserDAO.registerUser(
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getPhone()
            );

            String response = "User registered successfully!";

            exchange.sendResponseHeaders(200, response.length());

            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (SQLIntegrityConstraintViolationException e) {

            // This happens when MySQL rejects the registration
            // because the email or username already exists.
            String response = "Username or email already exists!";

            // 409 means the request conflicts with existing data.
            exchange.sendResponseHeaders(409, response.length());

            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {

            e.printStackTrace();

            String response = "Registration failed!";

            exchange.sendResponseHeaders(500, response.length());

            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }


    // Handles user login
    public static void handleLogin(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );

        Gson gson = new Gson();

        User user = gson.fromJson(requestBody, User.class);

        System.out.println("Login attempt:");
        System.out.println("Username: " + user.getUsername());

        try {

            // Try to log in and get the user's ID
            int userId = UserDAO.loginUser(
                    user.getUsername(),
                    user.getPassword()
            );

            if (userId != -1) {

                // Create a JSON response containing the user ID
                String response = """
                    {
                        "message": "Login successful!",
                        "user_id": %d
                    }
                    """.formatted(userId);

                exchange.getResponseHeaders().add(
                        "Content-Type",
                        "application/json"
                );

                exchange.sendResponseHeaders(
                        200,
                        response.getBytes(StandardCharsets.UTF_8).length
                );

                exchange.getResponseBody().write(
                        response.getBytes(StandardCharsets.UTF_8)
                );

                exchange.getResponseBody().close();

            } else {

                String response = "Incorrect username or password.";

                exchange.sendResponseHeaders(
                        401,
                        response.getBytes(StandardCharsets.UTF_8).length
                );

                exchange.getResponseBody().write(
                        response.getBytes(StandardCharsets.UTF_8)
                );

                exchange.getResponseBody().close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            String response = "Login failed!";

            exchange.sendResponseHeaders(
                    500,
                    response.getBytes(StandardCharsets.UTF_8).length
            );

            exchange.getResponseBody().write(
                    response.getBytes(StandardCharsets.UTF_8)
            );

            exchange.getResponseBody().close();
        }
    }
}