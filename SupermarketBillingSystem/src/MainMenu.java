import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MainMenu extends JFrame {

    private Connection con;

    public MainMenu() {

        // CONNECT DB FIRST
        connectDatabase();

        setTitle("Supermarket Billing - Main Menu");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 500));
        sidebar.setBackground(new Color(220, 245, 220));
        sidebar.setLayout(null);

        JLabel menuTitle = new JLabel("MENU");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        menuTitle.setForeground(new Color(0, 100, 0));
        menuTitle.setBounds(75, 20, 200, 40);
        sidebar.add(menuTitle);

        JButton addItemBtn = createMenuButton("Add Item", "icons/add.png");
        addItemBtn.setBounds(20, 100, 180, 45);
        sidebar.add(addItemBtn);
        
        JButton selectItemBtn = createMenuButton("Select Item", "icons/select.png");
        selectItemBtn.setBounds(20, 310, 180, 45); // agar exit button neeche shift karna hai to 380 etc
        sidebar.add(selectItemBtn);

        JButton generateBillBtn = createMenuButton("Generate Bill", "icons/bill.png");
        generateBillBtn.setBounds(20, 170, 180, 45);
        sidebar.add(generateBillBtn);
        
        


        JButton viewItemsBtn = createMenuButton("View Cart", "icons/view.png");
        viewItemsBtn.setBounds(20, 240, 180, 45);
        sidebar.add(viewItemsBtn);

        JButton exitBtn = createMenuButton("Exit", "icons/exit.png");
        exitBtn.setBounds(20, 380, 180, 45);
        sidebar.add(exitBtn);

        add(sidebar, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setBackground(new Color(245, 255, 245));
        center.setLayout(null);

        JLabel welcome = new JLabel("WELCOME TO SUPERMARKET SYSTEM");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setForeground(new Color(0, 120, 0));
        welcome.setBounds(180, 180, 500, 40);
        center.add(welcome);

        add(center, BorderLayout.CENTER);

        addItemBtn.addActionListener(e -> new AddItem());
        selectItemBtn.addActionListener(e -> new SelectItem());
        viewItemsBtn.addActionListener(e -> new ViewCart());
        generateBillBtn.addActionListener(e -> new GenerateBill());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // -------- DATABASE CONNECTION FUNCTION --------
    private void connectDatabase() {
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarketdb",
                "root",
                ""
            );
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIcon(new ImageIcon(iconPath));
        btn.setBackground(new Color(200, 235, 200));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(170, 225, 170));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(200, 235, 200));
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
