import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewCart extends JFrame {

    JTable cartTable;
    DefaultTableModel model;

    public ViewCart() {
        setTitle("View Cart");
        setSize(600, 400);
        setLayout(null);

        model = new DefaultTableModel();
        cartTable = new JTable(model);

        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Total");

        JScrollPane pane = new JScrollPane(cartTable);
        pane.setBounds(20, 20, 550, 300);
        add(pane);

        loadCartData();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void loadCartData() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/supermarketdb",
                "root",
                ""
            );

            String sql = "SELECT * FROM cart";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getDouble("total")
                });
            }

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewCart();
    }
}
