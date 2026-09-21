
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestClient {

    public static void main(String[] args) throws Exception {

        // This is the JSON data we want to send to the server.
        // It represents a new user registration.
        String json = """
                {
                    "first_name": "Paul",
                    "last_name": "Pogba",
                    "email": "paul@gmail.com",
                    "username": "paul123",
                    "password": "123",
                    "phone": "0501122334"
                }
                """;

        // Creates an HTTP client.
        // The client is responsible for sending HTTP requests to our server.
        HttpClient client = HttpClient.newHttpClient();

        // Creates the HTTP request that will be sent to the server.
        HttpRequest request = HttpRequest.newBuilder()

                // The URL of the API endpoint we want to communicate with.
                // Our FitnessServer is running on localhost:8080.
                // /users/register is the registration endpoint.
                .uri(URI.create("http://localhost:8080/users/register"))

                // Tells the server that the data we are sending is JSON.
                .header("Content-Type", "application/json")

                // Specifies that this is a POST request.
                // POST is used because we are sending data to create a new user.
                .POST(HttpRequest.BodyPublishers.ofString(json))

                // Finishes building the HTTP request.
                .build();

        // Sends the HTTP request to the server.
        // The server's response is stored in the 'response' variable.
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Prints a label so we know the following text is the server's response.
        System.out.println("Server response:");

        // Prints the actual response sent back by FitnessServer.
        System.out.println(response.body());
    }
}

