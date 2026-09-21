package api;
import java.net.URI;
import java.net.http.HttpClient; //sends
import java.net.http.HttpRequest; // builds request
import java.net.http.HttpResponse; //receives

public class UserAPI {

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

                // The registration API endpoint on our server.
                .uri(URI.create("http://localhost:8080/users/register"))

                // Tell the server that we are sending JSON.
                .header("Content-Type", "application/json")

                // Use POST because we are creating a new user.
                .POST(HttpRequest.BodyPublishers.ofString(json))

                // Finish building the request.
                .build();

        // Send the request and receive the server's response.
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the message received from the server.
        return response.body();
    }
}

