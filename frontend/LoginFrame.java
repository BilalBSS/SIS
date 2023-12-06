// LoginFrame.java
package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    JTextField txuser;
    JPasswordField pass;
    JButton blogin;
    JButton bregister;

    public LoginFrame() {
        super("Login Authentication");

        txuser = new JTextField(10);
        pass = new JPasswordField(10);
        bregister = new JButton("Register");
        blogin = new JButton("Login");

        setLayout(new GridBagLayout());
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(20, 20, 20, 20);

        constraints.gridx = 0;
        constraints.gridy = 0;     
        add(new JLabel("Username"), constraints);

        constraints.gridx = 1;
        add(txuser, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;  
        add(new JLabel("Password"), constraints);

        constraints.gridx = 1;
        add(pass, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(blogin, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(bregister, constraints);

        blogin.addActionListener(this);
        bregister.addActionListener(this);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==blogin) {
            // authentication logic here
            Database db = new Database();
            db.createTable();
            String username = txuser.getText();
            String password = new String(pass.getPassword());
            if (db.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // proceed to the next frame
                this.dispose();
                new MainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } else if (e.getSource() == bregister) {
            // registration logic here
            String username = txuser.getText();
            String password = new String(pass.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username and a password!");
                return;
            }
            Database db = new Database();
            db.createTable();
            if (db.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "This username is already taken!");
                return;
            }
            db.insertUser(username, password);
            JOptionPane.showMessageDialog(this, "Registration successful!");
        }
    }
}
