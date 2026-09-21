public class UserDAOTest
{

    public static void main(String[] args) {

        try {

            UserDAO.registerUser(
                    "John",
                    "Smith",
                    "john2@email.com",
                    "john1234",
                    "123456",
                    "0501234567"
            );

            System.out.println("User registered successfully!");

        } catch (Exception e) {

            System.out.println("Registration failed!");
            e.printStackTrace();
        }
    }
}