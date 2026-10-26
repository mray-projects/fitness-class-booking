
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BookingHandler {

    // Handles booking creation, cancellation, and retrieval
    public static void handleCreateBooking(HttpExchange exchange)
            throws IOException {

        // Allow the web frontend to communicate with the Java server
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin", "*"
        );

        // Specify which HTTP methods the frontend can use
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, DELETE, OPTIONS"
        );

        // Allow the frontend to send JSON data
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers", "Content-Type"
        );

        // Handle browser preflight requests
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        try {

            // Identify which operation the frontend is requesting
            String method = exchange.getRequestMethod();

            // GET: Retrieve bookings for a specific user
            if (method.equalsIgnoreCase("GET")) {

                // Read the user ID from the URL query string
                String query = exchange.getRequestURI().getRawQuery();

                if (query == null) {
                    sendResponse(exchange, 400, "Missing user_id.");
                    return;
                }

                String userIdValue = null;

                // Find the user_id parameter in the query string
                for (String parameter : query.split("&")) {

                    String[] parts = parameter.split("=", 2);

                    if (parts.length == 2 &&
                            parts[0].equals("user_id")) {

                        userIdValue = URLDecoder.decode(
                                parts[1],
                                StandardCharsets.UTF_8
                        );

                        break;
                    }
                }

                // Check that the user ID was supplied
                if (userIdValue == null || userIdValue.isBlank()) {
                    sendResponse(exchange, 400, "Missing user_id.");
                    return;
                }

                // Convert the user ID from text to an integer
                int userId = Integer.parseInt(userIdValue);

                // Retrieve this user's bookings from the database
                List<Map<String, Object>> bookings =
                        BookingDAO.getBookingsForUser(userId);

                // Convert the booking list into JSON
                Gson gson = new Gson();
                String response = gson.toJson(bookings);

                // Return the bookings to the frontend
                sendResponse(exchange, 200, response);
                return;
            }

            // Read the JSON data sent by the frontend
            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Gson converts JSON data into Java objects
            Gson gson = new Gson();

            String response;

            // POST: Create a new fitness class booking
            if (method.equalsIgnoreCase("POST")) {

                // Convert JSON into a BookingRequest object
                BookingRequest request =
                        gson.fromJson(requestBody, BookingRequest.class);

                // Ask the DAO to create the booking in the database
                BookingDAO.createBooking(
                        request.user_id,
                        request.class_id
                );

                // Message returned if booking creation succeeds
                response = "Class booked successfully!";
            }

            // DELETE: Cancel an existing booking
            else if (method.equalsIgnoreCase("DELETE")) {

                // Convert JSON into a CancelRequest object
                CancelRequest request =
                        gson.fromJson(requestBody, CancelRequest.class);

                // The DAO checks ownership, status, and class start time
                BookingDAO.cancelBooking(
                        request.user_id,
                        request.booking_id
                );

                // Message returned if cancellation succeeds
                response = "Booking cancelled successfully!";
            }

            // Reject unsupported HTTP methods
            else {
                sendResponse(exchange, 405, "Method not allowed.");
                return;
            }

            // Return a success response to the frontend
            sendResponse(exchange, 200, response);

        } catch (Exception e) {

            // Print the error in the Java console for debugging
            e.printStackTrace();

            // Return an error message to the frontend
            sendResponse(
                    exchange,
                    400,
                    e.getMessage() == null
                            ? "Booking request failed."
                            : e.getMessage()
            );
        }
    }

    // Sends an HTTP response back to the frontend
    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        // Convert the response message into bytes
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        // Send the HTTP status code and response length
        exchange.sendResponseHeaders(statusCode, bytes.length);

        // Send the response and close the connection
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    // Represents the data received when a user books a class
    static class BookingRequest {
        int user_id;
        int class_id;
    }

    // Represents the data received when a user cancels a booking
    static class CancelRequest {
        int user_id;
        int booking_id;
    }
}
