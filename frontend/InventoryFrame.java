// InventoryFrame.java
package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class InventoryFrame extends JFrame implements ActionListener {
    JButton addItemButton;
    JButton removeItemButton; 
    JButton mainMenuButton;
    JList<String> inventoryList;
    Connection conn;

    public InventoryFrame() {
        super("Inventory");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        // Create the buttons
        addItemButton = new JButton("Add Item");
        removeItemButton = new JButton("Remove Item");  
        mainMenuButton = new JButton("Back to Main Menu");
    
        // Create a panel for the buttons and set its layout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
    
        // Add the buttons to the button panel
        buttonPanel.add(addItemButton);
        buttonPanel.add(removeItemButton);
    
        // Create a panel and add the buttons and main menu button to it
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.NORTH);  
        panel.add(mainMenuButton, BorderLayout.SOUTH);
    
        // Add action listeners
        addItemButton.addActionListener(this);
        removeItemButton.addActionListener(this);  
        mainMenuButton.addActionListener(this);
    
        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");
    
            // Get the data from the Items table
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Items");
            ResultSet rs = pstmt.executeQuery();
    
            // Get the row data
            Vector<String> items = new Vector<>();
            while (rs.next()) {
                items.add(rs.getString("Item") + ": " + rs.getInt("Quantity"));
            }
    
            // Create the list and add it to a scroll pane
            inventoryList = new JList<>(items);
            JScrollPane scrollPane = new JScrollPane(inventoryList);
            panel.add(scrollPane, BorderLayout.CENTER);
    
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    
        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);
    
        setVisible(true);
    }
        
    public void refreshInventoryList() {
        try {
            // Get the data from the Items table
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Items");
            ResultSet rs = pstmt.executeQuery();
    
            // Get the row data
            DefaultListModel<String> items = new DefaultListModel<>();
            while (rs.next()) {
                items.addElement(rs.getString("Item") + ": " + rs.getInt("Quantity"));
            }
    
            // Update the list model
            inventoryList.setModel(items);
    
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItemButton) {
            new AddItemFrame(this);
        } else if (e.getSource() == removeItemButton) {  // New action for the "Remove Item" button
            new RemoveItemFrame(this);  // Open the RemoveItemFrame
        } else if (e.getSource() == mainMenuButton) {
            this.dispose();
            new MainMenu();
        }
    }
}