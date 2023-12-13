package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class OrderSuppliesFrame extends JFrame implements ActionListener {
    JList<String> itemList;
    JButton addToCartButton, backToMainMenuButton, goToCartButton;
    JTextField quantityField;
    Connection conn;
    JFrame mainMenuFrame; 
    HashMap<String, Integer> cart;  
    CartFrame cartFrame;  
    HashMap<String, Double> prices = new HashMap<>();

    public OrderSuppliesFrame(JFrame mainMenuFrame) {
        super("Order Supplies");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.mainMenuFrame = mainMenuFrame;  // Initialize the main menu frame

        // Initialize the prices
        prices.put("Book", 15.99);  // Price for a book
        prices.put("Desk", 120.00);  // Price for a desk
        prices.put("White Board", 45.50);  // Price for a white board
        prices.put("Stationery", 10.00);  // Price for stationery
        prices.put("Lab Equipment", 250.00);  // Price for lab equipment

        // Create the list of items
        String[] items = {"Book", "Desk", "White Board", "Stationery", "Lab Equipment"};
        itemList = new JList<>(items);

        // Create the buttons
        backToMainMenuButton = new JButton("Back to Main Menu");

        // Add action listeners
        backToMainMenuButton.addActionListener(this);

        // Create the quantity field
        quantityField = new JTextField(5);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(backToMainMenuButton, BorderLayout.NORTH); // Add the button at the top of the panel

        // Create the cart
        cart = new HashMap<>();

        // Create the cart frame and pass this frame to it
        cartFrame = new CartFrame(this);

        // Create the add to cart button
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(this);

        // Create the go to cart button
        goToCartButton = new JButton("Go to Cart");
        goToCartButton.addActionListener(this);

        // Add the add to cart button to the panel
        JPanel southPanel = new JPanel();
        southPanel.add(new JLabel("Quantity: "));
        southPanel.add(quantityField);
        southPanel.add(addToCartButton);
        southPanel.add(goToCartButton);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }

        // Add the list and the buttons to the frame
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
        else if (e.getSource() == addToCartButton) {
            String selectedItem = itemList.getSelectedValue();
            if (selectedItem != null && !quantityField.getText().isEmpty()) {
                int quantityToAdd = Integer.parseInt(quantityField.getText());
                cart.put(selectedItem, cart.getOrDefault(selectedItem, 0) + quantityToAdd);
                JOptionPane.showMessageDialog(this, "Item added to cart!");
            }
        }
        else if (e.getSource() == goToCartButton) {
            // Show the cart frame
            cartFrame.updateCart(cart);
            cartFrame.setVisible(true);
        }
    }
}
