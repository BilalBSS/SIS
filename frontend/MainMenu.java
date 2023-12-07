// MainMenu.java
package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import backend.*;

public class MainMenu extends JFrame implements ActionListener {
    JButton inventoryButton;
    JButton generateReportButton;

    public MainMenu() {
        super("Main Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the buttons
        generateReportButton = new JButton("Generate Report");
        inventoryButton = new JButton("Inventory");

        // Create a panel and add the buttons to it
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(generateReportButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(inventoryButton);
        panel.add(Box.createVerticalGlue());

        // Align the buttons horizontally
        generateReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        // Add action listeners
        generateReportButton.addActionListener(this);
        inventoryButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inventoryButton) {
            this.dispose();
            new InventoryFrame();
        } else if (e.getSource() == generateReportButton) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to generate a report?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                ReportGenerator.generateReport();
                JOptionPane.showMessageDialog(null, "Report generated successfully!");
            }
        }
    }
}
