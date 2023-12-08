package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class RemoveItemFrame extends JFrame implements ActionListener {
    JList<String> inventoryList;
    JButton removeItemButton;
    JTextField quantityField;
    Connection conn;
    JFrame mainMenuFrame;  // Reference to the main menu frame
    private JButton backToInventoryButton;

    public RemoveItemFrame(JFrame mainMenuFrame) {
        super("Remove Item");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        this.mainMenuFrame = mainMenuFrame;  // Initialize the main menu frame

        // Create the button
        removeItemButton = new JButton("Remove Item");
        backToInventoryButton = new JButton("Back to Inventory");

        // Add action listener
        removeItemButton.addActionListener(this);
        backToInventoryButton.addActionListener(this);  

        // Create the quantity field
        quantityField = new JTextField(5);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(backToInventoryButton, BorderLayout.NORTH); // Add the button at the top of the panel
        panel.add(removeItemButton, BorderLayout.CENTER);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");

            // Get the data from the Items table
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Items");
            ResultSet rs = pstmt.executeQuery();

            // Get the row data
            DefaultListModel<String> items = new DefaultListModel<>();
            while (rs.next()) {
                items.addElement(rs.getString("Item") + ": " + rs.getInt("Quantity"));
            }

            // Create the list and add it to a scroll pane
            inventoryList = new JList<>(items);
            JScrollPane scrollPane = new JScrollPane(inventoryList);

            // Add the scroll pane and the button to the frame
            JPanel southPanel = new JPanel();
            southPanel.add(new JLabel("Quantity: "));
            southPanel.add(quantityField);
            southPanel.add(removeItemButton);
            add(scrollPane, BorderLayout.CENTER);
            add(southPanel, BorderLayout.SOUTH);
            add(panel, BorderLayout.NORTH); // Add the panel with the "Back to Inventory" button at the north of the frame

            pstmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToInventoryButton) {
            // Switch back to the inventory frame
            this.setVisible(false);
            mainMenuFrame.setVisible(true);
        }
        else if (e.getSource() == removeItemButton) {
            String selectedItem = inventoryList.getSelectedValue();
            if (selectedItem != null && !quantityField.getText().isEmpty()) {
                int quantityToRemove = Integer.parseInt(quantityField.getText());
                int currentQuantity = Integer.parseInt(selectedItem.split(": ")[1]);

                if (quantityToRemove > currentQuantity) {
                    // Show an error message
                    JOptionPane.showMessageDialog(this, "Cannot remove more items than available in the inventory!");
                } else {
                    // Remove the selected item from the database
                    try {
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE Items SET Quantity = Quantity - ? WHERE Item = ?");
                        pstmt.setInt(1, quantityToRemove);  // Get the quantity from the text field
                        pstmt.setString(2, selectedItem.split(": ")[0]);  // Get the item name from the selected value
                        pstmt.executeUpdate();
                        pstmt.close();

                        // Show a confirmation dialog
                        JOptionPane.showMessageDialog(this, "Item removed successfully!");

                        // Refresh the inventory list in the main menu
                        ((InventoryFrame) mainMenuFrame).refreshInventoryList();

                        // Switch back to the main menu
                        this.setVisible(false);
                        mainMenuFrame.setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Update the selected item in the list
                    int selectedIndex = inventoryList.getSelectedIndex();
                    String itemName = selectedItem.split(": ")[0];
                    int newQuantity = currentQuantity - quantityToRemove;
                    ((DefaultListModel<String>) inventoryList.getModel()).setElementAt(itemName + ": " + newQuantity, selectedIndex);
                }
            }
        }
    }
}
