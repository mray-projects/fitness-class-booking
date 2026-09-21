import api.UserAPI;
public class UserAPITest {

    public static void main(String[] args) {

        try {

            String response = UserAPI.registerUser(
                    "Test",
                    "User",
                    "test@gmail.com",
                    "test123",
                    "123456"
            );

            System.out.println("Server response:");
            System.out.println(response);

        } catch (Exception e) {

            System.out.println("Request failed!");
            e.printStackTrace();
        }
    }
}