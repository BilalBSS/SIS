package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CartFrame extends JFrame implements ActionListener {
    JTextArea cartArea;
    JButton orderButton, backButton;
    Connection conn;
    OrderSuppliesFrame orderSuppliesFrame;  // Reference to the order supplies frame

    public double calculateTotalCost(HashMap<String, Integer> cart) {
        double totalCost = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            totalCost += orderSuppliesFrame.prices.get(entry.getKey()) * entry.getValue();
        }
        return totalCost;
    }

    public CartFrame(OrderSuppliesFrame orderSuppliesFrame) {
        super("Cart");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.orderSuppliesFrame = orderSuppliesFrame;  // Initialize the order supplies frame

        // Create the text area for the cart
        cartArea = new JTextArea();
        cartArea.setEditable(false);

        // Create the order button
        orderButton = new JButton("Order");
        orderButton.addActionListener(this);

        // Create the back button
        backButton = new JButton("Back");
        backButton.addActionListener(this);

        // Add the text area and the order button to the frame
        add(new JScrollPane(cartArea), BorderLayout.CENTER);
        add(orderButton, BorderLayout.SOUTH);
        add(backButton, BorderLayout.NORTH);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }

        setVisible(false);
    }

    public void updateCart(HashMap<String, Integer> cart) {
        // Update the text area with the items, their quantities, and their prices in the cart
        StringBuilder cartText = new StringBuilder();
        double totalCost = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            double price = orderSuppliesFrame.prices.get(entry.getKey());
            double cost = price * entry.getValue();
            totalCost += cost;
            cartText.append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(" (").append(price).append(" each, ")
                    .append(cost).append(" total)\n");
        }
        cartText.append("\nTotal cost: ").append(totalCost);
        cartArea.setText(cartText.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderButton) {
            // Order the items in the cart
            HashMap<String, Integer> cart = new HashMap<>();
            String[] lines = cartArea.getText().split("\n");
            for (int i = 0; i < lines.length - 2; i++) {  // Exclude the last two lines (empty line and total cost)
                String[] parts = lines[i].split(": ");
                String[] quantityAndPrice = parts[1].split(" ");
                cart.put(parts[0], Integer.parseInt(quantityAndPrice[0]));
            }
            if (!cart.isEmpty()) {
                // Add the selected item to the database
                try {
                    PreparedStatement pstmt = conn.prepareStatement("UPDATE Items SET Quantity = Quantity + ? WHERE Item = ?");
                    for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                        pstmt.setInt(1, entry.getValue());  // Get the quantity from the text field
                        pstmt.setString(2, entry.getKey());  // Get the item name from the selected value
                        pstmt.executeUpdate();
                    }
                    pstmt.close();
    
                    // Show a confirmation dialog
                    JOptionPane.showMessageDialog(this, "Order placed successfully!");
    
                    double totalCost = calculateTotalCost(cart);

                    // Clear the cart after ordering
                    cart.clear();
                    updateCart(cart);
                    
                    // Hide this frame and the order supplies frame, and show the main menu frame
                    this.setVisible(false);
                    orderSuppliesFrame.setVisible(false);
                    orderSuppliesFrame.mainMenuFrame.setVisible(true);
                    JOptionPane.showMessageDialog(this, "Order placed successfully! Total cost: $" + totalCost);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == backButton) {
            // Hide this frame and show the order supplies frame
            this.setVisible(false);
            orderSuppliesFrame.setVisible(true);
        }
    }    
}
