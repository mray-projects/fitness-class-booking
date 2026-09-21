
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import java.sql.SQLIntegrityConstraintViolationException;

public class UserHandler {

    // This method handles registration requests sent to /users/register.
    public static void handleRegister(HttpExchange exchange) throws IOException {

        // Gets the data (request body) that was sent by the client.
        // In our case, this contains the JSON registration data.
        InputStream inputStream = exchange.getRequestBody();

        // Reads all the data from the request body and converts it into a String.
        // The String will contain the JSON sent by TestClient.
        String requestBody = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );

        // Creates a Gson object.
        // Gson is used to convert between JSON and Java objects.
        Gson gson = new Gson();

        // Converts the JSON String into a Java User object.
        // requestBody = the JSON data
        // User.class = tells Gson what type of Java object to create.
        User user = gson.fromJson(requestBody, User.class);

        // Prints the information received from the client to the server console.
        System.out.println("Received user:");
        System.out.println("Name: " + user.getFirst_name() + " " + user.getLast_name());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Phone: " + user.getPhone());

        // Save user to MySQL
        try {

            // Calls the UserDAO to save the user's information in MySQL.
            // The information is taken from the User object created by Gson.
            UserDAO.registerUser(
                    user.getFirst_name(),
                    user.getLast_name(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getPhone()
            );

            // Creates the message that will be sent back to the client.
            String response = "User registered successfully!";

            // Sends HTTP status code 200 to indicate that the request was successful.
            // response.length() tells the server how many bytes of data will be sent.
            exchange.sendResponseHeaders(200, response.length());

            // Sends the response message back to the client.
            exchange.getResponseBody().write(response.getBytes());

            // Closes the response.
            exchange.getResponseBody().close();

        } catch (Exception e) {

            // Prints the error details in the server console.
            // This helps us identify what went wrong.
            e.printStackTrace();

            // Creates the message that will be sent if registration fails.
            String response = "Registration failed!";

            // Sends HTTP status code 500 to indicate that a server-side error occurred.
            exchange.sendResponseHeaders(500, response.length());

            // Sends the failure message back to the client.
            exchange.getResponseBody().write(response.getBytes());

            // Closes the response.
            exchange.getResponseBody().close();
        }
    }
}

