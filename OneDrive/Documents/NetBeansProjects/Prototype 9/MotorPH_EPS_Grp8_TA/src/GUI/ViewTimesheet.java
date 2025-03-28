/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

/**
 *
 * @author ralph
 */

import DatabaseConnector.DatabaseConnection;
import BusinessLogic.DashboardData;
import BusinessLogic.EmployeeDetails;
import Controller.DBQueries;
import Controller.RoleAuthenticator;
import Controller.TimesheetController;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewTimesheet extends javax.swing.JFrame {
    // Core Components
    private RoleAuthenticator roleAuthenticator;
    private Connection connection;
    private String loggedInUsername;

    // Business Logic Components
    private EmployeeDetails employeeDetails;
    private DashboardData dashboardData;

    // Controller Components
    private TimesheetController timesheetController;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor that accepts username to load user-specific data
     * @param username The username of the logged-in user
     * @param connection Database connection
     */
    public ViewTimesheet(String username, Connection connection) {
        initComponents();
        
        // Stores the username
        this.loggedInUsername = username;
        
        try {
            // Gets connections from DB (singleton)
            this.connection = DatabaseConnection.getInstance().getConnection();
            
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error connecting to database: " + ex.getMessage(), 
            "Connection Error", 
            JOptionPane.ERROR_MESSAGE);
    }
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Initially save button is disabled to avoid conflicts
        saveButton.setEnabled(false);
        
        // Initialize TimesheetController and tableModel
        timesheetController = new TimesheetController();
        tableModel = (DefaultTableModel) timesheetTable.getModel();
        
        // Set column names
        String[] columnNames = {
            "AttendanceID", 
            "EID", 
            "LogDate", 
            "LogTime", 
            "Status"
        };
        tableModel.setColumnIdentifiers(columnNames);
        
        // Store the database connection and username
        this.loggedInUsername = username;
        
        try {
            // Gets connections from DB (singleton)
            this.connection = connection;
            if (this.connection == null || this.connection.isClosed()) 
            {
                this.connection = DatabaseConnection.getInstance().getConnection();
            }
            
            // Load employee data and initialize timesheet
            initializeTimesheet(username);
            
            this.employeeDetails = new EmployeeDetails(username, this.connection);
            
            // Display employee name and designation with EID
            updateUI();            
                        
            // Load timesheet data into the table
            timesheetController.loadTimesheetData(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database connection error: "+ ex.getMessage(),
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Constructor that accepts user data
    public ViewTimesheet(
        String firstName,
        String lastName,
        String designation,
        int eid,
        String username) 
    {
        initComponents();

        // Initialize TimesheetController and tableModel
        this.loggedInUsername = username;
        timesheetController = new TimesheetController();
        tableModel = (DefaultTableModel) timesheetTable.getModel();
        
        // Update UI with user data
        String fullName = firstName + " " + lastName;
        String designationWithEid = designation + " -- " + eid;
        this.fullName.setText(fullName);
        this.designationWithEid.setText(designationWithEid);
                
        // Load timesheet data into the table
        try {
            timesheetController.loadTimesheetData(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading timesheet data: "+ ex.getMessage(),
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Constructor for simpler initialization
    public ViewTimesheet(String firstName, String lastName, String designation, int eid) {
        initComponents();
        
        // Initialize TimesheetController and tableModel
        timesheetController = new TimesheetController();
        tableModel = (DefaultTableModel) timesheetTable.getModel();
        
        // Update UI with user data
        String fullName = firstName + " " + lastName;
        String designationWithEid = designation + " -- " + eid;
        this.fullName.setText(fullName);
        this.designationWithEid.setText(designationWithEid);
             
        // Load timesheet data into the table
        try {
            timesheetController.loadTimesheetData(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading timesheet data: "+ ex.getMessage(),
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public ViewTimesheet() {
        initComponents();
        
        // Initialize TimesheetController and tableModel
        timesheetController = new TimesheetController();
        tableModel = (DefaultTableModel) timesheetTable.getModel();
        
        try {
            // Load timesheet data into the table
            timesheetController.loadTimesheetData(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading timesheet data: "+ ex.getMessage(),
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeTimesheet(String username) {
    try {
        // Get the default table model from your JTable
        DefaultTableModel tableModel = (DefaultTableModel) timesheetTable.getModel();
        System.out.println("Table model has " + tableModel.getColumnCount() + " columns");
        
        // Check if timesheetController is initialized
        if (timesheetController == null) {
            System.out.println("ERROR: timesheetController is null");
            timesheetController = new TimesheetController(); // Initialize if needed
        }
        
        // Load timesheet data directly using the controller
        boolean success = timesheetController.loadTimesheetData(tableModel);
        
        System.out.println("After loading, table model has " + tableModel.getRowCount() + " rows");
        
        if (success) {
            System.out.println("Timesheet data loaded successfully");
        } else {
            System.out.println("Failed to load timesheet data");
            JOptionPane.showMessageDialog(this, 
                "No timesheet data found", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            }
    }
    catch (SQLException ex) {
        System.out.println("SQL Exception: " + ex.getMessage());
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error initializing timesheet view: " + ex.getMessage(), 
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    // Update the interface with specific details
    private void updateUI() {
        if(employeeDetails != null) {
        fullName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
        designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());
        } else {
            fullName.setText("No employee selected.");
            designationWithEid.setText("--");
        }
    }
    
    private void setupTable() {
        // Setup the table for timesheet data
        JScrollPane scrollPane = new JScrollPane(timesheetTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Add timesheet-specific actions
    private void addTimeLog() {
        try {
            // Get data from input fields or dialog
            String attendanceID = ""; // Generate or get from input
            String eid = String.valueOf(employeeDetails.getEid());
            String logDate = ""; // Get from date picker
            String logTime = ""; // Get from time picker
            String status = ""; // Get from dropdown or input
            
            Object[] timeLog = {attendanceID, eid, logDate, logTime, status};
            
            // Add the time log
            boolean success = timesheetController.addTimeLog(timeLog);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Time log added successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the table
                timesheetController.loadTimesheetData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to add time log", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTimeLog(String attendanceID) {
        try {
            // Get data from selected row
            int selectedRow = timesheetTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a row to update", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get values from the table
            String eid = String.valueOf(tableModel.getValueAt(selectedRow, 1));
            String logDate = String.valueOf(tableModel.getValueAt(selectedRow, 2));
            String logTime = String.valueOf(tableModel.getValueAt(selectedRow, 3));
            String status = String.valueOf(tableModel.getValueAt(selectedRow, 4));
            
            Object[] timeLog = {attendanceID, eid, logDate, logTime, status};
            
            // Update the time log
            boolean success = timesheetController.updateTimeLog(timeLog);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Time log updated successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the table
                timesheetController.loadTimesheetData(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to update time log", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteTimeLog() {
        try {
            // Get selected row
            int selectedRow = timesheetTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a row to delete", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get attendance ID
            String attendanceID = String.valueOf(tableModel.getValueAt(selectedRow, 0));
            
            // Confirm delete
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this time log?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete the time log
                boolean success = timesheetController.deletetimeLog(attendanceID);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Time log deleted successfully", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload the table
                    timesheetController.loadTimesheetData(tableModel);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete time log", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addNewEmptyRow() {
        timesheetController.addEmptyRowToTable(tableModel);
    }
    
    private void navigateBasedOnRole(String username, Connection connection) {
     try {
         if (username == null || username.isEmpty()) {
             JOptionPane.showMessageDialog(this,
                 "User session not found. Please log in again.",
                 "Session Error",
                 JOptionPane.ERROR_MESSAGE);
             return;
         }

         // Initialize roleAuthenticator if it's null
         if (this.roleAuthenticator == null) {
             this.roleAuthenticator = new RoleAuthenticator();
         }

         // Instead of trying to authenticate again, just get the user details directly
         String designation = getUserDesignationFromDB(username, connection);

         if (designation == null) {
             JOptionPane.showMessageDialog(this,
                 "User designation not found",
                 "Error",
                 JOptionPane.ERROR_MESSAGE);
             return;
         }

         if (designation.contains("Rank and File")) {
             new EmployeeDashboard(username, connection).setVisible(true);
         } else {
             new AdminDashboard(username, connection).setVisible(true);
         }

         this.dispose();
     } catch (HeadlessException | SQLException ex) {
         Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
         JOptionPane.showMessageDialog(this,
             "Navigation error: " + ex.getMessage(),
             "Error",
             JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to get user designation directly from the database
        private String getUserDesignationFromDB(String username, Connection connection) {
        String designation = null;
        try {
            DBQueries queries = new DBQueries();
            PreparedStatement pstmt = connection.prepareStatement(queries.getUserDesignationByUsername);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return designation;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboardLabel = new javax.swing.JLabel();
        payrollLabel = new javax.swing.JLabel();
        viewSettingsButton = new javax.swing.JLabel();
        dashboardPanel = new javax.swing.JPanel();
        dashboardPane = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        viewProfileButton = new javax.swing.JButton();
        fullName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        viewSettingsBtn = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        timesheetTable = new javax.swing.JTable();
        updateButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        sortButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dashboardLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(51, 51, 51));
        dashboardLabel.setText("Dashboard");
        dashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseClicked(evt);
            }
        });

        payrollLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        payrollLabel.setForeground(new java.awt.Color(51, 51, 51));
        payrollLabel.setText("Payroll");
        payrollLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payrollLabelMouseClicked(evt);
            }
        });

        viewSettingsButton.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        viewSettingsButton.setForeground(new java.awt.Color(51, 51, 51));
        viewSettingsButton.setText("Settings");
        viewSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewSettingsButtonMouseClicked(evt);
            }
        });

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Timesheet");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        viewProfileButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewProfileButton.setText("View Profile");
        viewProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewProfileButtonActionPerformed(evt);
            }
        });

        fullName.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        fullName.setForeground(new java.awt.Color(255, 255, 255));
        fullName.setText("fullName");

        designationWithEid.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        designationWithEid.setForeground(new java.awt.Color(255, 255, 255));
        designationWithEid.setText("designationWithEid");

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource Folder/blankephoto.png"))); // NOI18N

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullName)
                    .addComponent(designationWithEid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 497, Short.MAX_VALUE)
                .addComponent(viewProfileButton)
                .addGap(81, 81, 81))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 3, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(viewProfileButton)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(fullName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationWithEid)
                        .addGap(28, 28, 28))))
        );

        jLabel5.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 35, 102));
        jLabel5.setText("Quick Actions");

        viewSettingsBtn.setBackground(new java.awt.Color(0, 35, 102));
        viewSettingsBtn.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewSettingsBtn.setForeground(new java.awt.Color(255, 255, 255));
        viewSettingsBtn.setText("Settings");
        viewSettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSettingsBtnActionPerformed(evt);
            }
        });

        addButton.setBackground(new java.awt.Color(0, 35, 102));
        addButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        timesheetTable.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        timesheetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "AttendanceID", "EID", "LogDate", "LogTime", "Status"
            }
        ));
        timesheetTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(timesheetTable);

        updateButton.setBackground(new java.awt.Color(0, 35, 102));
        updateButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(0, 35, 102));
        searchButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(0, 35, 102));
        deleteButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        sortButton.setBackground(new java.awt.Color(0, 35, 102));
        sortButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sortButton.setForeground(new java.awt.Color(255, 255, 255));
        sortButton.setText("Sort");
        sortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortButtonActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));
        jPanel10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel10.setToolTipText("");

        jLabel55.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Office Hours: 8:30am–5:30pm, Monday through Saturday");

        jLabel56.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Office Address: 7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City");

        jLabel57.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073 ");

        jLabel58.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Email: corporate@motorph.com");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(310, 310, 310)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel55))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel57)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addComponent(jLabel58)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshButton.setBackground(new java.awt.Color(0, 35, 102));
        refreshButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        saveButton.setBackground(new java.awt.Color(0, 35, 102));
        saveButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(dashboardPane)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(addButton)
                                .addGap(18, 18, 18)
                                .addComponent(updateButton)
                                .addGap(18, 18, 18)
                                .addComponent(deleteButton)
                                .addGap(18, 18, 18)
                                .addComponent(searchButton)
                                .addGap(18, 18, 18)
                                .addComponent(sortButton)
                                .addGap(18, 18, 18)
                                .addComponent(refreshButton)
                                .addGap(233, 233, 233)
                                .addComponent(saveButton)
                                .addGap(18, 18, 18)
                                .addComponent(viewSettingsBtn)))
                        .addGap(0, 25, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(dashboardPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewSettingsBtn)
                    .addComponent(addButton)
                    .addComponent(updateButton)
                    .addComponent(searchButton)
                    .addComponent(deleteButton)
                    .addComponent(sortButton)
                    .addComponent(refreshButton)
                    .addComponent(saveButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource Folder/MotorPHLogo small.png"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(37, 61, 144));
        jLabel17.setText("----");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(dashboardLabel)
                        .addGap(75, 75, 75)
                        .addComponent(payrollLabel)
                        .addGap(68, 68, 68)
                        .addComponent(viewSettingsButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(336, 336, 336))))
            .addComponent(dashboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dashboardLabel)
                            .addComponent(payrollLabel)
                            .addComponent(viewSettingsButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseClicked
        navigateBasedOnRole(this.loggedInUsername, this.connection);
    }//GEN-LAST:event_dashboardLabelMouseClicked

    private void payrollLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseClicked
        try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "User session not found. Please log in again.",
                    "Session Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Opens PayrollDashboard and pass the username and connection
            new PayrollDashboard(this.loggedInUsername, this.connection).setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening PayrollDashboard: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);}
    }//GEN-LAST:event_payrollLabelMouseClicked

    private void viewSettingsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsButtonMouseClicked
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens ViewSettings and passes the username and connection
        new ViewSettings (this.loggedInUsername, this.connection).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
            Logger.getLogger(ViewTimesheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewSettingsButtonMouseClicked

    private void viewProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewProfileButtonActionPerformed
        try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "User session not found. Please log in again.",
                    "Session Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Opens EmployeeDashboard and passes the username and connection
            new ViewProfile (this.loggedInUsername, this.connection).setVisible(true);
            this.dispose();
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening EditProfile: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);}
    }//GEN-LAST:event_viewProfileButtonActionPerformed

    private void viewSettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSettingsBtnActionPerformed
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens ViewSettings and passes the username and connection
        new ViewSettings (this.loggedInUsername, this.connection).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
            Logger.getLogger(ViewTimesheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewSettingsBtnActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addNewEmptyRow();
        timesheetTable.scrollRectToVisible(timesheetTable.getCellRect(timesheetTable.getRowCount() - 1, 0, true));
        timesheetTable.setRowSelectionInterval(timesheetTable.getRowCount() - 1, timesheetTable.getRowCount() - 1);
        timesheetTable.editCellAt(timesheetTable.getRowCount() - 1, 0);
        saveButton.setEnabled(true); // Enable the saveButton when an input was made
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int selectedRow = timesheetTable.getSelectedRow();
        if (selectedRow == -1) {
            timesheetController.showMessage("Please select an employee to update.", "Selection Required", false);
            return;
        }
        String eid = tableModel.getValueAt(selectedRow, 0).toString();
        timesheetController.showUpdateDialog(this, eid, tableModel);
        saveButton.setEnabled(true); // Enable the saveButton when an update was made
    }//GEN-LAST:event_updateButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        timesheetController.showSearchDialog(this, tableModel);
    }//GEN-LAST:event_searchButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int selectedRow = timesheetController.getSelectedRow();
        if (selectedRow == -1) {
            timesheetController.showMessage("Please select an employee to delete.", "Selection Required", false);
            return;
        }

        String eid = tableModel.getValueAt(selectedRow, 0).toString();
        timesheetController.deleteEmployeeWithConfirmation(eid, tableModel);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void sortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortButtonActionPerformed
        timesheetController.showSortDialog(this, tableModel);
    }//GEN-LAST:event_sortButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        try {
            timesheetController.loadTimesheetData(tableModel);
        } catch (SQLException ex) {
            Logger.getLogger(ViewTimesheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            timesheetController.saveNewTimeLogFromTable(tableModel);
            timesheetController.loadTimesheetData(tableModel);
            saveButton.setEnabled(false); // Disable the saveButton after saving
        } catch (SQLException ex) {
            Logger.getLogger(ViewTimesheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ViewTimesheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewTimesheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewTimesheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewTimesheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewTimesheet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton sortButton;
    private javax.swing.JTable timesheetTable;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton viewProfileButton;
    private javax.swing.JButton viewSettingsBtn;
    private javax.swing.JLabel viewSettingsButton;
    // End of variables declaration//GEN-END:variables
}
