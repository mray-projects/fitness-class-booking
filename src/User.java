import java.io.*;

public class User {

    String firstName;
    String lastName;
    String email;
    String username;
    String password;

    public User(String firstName, String lastName, String email,
                String username, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public void saveUser() {

        try {

            FileWriter file = new FileWriter("users.txt", true);

            file.write(firstName + "," +
                    lastName + "," +
                    email + "," +
                    username + "," +
                    password + "\n");

            file.close();

        } catch (Exception e) {

            System.out.println("Error saving user.");

        }
    }

    public static User login(String username, String password) {

        try {

            BufferedReader file =
                    new BufferedReader(new FileReader("users.txt"));

            String line;

            while ((line = file.readLine()) != null) {

                String[] data = line.split(",");

                if (data[3].equals(username)
                        && data[4].equals(password)) {

                    file.close();

                    return new User(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4]
                    );
                }
            }

            file.close();

        } catch (Exception e) {

            return null;

        }

        return null;
    }
}