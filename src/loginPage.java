import javax.swing.*;
import java.awt.*;

public class loginPage {

    JFrame frame;
    JTextField usernameField;
    JPasswordField passwordField;

    public loginPage() {

        frame = new JFrame("Fitness Class Booking System");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 1, 10, 15));


        JLabel title = new JLabel(
                "Fitness Class Booking System",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 32));


        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(400, 50));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));


        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(400, 50));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));


        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setPreferredSize(new Dimension(190, 50));
        registerButton.setPreferredSize(new Dimension(190, 50));

        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));


        JPanel usernamePanel = new JPanel(new BorderLayout(10, 5));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);


        JPanel passwordPanel = new JPanel(new BorderLayout(10, 5));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));

        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);


        loginPanel.add(title);
        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(buttonPanel);

        mainPanel.add(loginPanel);

        frame.add(mainPanel);

        //login button
        loginButton.addActionListener(e -> login());


        registerButton.addActionListener(e -> {

            frame.dispose();

            new Registration();

        });

        // Show window
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