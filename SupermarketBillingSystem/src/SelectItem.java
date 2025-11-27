import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SelectItem extends JFrame {

    JComboBox<String> productDropdown;
    JTextField qtyField;
    JButton addBtn;

    public SelectItem() {
        setTitle("Select Item");
        setSize(400, 300);
        setLayout(null);

        JLabel productLabel = new JLabel("Select Product:");
        productLabel.setBounds(50, 50, 120, 30);
        add(productLabel);

        productDropdown = new JComboBox<>();
        productDropdown.setBounds(170, 50, 150, 30);
        add(productDropdown);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setBounds(50, 100, 120, 30);
        add(qtyLabel);

        qtyField = new JTextField();
        qtyField.setBounds(170, 100, 150, 30);
        add(qtyField);

        addBtn = new JButton("Add to Cart");
        addBtn.setBounds(120, 170, 150, 40);
        add(addBtn);

        loadProducts();

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void loadProducts() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarketdb",
                "root",
                ""
            );

            String sql = "SELECT name FROM products";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                productDropdown.addItem(rs.getString("name"));
            }

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    void addToCart() {
        String name = (String) productDropdown.getSelectedItem();
        int qty = Integer.parseInt(qtyField.getText());

        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarketdb",
                "root",
                ""
            );

            // Fetch product price
            String priceQuery = "SELECT price FROM products WHERE name=?";
            PreparedStatement pst1 = con.prepareStatement(priceQuery);
            pst1.setString(1, name);
            ResultSet rs = pst1.executeQuery();

            double price = 0;
            if (rs.next()) price = rs.getDouble("price");

            double total = price * qty;

            // Insert into cart table
            String insertQuery = "INSERT INTO cart (product_name, price, quantity, total) VALUES (?, ?, ?, ?)";
            PreparedStatement pst2 = con.prepareStatement(insertQuery);

            pst2.setString(1, name);
            pst2.setDouble(2, price);
            pst2.setInt(3, qty);
            pst2.setDouble(4, total);

            pst2.executeUpdate();

            JOptionPane.showMessageDialog(null, "Item added to cart!");

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SelectItem();
    }
}
