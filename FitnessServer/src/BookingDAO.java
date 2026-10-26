import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class BookingDAO {

    // Create a booking
    public static void createBooking(int userId, int classId) throws Exception
    {

        Connection connection = DatabaseConnection.getConnection();

        // First, check the class capacity
        String capacitySql =
                "SELECT capacity FROM FitnessClass WHERE class_id = ?";

        PreparedStatement capacityStatement =
                connection.prepareStatement(capacitySql);

        capacityStatement.setInt(1, classId);

        ResultSet capacityResult =
                capacityStatement.executeQuery();

        if (!capacityResult.next()) {
            capacityResult.close();
            capacityStatement.close();
            connection.close();

            throw new Exception("Class not found.");
        }

        int capacity = capacityResult.getInt("capacity");

        capacityResult.close();
        capacityStatement.close();


        // Count the current confirmed bookings
        String countSql =
                "SELECT COUNT(*) FROM Booking " +
                        "WHERE class_id = ? AND status = 'CONFIRMED'";

        PreparedStatement countStatement =
                connection.prepareStatement(countSql);

        countStatement.setInt(1, classId);

        ResultSet countResult =
                countStatement.executeQuery();

        countResult.next();

        int currentBookings = countResult.getInt(1);

        countResult.close();
        countStatement.close();


        // Check whether the class is full
        if (currentBookings >= capacity) {

            connection.close();

            throw new Exception("Class is full.");
        }


        // Check whether this user already has a confirmed booking
        String existingSql =
                "SELECT * FROM Booking " +
                        "WHERE user_id = ? " +
                        "AND class_id = ? " +
                        "AND status = 'CONFIRMED'";

        PreparedStatement existingStatement =
                connection.prepareStatement(existingSql);

        existingStatement.setInt(1, userId);
        existingStatement.setInt(2, classId);

        ResultSet existingResult =
                existingStatement.executeQuery();

        if (existingResult.next()) {

            existingResult.close();
            existingStatement.close();
            connection.close();

            throw new Exception("You have already booked this class.");
        }

        existingResult.close();
        existingStatement.close();


        // Create the booking
        String insertSql =
                "INSERT INTO Booking (user_id, class_id) " +
                        "VALUES (?, ?)";

        PreparedStatement insertStatement =
                connection.prepareStatement(insertSql);

        insertStatement.setInt(1, userId);
        insertStatement.setInt(2, classId);

        insertStatement.executeUpdate();

        insertStatement.close();
        connection.close();
    }


    // Cancel a booking
    public static void cancelBooking(int userId, int bookingId)
            throws Exception
    {

        Connection connection = DatabaseConnection.getConnection();

        try {
            // Find the booking and its class start time
            String checkSql =
                    "SELECT b.status, " +
                            "TIMESTAMP(f.class_date, f.class_time) AS start_time " +
                            "FROM Booking b " +
                            "JOIN FitnessClass f ON b.class_id = f.class_id " +
                            "WHERE b.booking_id = ? AND b.user_id = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(checkSql)) {

                statement.setInt(1, bookingId);
                statement.setInt(2, userId);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        throw new Exception("Booking not found.");
                    }

                    if (!result.getString("status").equals("CONFIRMED")) {
                        throw new Exception("This booking is already cancelled.");
                    }

                    if (!result.getTimestamp("start_time")
                            .after(new java.sql.Timestamp(System.currentTimeMillis()))) {
                        throw new Exception(
                                "Cannot cancel after the class has started."
                        );
                    }
                }
            }

            // Cancel only if the booking is still confirmed
            String updateSql =
                    "UPDATE Booking SET status = 'CANCELLED' " +
                            "WHERE booking_id = ? AND user_id = ? " +
                            "AND status = 'CONFIRMED'";

            try (PreparedStatement statement =
                         connection.prepareStatement(updateSql)) {

                statement.setInt(1, bookingId);
                statement.setInt(2, userId);

                if (statement.executeUpdate() == 0) {
                    throw new Exception(
                            "Booking could not be cancelled. Please try again."
                    );
                }
            }

        } finally {
            connection.close();
        }
    }


    // Retrieve all bookings belonging to a user
    public static List<Map<String, Object>> getBookingsForUser(int userId)
            throws Exception {

        // Store the bookings retrieved from the database
        List<Map<String, Object>> bookings = new ArrayList<>();

        Connection connection = DatabaseConnection.getConnection();

        try {

            // Join Booking and FitnessClass to retrieve class details
            String sql =
                    "SELECT b.booking_id, b.status, " +
                            "f.name AS class_name, " +
                            "f.class_date, f.class_time, f.location " +
                            "FROM Booking b " +
                            "JOIN FitnessClass f ON b.class_id = f.class_id " +
                            "WHERE b.user_id = ? " +
                            "ORDER BY f.class_date, f.class_time";

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                // Retrieve bookings for the specified user only
                statement.setInt(1, userId);

                try (ResultSet result = statement.executeQuery()) {

                    // Process each booking returned by the query
                    while (result.next()) {

                        Map<String, Object> booking =
                                new LinkedHashMap<>();

                        booking.put(
                                "booking_id",
                                result.getInt("booking_id")
                        );

                        booking.put(
                                "status",
                                result.getString("status")
                        );

                        booking.put(
                                "class_name",
                                result.getString("class_name")
                        );

                        booking.put(
                                "class_date",
                                result.getString("class_date")
                        );

                        booking.put(
                                "class_time",
                                result.getString("class_time")
                        );

                        booking.put(
                                "location",
                                result.getString("location")
                        );

                        bookings.add(booking);
                    }
                }
            }

        } finally {
            connection.close();
        }

        // Return the list of bookings to BookingHandler
        return bookings;
    }


}