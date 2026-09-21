import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO
{

    public static void registerUser(
            String firstName,
            String lastName,
            String email,
            String username,
            String password,
            String phone
    ) throws SQLException {

        String sql = "INSERT INTO User " +
                "(first_name, last_name, email, username, password, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setString(4, username);
        statement.setString(5, password);
        statement.setString(6, phone);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }
}