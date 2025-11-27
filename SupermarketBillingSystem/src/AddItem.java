import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddItem extends JFrame {

    private JTextField nameField, priceField, qtyField;
    private Connection con;  // GLOBAL CONNECTION

    public AddItem() {
        // ---------- CONNECT DATABASE FIRST ----------
        connectDatabase();

        setTitle("Add Item - Supermarket Billing System");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ---------- LIGHT THEME PANEL ----------
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 255, 245));  // soft greenish white
        panel.setLayout(null);
        add(panel);

        // ---------- TITLE ----------
        JLabel title = new JLabel("Add New Item");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 120, 0)); // dark green
        title.setBounds(130, 20, 300, 40);
        panel.add(title);

        // ---------- LABELS ----------
        JLabel nameLabel = new JLabel("Item Name:");
        styleLabel(nameLabel);
        nameLabel.setBounds(50, 100, 150, 30);
        panel.add(nameLabel);

        JLabel priceLabel = new JLabel("Price:");
        styleLabel(priceLabel);
        priceLabel.setBounds(50, 160, 150, 30);
        panel.add(priceLabel);

        JLabel qtyLabel = new JLabel("Quantity:");
        styleLabel(qtyLabel);
        qtyLabel.setBounds(50, 220, 150, 30);
        panel.add(qtyLabel);

        // ---------- INPUT FIELDS ----------
        nameField = createTextField();
        nameField.setBounds(180, 100, 200, 30);
        panel.add(nameField);

        priceField = createTextField();
        priceField.setBounds(180, 160, 200, 30);
        panel.add(priceField);

        qtyField = createTextField();
        qtyField.setBounds(180, 220, 200, 30);
        panel.add(qtyField);

        // ---------- ROUNDED GREEN BUTTON ----------
        JButton addButton = new JButton("Add Item") {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 200, 0));  // bright green
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };

        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBounds(150, 300, 150, 45);

        panel.add(addButton);

        // ---------- BUTTON ACTION ----------
        addButton.addActionListener(e -> addItemToDatabase());

        setVisible(true);
    }

    // ---------- LABEL STYLING ----------
    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(new Color(40, 80, 40));   // soft green
    }

    // ---------- TEXT FIELD STYLING ----------
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 0), 2));
        return tf;
    }

    // ---------- DATABASE CONNECT ----------
    private void connectDatabase() {
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarketdb",
                "root", ""
            );
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // ---------- DATABASE INSERT ----------
    private void addItemToDatabase() {
        String name = nameField.getText();
        String price = priceField.getText();
        String qty = qtyField.getText();

        if (name.isEmpty() || price.isEmpty() || qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO producta(item_name, price, quantity) VALUES (?, ?, ?)"
            );

            pst.setString(1, name);
            pst.setString(2, price);
            pst.setString(3, qty);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Added Successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddItem();
    }
}
