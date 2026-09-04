import javax.swing.*;

public class main {

    public static void main(String[] args) {

        // Registration
        String name = JOptionPane.showInputDialog("Enter your name:");
        String username = JOptionPane.showInputDialog("Enter username:");
        String email = JOptionPane.showInputDialog("Enter email:");
        String password = JOptionPane.showInputDialog("Enter password:");

        JOptionPane.showMessageDialog(null, "Registration successful!");

        // Login
        while (true) {

            String loginUsername = JOptionPane.showInputDialog("Enter username:");
            String loginPassword = JOptionPane.showInputDialog("Enter password:");

            if (loginUsername.equals(username) &&
                    loginPassword.equals(password)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Welcome to the Fitness Class, " + name + "!"
                );

                break;

            } else {

                JOptionPane.showMessageDialog
                        (
                        null,
                        "Wrong username or password. Try again."
                );
            }
        }
    }
}