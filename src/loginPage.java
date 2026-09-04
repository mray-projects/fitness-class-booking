import javax.swing.*;
import java.awt.*;

public class loginPage {

    JFrame frame;
    JTextField usernameField;
    JPasswordField passwordField;

    public loginPage() {

        frame = new JFrame("Fitness Class Booking System");

        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        frame.add(usernameLabel);
        frame.add(usernameField);

        frame.add(passwordLabel);
        frame.add(passwordField);

        frame.add(loginButton);
        frame.add(registerButton);

        frame.add(new JLabel("Fitness Class"));
        frame.add(new JLabel("Booking System"));

        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {

            frame.dispose();

            new Registration();

        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void login() {

        String username = usernameField.getText();

        String password =
                new String(passwordField.getPassword());

        User user = User.login(username, password);

        if (user != null) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Welcome to the Fitness Class, "
                            + user.firstName + "!"
            );

        } else {

            JOptionPane.showMessageDialog(
                    frame,
                    "Incorrect username or password."
            );
        }
    }
}