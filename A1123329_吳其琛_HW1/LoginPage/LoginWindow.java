import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class LoginWindow extends JFrame {

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);

    public LoginWindow() {
        setTitle("登入");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use a layout manager instead of null layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        // Row 0: username
        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("帳號:"), c);
        c.gridx = 1;
        panel.add(usernameField, c);

        // Row 1: password
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("密碼:"), c);
        c.gridx = 1;
        panel.add(passwordField, c);

        // Row 2: login button
        JButton loginButton = new JButton("登入");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, c);

        loginButton.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(loginButton);

        add(panel);
        pack();                       
        setLocationRelativeTo(null);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        
        boolean ok = "admin".equals(username)
                && Arrays.equals(password, "1234".toCharArray());

        Arrays.fill(password, '0'); 

        if (ok) {
            JOptionPane.showMessageDialog(this, "登入成功", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "帳號或密碼錯誤", "失敗",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}