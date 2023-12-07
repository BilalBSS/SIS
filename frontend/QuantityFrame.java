// QuantityFrame.java
package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class QuantityFrame extends JFrame implements ActionListener {
    JSpinner quantitySpinner;
    String item;
    Connection conn;

    public QuantityFrame(String item) {
        super("Select Quantity");
        this.item = item;

        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the spinner
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(model);

        // Create the confirm button
        JButton confirmButton = new JButton("Confirm");

        // Create a panel and add the spinner and confirm button to it
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(new JLabel("Select quantity for " + item));
        panel.add(quantitySpinner);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(confirmButton);
        panel.add(Box.createVerticalGlue());

        // Align the spinner and confirm button horizontally
        quantitySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners
        confirmButton.addActionListener(this);

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");

            // Create the Items table if it does not exist
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Items " +
                         "(Item VARCHAR(255) PRIMARY KEY NOT NULL," +
                         " Quantity INTEGER NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int quantity = (Integer) quantitySpinner.getValue();
        JOptionPane.showMessageDialog(this, "You have added " + quantity + " " + item + "(s) to the inventory.");

        // Update the database
        try {
            // Check if the item already exists in the table
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Items WHERE Item = ?");
            pstmt.setString(1, item);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the item exists, update the quantity
                pstmt = conn.prepareStatement("UPDATE Items SET Quantity = Quantity + ? WHERE Item = ?");
                pstmt.setInt(1, quantity);
                pstmt.setString(2, item);
            } else {
                // If the item does not exist, insert a new row
                pstmt = conn.prepareStatement("INSERT INTO Items(Item, Quantity) VALUES(?, ?)");
                pstmt.setString(1, item);
                pstmt.setInt(2, quantity);
            }

            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // Navigate back to the InventoryFrame
        this.dispose();
        new InventoryFrame();
    }
}
