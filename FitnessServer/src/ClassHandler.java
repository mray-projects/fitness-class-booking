import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClassHandler {

    // Handles requests to view all fitness classes
    public static void handleGetClasses(HttpExchange exchange) throws IOException {

        // Allow the web frontend to communicate with the server
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin", "*"
        );

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Methods", "GET, OPTIONS"
        );

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Headers", "Content-Type"
        );

        // Handle browser CORS preflight request
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {

            // Get all classes from the database
            List<FitnessClass> classes =
                    FitnessClassDAO.getAllClasses();

            // Convert the Java objects into JSON
            Gson gson = new Gson();

            String response = gson.toJson(classes);

            // Tell the browser that the response is JSON
            exchange.getResponseHeaders().add(
                    "Content-Type", "application/json"
            );

            // Send the response
            exchange.sendResponseHeaders(
                    200,
                    response.getBytes(StandardCharsets.UTF_8).length
            );

            exchange.getResponseBody().write(
                    response.getBytes(StandardCharsets.UTF_8)
            );

            exchange.getResponseBody().close();

        } catch (Exception e) {

            e.printStackTrace();

            String response = "Failed to retrieve classes.";

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