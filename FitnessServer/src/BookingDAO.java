import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {

    // Create a booking
    public static void createBooking(int userId, int classId) throws Exception {

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
}