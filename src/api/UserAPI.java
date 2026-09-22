package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserAPI {

    // Register a new user
    public static String registerUser(
            String firstName,
            String lastName,
            String email,
            String username,
            String password
    ) throws Exception {

        // Create the JSON data that will be sent to the server.
        String json = """
                {
                    "first_name": "%s",
                    "last_name": "%s",
                    "email": "%s",
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(
                firstName,
                lastName,
                email,
                username,
                password
        );

        // Create an HTTP client.
        HttpClient client = HttpClient.newHttpClient();

        // Build the HTTP request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and receive the server's response.
        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        // Return the message received from the server.
        return response.body();
    }


    // Login an existing user
    public static String loginUser(
            String username,
            String password
    ) throws Exception {

        // Create the JSON data that will be sent to the server.
        String json = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(
                username,
                password
        );

        // Create an HTTP client.
        HttpClient client = HttpClient.newHttpClient();

        // Build the HTTP request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and receive the server's response.
        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        // Return the message received from the server.
        return response.body();
    }
}