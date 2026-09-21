import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestClient {

    public static void main(String[] args) throws Exception {

        String json = """
                {
                    "first_name": "John",
                    "last_name": "Smith",
                    "email": "john@email.com",
                    "username": "john123",
                    "password": "123456"
                }
                """;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Server response:");
        System.out.println(response.body());
    }
}