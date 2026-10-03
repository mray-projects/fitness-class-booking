import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    // Register a new user in the database
    public static void registerUser(
            String firstName,
            String lastName,
            String email,
            String username,
            String password,
            String phone
    ) throws SQLException {

        // SQL statement to insert the new user
        String sql = "INSERT INTO User " +
                "(first_name, last_name, email, username, password, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Connect to the MySQL database
        Connection connection = DatabaseConnection.getConnection();

        // Prepare the SQL statement
        PreparedStatement statement = connection.prepareStatement(sql);

        // Put the user information into the ? placeholders
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setString(4, username);
        statement.setString(5, password);
        statement.setString(6, phone);

        // Execute the INSERT statement
        statement.executeUpdate();

        // Close the statement and database connection
        statement.close();
        connection.close();
    }


    // Check whether a username and password are correct
    // Returns the user's ID if login is successful
    // Returns -1 if login fails
    public static int loginUser(
            String username,
            String password
    ) throws SQLException {

        // SQL statement to find the user with the given username and password
        String sql = "SELECT user_id FROM User " +
                "WHERE username = ? AND password = ?";

        // Connect to the MySQL database
        Connection connection = DatabaseConnection.getConnection();

        // Prepare the SQL statement
        PreparedStatement statement = connection.prepareStatement(sql);

        // Put the username and password into the ? placeholders
        statement.setString(1, username);
        statement.setString(2, password);

        // Execute the SELECT query
        var resultSet = statement.executeQuery();

        // Store the user's ID
        int userId = -1;

        // Check if a matching user was found
        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
        }

        // Close the database resources
        resultSet.close();
        statement.close();
        connection.close();

        // Return the user's ID
        return userId;
    }
}