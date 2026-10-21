
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BookingHandler {

    // Handles booking creation, cancellation, and browser requests
    public static void handleCreateBooking(HttpExchange exchange)
            throws IOException {

        // Allow the web frontend to communicate with the Java server
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin", "*"
        );

        // Specify which HTTP methods the frontend is allowed to use
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods", "POST, DELETE, OPTIONS"
        );

        // Allow the frontend to send JSON data
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers", "Content-Type"
        );

        // Browsers may send an OPTIONS request before POST or DELETE
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        try {

            // Identify which operation the frontend is requesting
            String method = exchange.getRequestMethod();

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

                // Convert the JSON data into a BookingRequest object
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

                // Convert the JSON data into a CancelRequest object
                CancelRequest request =
                        gson.fromJson(requestBody, CancelRequest.class);

                // Ask the DAO to cancel the booking
                // The DAO checks ownership, status, and class start time
                BookingDAO.cancelBooking(
                        request.user_id,
                        request.booking_id
                );

                // Message returned if cancellation succeeds
                response = "Booking cancelled successfully!";

            }

            // Reject any HTTP method other than POST, DELETE, or OPTIONS
            else {
                sendResponse(
                        exchange,
                        405,
                        "Method not allowed."
                );
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

        // Send the response message and close the connection
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }


    // Represents the data received when a user books a class
    static class BookingRequest {

        int user_id;   // ID of the user making the booking
        int class_id;  // ID of the fitness class being booked
    }


    // Represents the data received when a user cancels a booking
    static class CancelRequest {

        int user_id;     // ID of the user requesting cancellation
        int booking_id;  // ID of the specific booking to cancel
    }
}
