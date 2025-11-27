import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public LoginPage() {

        setTitle("Login — Billing System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // BACKGROUND PANEL WITH IMAGE + DARK OVERLAY
        JPanel bg = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(getClass().getResource("bg.jpg")).getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(20, 20, 20)); // fallback color
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                // DARK TRANSPARENT OVERLAY
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(null);

        // TITLE (NEON EFFECT)
        JLabel title = new JLabel("Billing System Login");
        title.setBounds(110, 40, 400, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0, 200, 255));
        bg.add(title);

        
        // USER LABEL
        JLabel ul = new JLabel("Username");
        ul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ul.setForeground(Color.WHITE);
        ul.setBounds(110, 120, 200, 20);
        bg.add(ul);

        // USER FIELD (GLASS EFFECT)
        userField = new JTextField();
        userField.setBounds(110, 145, 270, 35);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userField.setBackground(new Color(255, 255, 255, 180));
        userField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bg.add(userField);

        // PASS LABEL
        JLabel pl = new JLabel("Password");
        pl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pl.setForeground(Color.WHITE);
        pl.setBounds(110, 200, 200, 20);
        bg.add(pl);

        // PASS FIELD (GLASS EFFECT)
        passField = new JPasswordField();
        passField.setBounds(110, 225, 270, 35);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passField.setBackground(new Color(255, 255, 255, 180));
        passField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bg.add(passField);

        // LOGIN BUTTON (NEON GLOW)
        JButton login = new JButton("LOGIN");
        login.setBounds(165, 290, 160, 40);
        login.setFont(new Font("Segoe UI", Font.BOLD, 18));
        login.setForeground(Color.BLACK);
        login.setBackground(new Color(0, 200, 255));
        login.setFocusPainted(false);
        login.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2, true));
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));

        login.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                login.setBackground(new Color(0, 230, 255));
            }
            public void mouseExited(MouseEvent e) {
                login.setBackground(new Color(0, 200, 255));
            }
        });

        login.addActionListener(e -> {

            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("user")) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
                new MainMenu().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bg.add(login);

        add(bg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
