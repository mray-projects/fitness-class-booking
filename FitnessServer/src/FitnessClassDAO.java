import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FitnessClassDAO {

    // Get all fitness classes from the database
    public static List<FitnessClass> getAllClasses() throws Exception {

        String sql = "SELECT * FROM FitnessClass";

        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        List<FitnessClass> classes = new ArrayList<>();

        while (resultSet.next()) {

            FitnessClass fitnessClass = new FitnessClass();

            fitnessClass.setClass_id(resultSet.getInt("class_id"));
            fitnessClass.setName(resultSet.getString("name"));
            fitnessClass.setClass_date(resultSet.getDate("class_date"));
            fitnessClass.setClass_time(resultSet.getTime("class_time"));
            //fitnessClass.setLocation(resultSet.getString("location"));
            fitnessClass.setCapacity(resultSet.getInt("capacity"));
            fitnessClass.setTrainer_id(resultSet.getInt("trainer_id"));

            classes.add(fitnessClass);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return classes;
    }
}