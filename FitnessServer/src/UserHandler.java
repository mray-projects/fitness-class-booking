import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserHandler {

    // Handles user registration
    public static void handleRegister(HttpExchange exchange) throws IOException {

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

        // Get the login data sent by the client
        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );

        // Convert the JSON data into a User object
        Gson gson = new Gson();

        User user = gson.fromJson(requestBody, User.class);

        System.out.println("Login attempt:");
        System.out.println("Username: " + user.getUsername());

        try {

            // Check the username and password in MySQL
            boolean loginSuccessful = UserDAO.loginUser(
                    user.getUsername(),
                    user.getPassword()
            );

            if (loginSuccessful) {

                // Login details are correct
                String response = "Login successful!";

                exchange.sendResponseHeaders(200, response.length());

                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();

            } else {

                // Username or password is incorrect
                String response = "Incorrect username or password.";

                exchange.sendResponseHeaders(401, response.length());

                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            String response = "Login failed!";

            exchange.sendResponseHeaders(500, response.length());

            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}