import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UserHandler {

    public static void handleRegister(HttpExchange exchange) throws IOException {

        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );

        System.out.println("Received registration data:");
        System.out.println(requestBody);

        String response = "Registration received!";

        exchange.sendResponseHeaders(200, response.length());

        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}