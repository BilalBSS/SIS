// OrderSuppliesFrame.java
package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OrderSuppliesFrame extends JFrame implements ActionListener {
    JList<String> itemList;
    JButton orderButton;
    JButton backToMainMenuButton;
    JTextField quantityField;
    Connection conn;
    JFrame mainMenuFrame;  // Reference to the main menu frame

    public OrderSuppliesFrame(JFrame mainMenuFrame) {
        super("Order Supplies");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.mainMenuFrame = mainMenuFrame;  // Initialize the main menu frame

        // Create the list of items
        String[] items = {"Book", "Desk", "White Board", "Stationery", "Lab Equipment"};
        itemList = new JList<>(items);

        // Create the buttons
        orderButton = new JButton("Order");
        backToMainMenuButton = new JButton("Back to Main Menu");

        // Add action listeners
        orderButton.addActionListener(this);
        backToMainMenuButton.addActionListener(this);

        // Create the quantity field
        quantityField = new JTextField(5);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(backToMainMenuButton, BorderLayout.NORTH); // Add the button at the top of the panel
        panel.add(orderButton, BorderLayout.CENTER);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }

        // Add the list and the buttons to the frame
        JPanel southPanel = new JPanel();
        southPanel.add(new JLabel("Quantity: "));
        southPanel.add(quantityField);
        southPanel.add(orderButton);
        add(new JScrollPane(itemList), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH); // Add the panel with the buttons at the north of the frame

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToMainMenuButton) {
            // Switch back to the main menu
            this.setVisible(false);
            mainMenuFrame.setVisible(true);
        }
        else if (e.getSource() == orderButton) {
            String selectedItem = itemList.getSelectedValue();
            if (selectedItem != null && !quantityField.getText().isEmpty()) {
                int quantityToOrder = Integer.parseInt(quantityField.getText());

                // Add the selected item to the database
                try {
                    PreparedStatement pstmt = conn.prepareStatement("UPDATE Items SET Quantity = Quantity + ? WHERE Item = ?");
                    pstmt.setInt(1, quantityToOrder);  // Get the quantity from the text field
                    pstmt.setString(2, selectedItem);  // Get the item name from the selected value
                    pstmt.executeUpdate();
                    pstmt.close();

                    // Show a confirmation dialog
                    JOptionPane.showMessageDialog(this, "Order placed successfully!");

                    // Switch back to the main menu
                    this.setVisible(false);
                    mainMenuFrame.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
