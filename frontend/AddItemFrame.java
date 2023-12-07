// AddItemFrame.java
package frontend;

import javax.swing.*;
import java.awt.*;

public class AddItemFrame extends JFrame {
    JList<String> itemList;

    public AddItemFrame() {
        super("Add Item");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the list of items
        String[] items = {"Book", "Desk", "White Board", "Stationery", "Lab Equipment"};
        itemList = new JList<>(items);

        // Create a panel and add the list to it
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(itemList, BorderLayout.CENTER);

        // Add action listeners
        itemList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.dispose();
                new QuantityFrame(itemList.getSelectedValue());
            }
        });

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
