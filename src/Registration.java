import javax.swing.*;
import java.awt.*;

public class Registration {

    JFrame frame;

    JTextField firstNameField;
    JTextField lastNameField;
    JTextField emailField;
    JTextField usernameField;

    JPasswordField passwordField;
    JPasswordField reenterPasswordField;

    public Registration() {

        frame = new JFrame("Register");

        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 2, 10, 10));

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        usernameField = new JTextField();

        passwordField = new JPasswordField();
        reenterPasswordField = new JPasswordField();

        frame.add(new JLabel("First Name:"));
        frame.add(firstNameField);

        frame.add(new JLabel("Last Name:"));
        frame.add(lastNameField);

        frame.add(new JLabel("Email:"));
        frame.add(emailField);

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);

        frame.add(new JLabel("Password:"));
        frame.add(passwordField);

        frame.add(new JLabel("Re-enter Password:"));
        frame.add(reenterPasswordField);

        JButton registerButton = new JButton("Register");

        frame.add(new JLabel(""));
        frame.add(registerButton);

        registerButton.addActionListener(e -> register());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void register() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();

        String password =
                new String(passwordField.getPassword());

        String reenterPassword =
                new String(reenterPasswordField.getPassword());

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || email.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || reenterPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please fill in all fields."
            );

            return;
        }

        if (!password.equals(reenterPassword)) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Passwords do not match."
            );

            return;
        }

        User user = new User(
                firstName,
                lastName,
                email,
                username,
                password
        );

        user.saveUser();

        JOptionPane.showMessageDialog(
                frame,
                "Thank you for registering!"
        );

        frame.dispose();

        new loginPage();
    }
}