import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BookingHandler {

    // Handles booking a fitness class
    public static void handleCreateBooking(HttpExchange exchange) throws IOException {

        // Allow the web frontend to communicate with the server
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin", "*"
        );

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Methods", "POST, OPTIONS"
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

            // Get the JSON sent by the frontend
            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Convert JSON into a BookingRequest object
            Gson gson = new Gson();

            BookingRequest bookingRequest =
                    gson.fromJson(requestBody, BookingRequest.class);

            // Create the booking
            BookingDAO.createBooking(
                    bookingRequest.user_id,
                    bookingRequest.class_id
            );

            String response = "Class booked successfully!";

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

            String response = e.getMessage();

            exchange.sendResponseHeaders(
                    400,
                    response.getBytes(StandardCharsets.UTF_8).length
            );

            exchange.getResponseBody().write(
                    response.getBytes(StandardCharsets.UTF_8)
            );

            exchange.getResponseBody().close();
        }
    }


    // Represents the booking data received from the frontend
    static class BookingRequest {

        int user_id;
        int class_id;
    }
}