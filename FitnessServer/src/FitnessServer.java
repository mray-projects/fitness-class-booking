import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class FitnessServer {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        server.createContext("/test", FitnessServer::handleTest);
        server.createContext("/users/register", UserHandler::handleRegister);
        server.createContext("/users/login", UserHandler::handleLogin);

        server.start();

        System.out.println("Server started on port 8080");
    }

    private static void handleTest(HttpExchange exchange) throws IOException {

        String response = "Fitness Booking Server is working!";

        exchange.sendResponseHeaders(200, response.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}