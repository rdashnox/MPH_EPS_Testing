/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;


/**
 *
 * @author dashcodes
 */

import javax.swing.*;
import BusinessLogic.EmployeeDetails;
import Controller.RoleAuthenticator;
import Controller.DBQueries;
import Controller.UserSession;
import DatabaseConnector.DatabaseConnection;
import GUI.AdminDashboard;
import GUI.EmployeeDashboard;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginPage extends JFrame {
    private RoleAuthenticator roleAuthenticator;
    private Connection connection;
    private DBQueries dbq = new DBQueries();

    public LoginPage() {
        initComponents();
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        usernameField.setFocusable(true);

        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.roleAuthenticator = new RoleAuthenticator();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Failed to initialize database connection: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verifyUsername() {
        String username = usernameField.getText().trim();

        if (!username.isEmpty()) {
            try {
                boolean userExists = roleAuthenticator.checkUsernameExists(username);
                forgotPasswordButton.setEnabled(userExists);

                if (!userExists) {
                    JOptionPane.showMessageDialog(this,
                        "Username not found in database",
                        "Verification Failed",
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Database connection error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                forgotPasswordButton.setEnabled(false);
            }
        } else {
            forgotPasswordButton.setEnabled(false);
        }
    }
    
    private String validateCredentials(String username, String password) {
    try {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement(dbq.userDesignation)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Note: In production, use hashed passwords!
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("designation");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (SQLException ex) {
        Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Custom login method that validates credentials and redirects to appropriate dashboard
     */
    private void loginUser() throws SQLException {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
                // Verify credentials and get user details
    EmployeeDetails loggedInUser = roleAuthenticator.authenticateAndGetUserDetails(username, password);
    
    if (loggedInUser != null) {
        // Redirect to appropriate dashboard based on role
        String designation = loggedInUser.getDesignation();
        
        if (designation == null) {
            JOptionPane.showMessageDialog(this,
                "User designation not found",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Determine dashboard based on user role
        if (designation.contains("Rank and File")) {
            new EmployeeDashboard(
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getDesignation(),
                loggedInUser.getEid(),
                loggedInUser.getBasicSalary(),
                loggedInUser.getTotalAllowances(),
                loggedInUser.getTotalIncentives(), 
                loggedInUser.getTotalDeductions(),
                loggedInUser.getNetPay(),
                username,
                connection
            ).setVisible(true);
        } else {
            new AdminDashboard(
                username,
                connection
            ).setVisible(true);
        }
        
        this.dispose(); // Close login form
    } else {
        JOptionPane.showMessageDialog(this,
            "Invalid username or password",
            "Login Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        forgotPasswordButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        imagePanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MotorPH");
        setMinimumSize(new java.awt.Dimension(600, 400));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(37, 61, 144));
        jLabel1.setText("Login");

        jLabel2.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(150, 150, 150));
        jLabel2.setText("Login to your account.");

        jLabel3.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(37, 61, 144));
        jLabel3.setText("Password");

        jLabel4.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(37, 61, 144));
        jLabel4.setText("Username");

        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameFieldFocusLost(evt);
            }
        });
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        loginButton.setBackground(new java.awt.Color(37, 61, 144));
        loginButton.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        forgotPasswordButton.setFont(new java.awt.Font("Candara", 0, 10)); // NOI18N
        forgotPasswordButton.setText("Forgot Password");
        forgotPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotPasswordButtonActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource Folder/MotorPHLogo big.png"))); // NOI18N

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(forgotPasswordButton)
                        .addGap(155, 155, 155))))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loginButton)
                .addGap(18, 18, 18)
                .addComponent(forgotPasswordButton)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        getContentPane().add(loginPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 500));

        imagePanel.setBackground(new java.awt.Color(0, 0, 0));
        imagePanel.setMaximumSize(new java.awt.Dimension(300, 400));
        imagePanel.setMinimumSize(new java.awt.Dimension(300, 400));
        imagePanel.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel6.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource Folder/MotorPH login.jpg"))); // NOI18N
        jLabel6.setText("Image Here");

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(3, Short.MAX_VALUE))
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        getContentPane().add(imagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 380, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
    
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Both username and password are required!",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Verify credentials and get user details
            EmployeeDetails loggedInUser = roleAuthenticator.authenticateAndGetUserDetails(username, password);

            if (loggedInUser != null) {
                // Save user in the session
                UserSession.getInstance().setLoggedInUser(loggedInUser);
                UserSession.getInstance().setUsername(username);

                // Redirect to appropriate dashboard based on role
                String designation = loggedInUser.getDesignation();

                if (designation == null) {
                    JOptionPane.showMessageDialog(this,
                        "User designation not found",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (designation.contains("Rank and File")) {
                    new EmployeeDashboard(
                        loggedInUser.getFirstName(),
                        loggedInUser.getLastName(),
                        loggedInUser.getDesignation(),
                        loggedInUser.getEid(),
                        loggedInUser.getBasicSalary(),
                        loggedInUser.getTotalAllowances(),
                        loggedInUser.getTotalIncentives(), 
                        loggedInUser.getTotalDeductions(),
                        loggedInUser.getNetPay(),
                        username,
                        connection
                    ).setVisible(true);
                } else {
                    new AdminDashboard(
                        username,
                        connection
                    ).setVisible(true);
                }

                this.dispose(); // Close login form
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void forgotPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotPasswordButtonActionPerformed
        String username = usernameField.getText().trim();
    if (!username.isEmpty()) {
        new ForgotPassword(username).setVisible(true);
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this,
            "Please enter a username first",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_forgotPasswordButtonActionPerformed

    private void usernameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFieldFocusLost
        verifyUsername();
    }//GEN-LAST:event_usernameFieldFocusLost

    private void usernameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldFocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
        @Override    
        public void run() {
                new LoginPage().setVisible(true);
                }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton forgotPasswordButton;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
