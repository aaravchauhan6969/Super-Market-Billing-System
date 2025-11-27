import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateBill extends JFrame {

    JButton generateBtn, printBtn;
    JTextPane billPane;
    StyledDocument doc;

    public GenerateBill() {
        setTitle("Supermarket Billing System");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top panel with buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        generateBtn = new JButton("Generate Bill");
        printBtn = new JButton("Print Bill");
        topPanel.add(generateBtn);
        topPanel.add(printBtn);

        // Bill area
        billPane = new JTextPane();
        billPane.setEditable(false);
        billPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billPane.setBackground(new Color(255, 250, 240)); // light cream
        doc = billPane.getStyledDocument();

        JScrollPane scrollPane = new JScrollPane(billPane);
        scrollPane.getViewport().setBackground(billPane.getBackground());

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        generateBtn.addActionListener(e -> generateBill());
        printBtn.addActionListener(e -> printBill());

        setVisible(true);
    }

    void generateBill() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/supermarketdb",
                    "root",
                    ""
            );

            // Get total
            String totalQuery = "SELECT SUM(total) AS grand_total FROM cart";
            PreparedStatement pst1 = con.prepareStatement(totalQuery);
            ResultSet rs1 = pst1.executeQuery();
            double grandTotal = 0;
            if (rs1.next()) {
                grandTotal = rs1.getDouble("grand_total");
            }

            // Insert into bills
            String insertBill = "INSERT INTO bills (total) VALUES (?)";
            PreparedStatement pst2 = con.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS);
            pst2.setDouble(1, grandTotal);
            pst2.executeUpdate();
            ResultSet billIDResult = pst2.getGeneratedKeys();
            int billID = 0;
            if (billIDResult.next()) {
                billID = billIDResult.getInt(1);
            }

            // Get cart items
            String cartQuery = "SELECT * FROM cart";
            PreparedStatement pst3 = con.prepareStatement(cartQuery);
            ResultSet rs2 = pst3.executeQuery();
            String insertItem = "INSERT INTO bill_items (bill_id, product_name, price, quantity, total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst4 = con.prepareStatement(insertItem);

            // Clear previous text
            doc.remove(0, doc.getLength());

            // Styles
            SimpleAttributeSet centerBold = new SimpleAttributeSet();
            StyleConstants.setBold(centerBold, true);
            StyleConstants.setAlignment(centerBold, StyleConstants.ALIGN_CENTER);

            SimpleAttributeSet leftBold = new SimpleAttributeSet();
            StyleConstants.setBold(leftBold, true);
            StyleConstants.setAlignment(leftBold, StyleConstants.ALIGN_LEFT);

            SimpleAttributeSet normal = new SimpleAttributeSet();
            StyleConstants.setAlignment(normal, StyleConstants.ALIGN_LEFT);

            SimpleAttributeSet grayBackground = new SimpleAttributeSet();
            StyleConstants.setBackground(grayBackground, new Color(230, 230, 230)); // light gray

            // Header
            doc.insertString(doc.getLength(), "\tSUPER MARKET\n", centerBold);
            doc.setParagraphAttributes(doc.getLength(), 1, centerBold, false);
            doc.insertString(doc.getLength(), "\t123 Market Street, City\n\n", centerBold);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            doc.insertString(doc.getLength(), "Date: " + dtf.format(LocalDateTime.now()) + "\n\n", leftBold);

            // Table Header
            doc.insertString(doc.getLength(), String.format("%-20s %5s %8s %10s\n", "Product", "Qty", "Price", "Total"), leftBold);
            doc.insertString(doc.getLength(), "--------------------------------------------------\n", normal);

            boolean alternate = false;
            while (rs2.next()) {
                pst4.setInt(1, billID);
                pst4.setString(2, rs2.getString("product_name"));
                pst4.setDouble(3, rs2.getDouble("price"));
                pst4.setInt(4, rs2.getInt("quantity"));
                pst4.setDouble(5, rs2.getDouble("total"));
                pst4.executeUpdate();

                // Alternate row color
                SimpleAttributeSet rowStyle = new SimpleAttributeSet();
                if (alternate) {
                    StyleConstants.setBackground(rowStyle, new Color(240, 240, 240));
                }
                alternate = !alternate;

                doc.insertString(doc.getLength(),
                        String.format("%-20s %5d %8.2f %10.2f\n",
                                rs2.getString("product_name"),
                                rs2.getInt("quantity"),
                                rs2.getDouble("price"),
                                rs2.getDouble("total")), rowStyle);
            }

            doc.insertString(doc.getLength(), "--------------------------------------------------\n", normal);
            doc.insertString(doc.getLength(), String.format("%-35s %10.2f\n", "Grand Total: ₹", grandTotal), leftBold);
            doc.insertString(doc.getLength(), "--------------------------------------------------\n", normal);

            // Clear cart
            PreparedStatement pst5 = con.prepareStatement("DELETE FROM cart");
            pst5.executeUpdate();

            con.close();
            JOptionPane.showMessageDialog(this, "Bill Generated!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void printBill() {
        try {
            boolean complete = billPane.print();
            if (complete) JOptionPane.showMessageDialog(this, "Printing Complete");
            else JOptionPane.showMessageDialog(this, "Printing Cancelled");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GenerateBill::new);
    }
}
