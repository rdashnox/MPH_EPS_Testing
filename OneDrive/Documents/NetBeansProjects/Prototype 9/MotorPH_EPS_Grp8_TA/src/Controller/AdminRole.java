package Controller;

/**
 *
 * @author dashcodes
 */

import DatabaseConnector.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.*;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class AdminRole {
    private final DatabaseConnection databaseConnection;
    private final DBQueries dbQueries;
    
    /**
     * Constructor initializes database connection and queries
     */
    public AdminRole() {
        databaseConnection = new DatabaseConnection();
        dbQueries = new DBQueries();
    }
    
    /**
     * Loads employee data from database into the table model
     * @param tableModel The DefaultTableModel to populate with employee data
     * @return boolean indicating success or failure
     */
    public boolean loadEmployeeData(DefaultTableModel tableModel) {
        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(dbQueries.getAdminAccess())) {
            
            // Clear existing rows
            tableModel.setRowCount(0);
        
            // Populate the table model with data
        while (rs.next()) {
            Object[] row = new Object[12]; 
            row[0] = rs.getString("EID");
            String lastName = rs.getString("Last_Name");
            String firstName = rs.getString("First_Name");
            row[1] = lastName + ", " + firstName;
            row[2] = rs.getString("Birthday");
            row[3] = rs.getString("Address");
            row[4] = rs.getString("Phone_Number");
            row[5] = rs.getString("SSS_#");
            row[6] = rs.getString("PhilHealth_#");
            row[7] = rs.getString("TIN_#");
            row[8] = rs.getString("Pag-ibig_#");
            row[9] = rs.getString("Status");
            row[10] = rs.getString("Designation");
            row[11] = rs.getString("Basic_Salary");
            tableModel.addRow(row);
        }
        return true;
    } catch (SQLException e) {
        return false;
        }
    }
    
        public boolean addEmployee(Object[] employee) {
            String sql = "INSERT INTO payrollmph "
            + "(EID, "
            + "Last_Name, "
            + "First_Name, "
            + "Birthday, "
            + "Address, "
            + "Phone_Number, "
            + "`SSS_#`, "
            + "`PhilHealth_#`, "
            + "`TIN_#`, "
            + "`Pag-ibig_#`, "
            + "Status, "
            + "Designation, "
            + "Basic_Salary) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Extract data from the employee array with proper error handling
        stmt.setString(1, employee[0].toString());
        stmt.setString(2, employee[1].toString());
        stmt.setString(3, employee[2].toString());
        stmt.setString(4, employee[3].toString());
        stmt.setString(5, employee[4].toString());
        stmt.setString(6, employee[5].toString());
        stmt.setString(7, employee[6].toString());
        stmt.setString(8, employee[7].toString());
        stmt.setString(9, employee[8].toString());
        stmt.setString(10, employee[9].toString());
        stmt.setString(11, employee[10].toString());
        stmt.setString(12, employee[11].toString());
        
        // Handle salary properly
        double salary;
        if (employee[12] instanceof Double) {
            salary = (Double) employee[12];
        } else {
            try {
                salary = Double.parseDouble(employee[12].toString());
            } catch (NumberFormatException e) {
                throw new SQLException("Invalid salary format");
            }
        }
        stmt.setDouble(13, salary);
        
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
        
    } catch (SQLException e) {
        System.err.println("Error adding employee: " + e.getMessage());
        return false;
    }
}
    
    /**
    * Updates an existing employee in the database
    * @param employee An array of updated employee data
     * @return boolean indicating success or failure
    */
    public boolean updateEmployee(Object[] employee) {
        try (Connection conn = databaseConnection.getConnection()) {
            // Prepare the SQL query
            String sql = "UPDATE payrollmph SET " +
                    "Last_Name = ?, " +
                    "First_Name = ?, " +
                    "Birthday = ?, " +
                    "Address = ?, " +
                    "Phone_Number = ?, " +
                    "Status = ?, " +
                    "Designation = ?, " +
                    "Basic_Salary = ? " +
                    "WHERE EID = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Parse the full name into first and last name (if it's combined)
                String lastName, firstName;
                String fullName = employee[2].toString();

                if (fullName.contains(",")) {
                    String[] nameParts = fullName.split(",");
                    lastName = nameParts[0].trim();
                    firstName = nameParts.length > 1 ? nameParts[1].trim() : "";
                } else {
                    // If they're passed separately
                    lastName = employee[2].toString();
                    firstName = employee.length > 3 ? employee[3].toString() : "";
                }

                // Set parameters in the correct order
                stmt.setString(1, lastName);
                stmt.setString(2, firstName);
                stmt.setString(3, employee[1].toString()); // Birthday
                stmt.setString(4, employee[4].toString()); // Address
                stmt.setString(5, employee[5].toString()); // Phone number
                stmt.setString(6, employee[6].toString()); // Status
                stmt.setString(7, employee[3].toString()); // Designation

                // Handle the salary value
                double salary;
                if (employee[7] instanceof Double) {
                    salary = (Double) employee[7];
                } else {
                    try {
                        salary = Double.parseDouble(employee[7].toString());
                    } catch (NumberFormatException e) {
                        throw new SQLException("Invalid salary format");
                    }
                }
                stmt.setDouble(8, salary);

                // Set the EID for the WHERE clause
                stmt.setString(9, employee[0].toString());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes an employee from the database
     * @param eid Employee ID to delete
     * @return boolean indicating success or failure
     */
    public boolean deleteEmployee(String eid) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getDeleteEmployee())) {
            
            stmt.setString(1, eid);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Checks if an employee ID already exists
     * @param eid Employee ID to check
     * @return boolean indicating if the EID exists
     */
    public boolean isEidExists(String eid) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getSearchByEID())) {
            
            stmt.setString(1, eid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking EID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Retrieves employee data for a specific EID
     * @param eid Employee ID to retrieve
     * @return Object array containing employee data or null if not found
     */
    public Object[] getEmployeeById(String eid) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getGetEmployeeByEid())) {

            stmt.setString(1, eid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Object[] employee = new Object[12];

                employee[0] = rs.getString("EID");

                // Format name properly
                String lastName = rs.getString("Last_Name");
                String firstName = rs.getString("First_Name");
                employee[1] = (lastName != null ? lastName : "") + ", " + (firstName != null ? firstName : "");

                employee[2] = rs.getString("Birthday");
                employee[3] = rs.getString("Address");
                employee[4] = rs.getString("Phone_Number");
                employee[5] = rs.getString("SSS_#");
                employee[6] = rs.getString("PhilHealth_#");
                employee[7] = rs.getString("TIN_#");
                employee[8] = rs.getString("Pag-ibig_#");
                employee[9] = rs.getString("Status");
                employee[10] = rs.getString("Designation");

                // Handle salary properly
                try {
                    employee[11] = rs.getDouble("Basic_Salary");
                } catch (SQLException ex) {
                    String salaryStr = rs.getString("Basic_Salary");
                    if (salaryStr != null && !salaryStr.isEmpty()) {
                        try {
                            employee[11] = Double.parseDouble(salaryStr.replaceAll("[^0-9.]", ""));
                        } catch (NumberFormatException e) {
                            employee[11] = 0.0;
                        }
                    } else {
                        employee[11] = 0.0;
                    }
                }

                return employee;
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * Searches for employees based on a specific column and search term
     * @param columnName Column to search in
     * @param searchTerm Term to search for
     * @param tableModel The DefaultTableModel to populate with search results
     * @return boolean indicating success or failure
     */
    public boolean searchEmployees(String columnName, String searchTerm, DefaultTableModel tableModel) {
        String query = dbQueries.createSearchQuery(columnName);

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();

            // Clear existing rows
            tableModel.setRowCount(0);

            boolean resultsFound = false;

            // Add data from ResultSet to the table model
            while (rs.next()) {
                resultsFound = true;
                Vector<Object> row = new Vector<>();
                row.add(rs.getString(dbQueries.getEid()));

                // Combine lastName and firstName
                String lastName = rs.getString(dbQueries.getLastName());
                String firstName = rs.getString(dbQueries.getFirstName());
                row.add(lastName + ", " + firstName);
                row.add(rs.getString(dbQueries.getBirthDay()));
                row.add(rs.getString(dbQueries.getAddress()));
                row.add(rs.getString(dbQueries.getPhoneNumber()));
                row.add(rs.getString(dbQueries.getSSS()));
                row.add(rs.getString(dbQueries.getPhilHealth()));
                row.add(rs.getString(dbQueries.getTin()));
                row.add(rs.getString(dbQueries.getPagIbig()));
                row.add(rs.getString(dbQueries.getStatus()));
                row.add(rs.getString(dbQueries.getDesignation()));

                try {
                    double salary = rs.getDouble(dbQueries.getBasicSalary());
                    row.add(salary);
                } catch (SQLException ex) {
                    String salaryStr = rs.getString(dbQueries.getBasicSalary());
                    if (salaryStr != null && !salaryStr.trim().isEmpty()) {
                        salaryStr = salaryStr.replaceAll("[^0-9.]", "");
                        try {
                            double salary = Double.parseDouble(salaryStr);
                            row.add(salary);
                        } catch (NumberFormatException e) {
                            row.add(0.0);
                            System.err.println("Could not parse salary: " + salaryStr);
                        }
                    } else {
                        row.add(0.0); // Default value if null or empty
                    }
                }
                tableModel.addRow(row);
            }

            // Check if any results were found
            if (!resultsFound) {
                return false; // No matching records found
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Sorts the employee data based on a specific column
     * @param columnName Column to sort by
     * @param ascending If true, sort in ascending order; otherwise descending
     * @param tableModel The DefaultTableModel to populate with sorted data
     * @return boolean indicating success or failure
     */
    public boolean sortEmployees(String columnName, boolean ascending, DefaultTableModel tableModel) {
    String query = dbQueries.createSortQuery(columnName, ascending);
    
    try (Connection conn = databaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Add data from ResultSet to the table model
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString(dbQueries.getEid()));
            
            // Combine lastName and firstName
            String lastName = rs.getString(dbQueries.getLastName());
            String firstName = rs.getString(dbQueries.getFirstName());
            row.add(lastName + ", " + firstName);
            
            row.add(rs.getString(dbQueries.getBirthDay()));
            row.add(rs.getString(dbQueries.getAddress()));
            row.add(rs.getString(dbQueries.getPhoneNumber()));
            row.add(rs.getString(dbQueries.getSSS()));
            row.add(rs.getString(dbQueries.getPhilHealth()));
            row.add(rs.getString(dbQueries.getTin()));
            row.add(rs.getString(dbQueries.getPagIbig()));
            row.add(rs.getString(dbQueries.getStatus()));
            row.add(rs.getString(dbQueries.getDesignation()));
            
            // Fix for numeric value with commas - get as string and handle conversion
            try {
                // First try getting as double directly
                double salary = rs.getDouble("Basic_Salary");
                row.add(salary);
            } catch (SQLException ex) {
                // If that fails, get as string and clean it
                String salaryStr = rs.getString("Basic_Salary");
                if (salaryStr != null) {
                    // Remove commas and convert to double
                    salaryStr = salaryStr.replace(",", "");
                    try {
                        double salary = Double.parseDouble(salaryStr);
                        row.add(salary);
                    } catch (NumberFormatException e) {
                        // If all else fails, just add the string value
                        row.add(salaryStr);
                    }
                } else {
                    row.add(0.0); // Default value if null
                }
            }
            
            tableModel.addRow(row);
        }
        
        return true;
    } catch (SQLException e) {
        System.err.println("Error sorting employees: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    /**
     * Gets the column names for the employee table
     * @return Array of column names
     */
    public String[] getColumnNames() {
        return new String[]
        {"EID",
        "Last_Name",
        "First_Name",
        "Birthday",
        "Address", 
        "Phone_Number",
        "SSS #",
        "PhilHealth #",
        "TIN #",
        "Pag-ibig #", 
        "Status",
        "Designation",
        "Basic_Salary"};
    }
    
    /**
     * Validate employee data before adding/updating
     * @param employee
     * @return
     */
    
    public String validateEmployeeData(Object[] employee) {
    if (employee.length < 3) {
        return "Invalid employee data format.";
    }

    // Check for required fields
    if (employee[0] == null || employee[0].toString().isEmpty()) {
        return "EID is required.";
    }
    
    // Check name field(s)
    if (employee.length >= 3) {
        if (employee[1] == null || employee[1].toString().isEmpty()) {
            if (employee[2] == null || employee[2].toString().isEmpty()) {
                return "Name is required.";
            }
        }
    }
    
    // Validate salary if present
    if (employee.length >= 13) {
        if (employee[12] != null) {
            try {
                if (!(employee[12] instanceof Double)) {
                    Double.parseDouble(employee[12].toString());
                }
            } catch (NumberFormatException e) {
                return "Salary must be a valid number.";
            }
        }
    }
    
    return null; // No validation errors
}
    
    /**
    * Sets up a table with the last row being editable for new employee entry
    * @param table The JTable to set up
    * @param tableModel The table model to use
    */
   public void setupEditableTable(JTable table, DefaultTableModel tableModel) {
       // Make only the last row editable
       table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
           @Override
           public boolean isCellEditable(EventObject e) {
               int row = table.getSelectedRow();
               return row == table.getRowCount() - 1;
           }
       });

       // Set up table model with proper column types
       tableModel.setColumnIdentifiers(new String[] {
           "EID", "Full Name", "Birthday", "Address", "Phone Number",
           "SSS #", "PhilHealth #", "TIN #", "Pag-Ibig #", "Status", 
           "Designation", "Basic Salary"
       });

       // Set the salary column to display as Double
       table.getColumnModel().getColumn(11).setCellRenderer(new DefaultTableCellRenderer() {
           @Override
           public Component getTableCellRendererComponent(JTable table, Object value, 
                   boolean isSelected, boolean hasFocus, int row, int column) {
               if (value instanceof Double) {
                   setText(String.format("%.2f", (Double)value));
               } else if (value != null) {
                   setText(value.toString());
               } else {
                   setText("");
               }
               return this;
           }
       });
   }

   /**
    * Adds an empty row to the table model
    * @param tableModel The table model to add the row to
    */
   public void addEmptyRowToTable(DefaultTableModel tableModel) {
       Object[] emptyRow = new Object[12];
       tableModel.addRow(emptyRow);
   }
    
    public void showAddDialog(JFrame parent, DefaultTableModel tableModel) {
        JDialog addDialog = new JDialog(parent, "Add New Employee", true);
        addDialog.setSize(new Dimension(600, 500));
        addDialog.setLocationRelativeTo(parent);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Create form fields with auto-generated EID
        JTextField eidField = new JTextField(generateNextEID(), 10);

        // Make it read-only since it's auto-generated
        eidField.setEditable(false);
        eidField.setFocusable(false);

        JTextField lastNameField = new JTextField(20);
        JTextField firstNameField = new JTextField(20);
        JTextField birthDayField = new JTextField(20);
        birthDayField.setDocument(new DateDocument());
        setPlaceholder(birthDayField, "YYYY-MM-DD");

        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(15);
        phoneField.setDocument(new PhoneNumberDocument());
        setPlaceholder(phoneField, "XXX-XXX-XXX");

        JTextField sssField = new JTextField(15);
        sssField.setDocument(new GovernmentIDDocument(10));
        setPlaceholder(sssField, "XX-XXXXXXX-X");

        JTextField philHealthField = new JTextField(15);
        philHealthField.setDocument(new GovernmentIDDocument(12));
        setPlaceholder(philHealthField, "XXXXXXXXXXXX");

        JTextField tinField = new JTextField(15);
        tinField.setDocument(new GovernmentIDDocument(9));
        setPlaceholder(tinField, "XXX-XXX-XXX-XXX");

        JTextField pagIbigField = new JTextField(15);
        pagIbigField.setDocument(new GovernmentIDDocument(12));
        setPlaceholder(pagIbigField, "XXXXXXXXXXX");

        String[] statusOptions = {"Probationary", "Regular"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);

        JComboBox<String> designationCombo = new JComboBox<>(designationOptions);
        JTextField salaryField = new JTextField(15);
        salaryField.setDocument(new SalaryDocument());
        addSalaryFormatting(salaryField);

        // Add all fields to form panel with labels using the helper method
        addFormField(formPanel, "EID:", eidField);
        addFormField(formPanel, "Last Name:", lastNameField);
        addFormField(formPanel, "First Name:", firstNameField);
        addFormField(formPanel, "Birthday (YYYY-MM-DD):", birthDayField);
        addFormField(formPanel, "Address:", addressField);
        addFormField(formPanel, "Phone Number:", phoneField);
        addFormField(formPanel, "SSS #:", sssField);
        addFormField(formPanel, "PhilHealth #:", philHealthField);
        addFormField(formPanel, "TIN #:", tinField);
        addFormField(formPanel, "Pag-ibig #:", pagIbigField);
        addFormField(formPanel, "Status:", statusCombo);
        addFormField(formPanel, "Designation:", designationCombo);
        addFormField(formPanel, "Basic Salary:", salaryField);

        // Add buttons
        JButton addButton = new JButton("Add Employee");
        JButton cancelButton = new JButton("Cancel");

        // Add button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Add form and button panels to dialog
        addDialog.setLayout(new BorderLayout());
        addDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add button action
        addButton.addActionListener(e -> {
            try {

                formatSalaryField(salaryField);
                // Create the employee data array with the structure expected by addEmployee
                Object[] employee = new Object[13];
                employee[0] = eidField.getText().trim();
                employee[1] = lastNameField.getText().trim();
                employee[2] = firstNameField.getText().trim();
                employee[3] = birthDayField.getText().trim();
                employee[4] = addressField.getText().trim();
                employee[5] = phoneField.getText().trim();
                employee[6] = sssField.getText().trim();
                employee[7] = philHealthField.getText().trim();
                employee[8] = tinField.getText().trim();
                employee[9] = pagIbigField.getText().trim();
                employee[10] = statusCombo.getSelectedItem().toString();
                employee[11] = designationCombo.getSelectedItem().toString();

                // Validate salary
                       double rate;
            try {
                rate = Double.parseDouble(salaryField.getText().trim());
                employee[12] = rate;
            } catch (NumberFormatException ex) {
                showMessage("Invalid salary format.", "Error", true);
                return;
            }

                // Validate employee data
                String validationError = validateEmployeeData(employee);
                if (validationError != null) {
                    showMessage(validationError, "Validation Error", true);
                    return;
                }

                // Check if EID already exists
                if (isEidExists(eidField.getText().trim())) {
                    showMessage("Employee ID already exists. Please use a different ID.", "Error", true);
                    return;
                }

                // Add employee to database
                if (addEmployee(employee)) {
                    showMessage("Employee added successfully!", "Success", false);
                    loadEmployeeData(tableModel);
                    addDialog.dispose();
                } else {
                    showMessage("Failed to add employee.", "Error", true);
                }
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage(), "Error", true);
                ex.printStackTrace();
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> addDialog.dispose());

        // Show dialog
        addDialog.getContentPane().add(formPanel);
        addDialog.setVisible(true);
    }

   /**
    * Shows a dialog for updating an employee
    * @param parent The parent frame
    * @param eid The employee ID to update
    * @param tableModel The table model to refresh after update
    */
   public void showUpdateDialog(JFrame parent, String eid, DefaultTableModel tableModel) {
        Object[] employee = getEmployeeById(eid);

        if (employee == null) {
            showMessage("Could not retrieve employee data.", "Error", true);
            return;
        }

        JDialog updateDialog = new JDialog(parent, "Update Employee", true);
        updateDialog.setSize(new Dimension(600, 500));
        updateDialog.setLocationRelativeTo(parent);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Extract full name parts - assuming format "LastName, FirstName"
        String fullName = employee[1].toString();
        String lastName = "";
        String firstName = "";
        if (fullName.contains(",")) {
            String[] nameParts = fullName.split(",");
            lastName = nameParts[0].trim();
            firstName = nameParts.length > 1 ? nameParts[1].trim() : "";
        }

        // Add fields with pre-filled values
        JTextField eidField = new JTextField(employee[0].toString(), 10);

        // Make it read-only since it's auto-generated
        eidField.setEditable(false);
        eidField.setFocusable(false);

        // For LastName field - allow letters and hyphens
        JTextField lastNameField = new JTextField(lastName, 20);
        lastNameField.setDocument(new RestrictedInput(
            true,   // allow letters
            false,  // no spaces
            true,   // allow hyphens
            false,  // no apostrophes
            false,  // no numbers
            40      // max length
        ));

        JTextField firstNameField = new JTextField(firstName, 20);
        firstNameField.setEditable(false);
        firstNameField.setFocusable(false);

        JTextField birthDayField = new JTextField(20);
        birthDayField.setEditable(false);
        birthDayField.setFocusable(false);
        // If birthday is empty or null, set a place value
        if (employee[2] == null || employee[2].toString().trim().isEmpty()) {
        birthDayField.setText("Not Available");
        } else {
        birthDayField.setText(employee[2].toString());
        }   

        JTextField addressField = new JTextField(employee[3].toString(), 20);

        JTextField phoneField = new JTextField(15);
        phoneField.setDocument(RestrictedInput.numbersOnly(10)); // 10 digits for phone number

        // Retrieve and format the phone number
        if (employee[4] == null || employee[4].toString().trim().isEmpty()) {
        setPlaceholder(phoneField, "XXX-XXX-XXX");
        } else {
        String rawPhone = employee[4].toString().replaceAll("[^0-9]", ""); // Remove non-digits
        try {
            // Clear existing content
            phoneField.getDocument().remove(0, phoneField.getDocument().getLength());
            // Simulate user input to trigger formatting
            for (char c : rawPhone.toCharArray()) {
                phoneField.getDocument().insertString(
                    phoneField.getDocument().getLength(), 
                    String.valueOf(c), 
                    null
                );
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            // Fallback: Set raw value if formatting fails
            phoneField.setText(rawPhone);
        }
    }

        String[] statusOptions = {"Probationary", "Regular"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setSelectedItem(employee[9].toString()); // Use correct index for status

        JComboBox<String> designationCombo = new JComboBox<>(designationOptions);
        designationCombo.setSelectedItem(employee[10].toString()); 

        // Format salary as a string
        JTextField salaryField = new JTextField();
        salaryField.setDocument(new SalaryDocument());
        addSalaryFormatting(salaryField);
        if (employee[11] instanceof Double) {
            salaryField.setText(String.format("%.2f", (Double)employee[11]));
        } else {
            salaryField.setText(employee[11].toString());
        }

        // Add all fields to form panel with labels
        addFormField(formPanel, "EID:", eidField);
        addFormField(formPanel, "Last Name:", lastNameField);
        addFormField(formPanel, "First Name:", firstNameField);
        addFormField(formPanel, "Birthday (YYYY-MM-DD):", birthDayField);
        addFormField(formPanel, "Address:", addressField);
        addFormField(formPanel, "Phone Number:", phoneField);
        addFormField(formPanel, "Status:", statusCombo);
        addFormField(formPanel, "Designation:", designationCombo);
        addFormField(formPanel, "Basic Salary:", salaryField);

        // Add buttons
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

        // Add buttons to a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        // Add form panel and button panel to dialog
        updateDialog.setLayout(new BorderLayout());
        updateDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        updateButton.addActionListener(e -> {
            try {
                // Create the employee data array with the correct structure
                Object[] updatedEmployee = new Object[8];
                updatedEmployee[0] = eidField.getText().trim(); // EID
                updatedEmployee[1] = birthDayField.getText().trim(); // Birthday
                updatedEmployee[2] = lastNameField.getText().trim() + ", " + firstNameField.getText().trim(); // Full name
                updatedEmployee[3] = designationCombo.getSelectedItem().toString();
                updatedEmployee[4] = addressField.getText().trim(); // Address
                updatedEmployee[5] = phoneField.getText().trim(); // Phone
                updatedEmployee[6] = statusCombo.getSelectedItem(); // Status

                // Parse salary
                double salary;
                try {
                    salary = Double.parseDouble(salaryField.getText().trim());
                    updatedEmployee[7] = salary;
                } catch (NumberFormatException ex) {
                    showMessage("Invalid salary format. Please enter a number.", "Error", true);
                    return;
                }

                if (updateEmployee(updatedEmployee)) {
                    showMessage("Employee updated successfully.", "Success", false);
                    loadEmployeeData(tableModel);
                    updateDialog.dispose();
                } else {
                    showMessage("Failed to update employee.", "Error", true);
                }
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage(), "Error", true);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> updateDialog.dispose());

        updateDialog.setVisible(true);
    }

    // Helper method to add form fields with consistent layout
    private void addFormField(JPanel panel, String label, Component field) {
        JPanel fieldPanel = new JPanel(new GridLayout(1, 2));
        fieldPanel.add(new JLabel(label));
        fieldPanel.add(field);
        panel.add(fieldPanel);
    }

        // Designation Options for the dropdown
        private final String[] designationOptions = {
        "IT Operations and Systems",
        "HR Manager",
        "HR Team Leader",
        "HR Rank and File",
        "Accounting Head",
        "Payroll Manager",
        "Payroll Team Leader",
        "Payroll Rank and File",
        "Account Manager",
        "Account Team Leader",
        "Account Rank and File",
        "Sales & Marketing",
        "Supply Chain and Logistics",
        "Customer Service and Relations"
    };


       /**
        * Deletes an employee after confirmation
        * @param eid The employee ID to delete
        * @param tableModel The table model to refresh after deletion
        */
       public void deleteEmployeeWithConfirmation(String eid, DefaultTableModel tableModel) {
           // Find employee name from table model or database
           String employeeName = getEmployeeNameById(eid);

           if (confirmDeletion(employeeName)) {
               if (deleteEmployee(eid)) {
                   showMessage("Employee deleted successfully.", "Success", false);
                   loadEmployeeData(tableModel);
               } else {
                   showMessage("Failed to delete employee.", "Error", true);
               }
           }
       }

   /**
    * Gets an employee's name by their ID
    * @param eid The employee ID
    * @return The employee's full name
    */
   private String getEmployeeNameById(String eid) {
       Object[] employee = getEmployeeById(eid);
       if (employee != null && employee.length >= 3) {
           return employee[1].toString() + " " + employee[2].toString();
       }
       return "Employee #" + eid;
   }

   /**
    * Shows a dialog for searching employees
    * @param parent The parent frame
    * @param tableModel The table model to display search results
    */
   public void showSearchDialog(JFrame parent, DefaultTableModel tableModel) {
       JDialog searchDialog = new JDialog(parent, "Search Employees", true);
       searchDialog.setSize(new Dimension(400, 200));
       searchDialog.setLocationRelativeTo(parent);

       // Create panel
       JPanel panel = new JPanel();
       panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

       // Add components
       JComboBox<String> columnComboBox = new JComboBox<>(new String[]{"EID"});
       JTextField searchField = new JTextField(20);

       // Add labels and components to panel
       panel.add(new JLabel("Search by:"));
       panel.add(columnComboBox);
       panel.add(new JLabel("Search term:"));
       panel.add(searchField);

       // Add buttons
       JButton searchButton = new JButton("Search");
       JButton cancelButton = new JButton("Cancel");

       // Add buttons to a button panel
       JPanel buttonPanel = new JPanel();
       buttonPanel.add(searchButton);
       buttonPanel.add(cancelButton);

       // Add panels to dialog
       searchDialog.add(panel, "Center");
       searchDialog.add(buttonPanel, "South");

       // Add action listeners to buttons
       searchButton.addActionListener(e -> {
           String column = (String) columnComboBox.getSelectedItem();
           String searchTerm = searchField.getText().trim();

           if (searchTerm.isEmpty()) {
               showMessage("Please enter a search term.", "Error", true);
               return;
           }

           String dbColumnName = column.replace(" ", "_");
           if (searchEmployees(dbColumnName, searchTerm, tableModel)) {
               searchDialog.dispose();
           } else {
               showMessage("EID not found!", "Error", true);
           }
       });

       cancelButton.addActionListener(e -> searchDialog.dispose());

       // Show dialog
       searchDialog.setVisible(true);
   }

    /**
     * Shows a dialog for sorting employees
     * @param parent The parent frame
     * @param tableModel The table model to sort
     */
    public void showSortDialog(JFrame parent, DefaultTableModel tableModel) {
        JDialog sortDialog = new JDialog(parent, "Sort Employees", true);
        sortDialog.setSize(new Dimension(400, 200));
        sortDialog.setLocationRelativeTo(parent);

        // Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components
        JComboBox<String> columnComboBox = new JComboBox<>(new String[]{
            "Status", "Designation", "EID", "Last_Name"
        });
        JRadioButton ascendingRadio = new JRadioButton("Ascending", true);
        JRadioButton descendingRadio = new JRadioButton("Descending", false);

        // Group radio buttons
        ButtonGroup sortOrderGroup = new ButtonGroup();
        sortOrderGroup.add(ascendingRadio);
        sortOrderGroup.add(descendingRadio);

        // Add labels and components to panel
        panel.add(new JLabel("Sort by:"));
        panel.add(columnComboBox);
        panel.add(new JLabel("Sort order:"));
        panel.add(ascendingRadio);
        panel.add(descendingRadio);

        // Add buttons
        JButton sortButton = new JButton("Sort");
        JButton cancelButton = new JButton("Cancel");

        // Add buttons to a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sortButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        sortDialog.add(panel, "Center");
        sortDialog.add(buttonPanel, "South");

        // Add action listeners to buttons
        sortButton.addActionListener(e -> {
            String column = (String) columnComboBox.getSelectedItem();
            boolean ascending = ascendingRadio.isSelected();

            String dbColumnName = column.replace(" ", "_");
            if (sortEmployees(dbColumnName, ascending, tableModel)) {
                sortDialog.dispose();
            } else {
                showMessage("Sort failed.", "Error", true);
            }
        });

        cancelButton.addActionListener(e -> sortDialog.dispose());

        // Show dialog
        sortDialog.setVisible(true);
    }
    
    /**
     * Display a message dialog with customized icon based on message type
     * @param message The message to display
    * @param title The dialog title
    * @param isError If true, shows error icon; otherwise shows information icon
    */
    public void showMessage(String message, String title, boolean isError) {
    if (message == null || message.trim().isEmpty()) {
        message = isError ? "An error occurred." : "Operation completed.";
    }
    
    int messageType = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
    JOptionPane.showMessageDialog(null, message, title, messageType);
    }
    
    /**
    * Shows a confirmation dialog for deletion
    * @param employeeName The name of the employee to be deleted
    * @return boolean indicating whether the user confirmed the deletion
    */
    public boolean confirmDeletion(String employeeName) {
    int confirm = JOptionPane.showConfirmDialog(
        null,
        "Are you sure you want to delete employee " + employeeName + "?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );
    
    return confirm == JOptionPane.YES_OPTION;
    }
   


    private void setPlaceholder(JTextField textField, String placeholder) {
    textField.setText(placeholder);
    textField.setForeground(Color.GRAY);
    
    textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(Color.GRAY);
            } else {
                textField.setForeground(Color.BLACK); // Add this line to ensure text stays black
            }
        }
    });
}


    private class PhoneNumberDocument extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // Only allow digits
        if (str == null || !str.matches("\\d*")) return;
        
        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
        
        // Remove any existing hyphens for calculation
        String plainText = newText.replaceAll("-", "");
        
        // Check if we're exceeding the 9-digit limit
        if (plainText.length() > 9) return;
        
        // Format with hyphens (XXX-XXX-XXX)
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            if (i == 3 || i == 6) formatted.append('-');
            formatted.append(plainText.charAt(i));
        }
        
        // Replace the entire content with formatted text
        remove(0, getLength());
        super.insertString(0, formatted.toString(), a);
    }
}

    private class SalaryDocument extends PlainDocument {
    private static final double MAX_SALARY = 999999.00;
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // Only allow digits and decimal point
        if (str == null || !str.matches("[0-9.]*")) return;
        
        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
        
        try {
            // Remove any existing commas
            String plainText = newText.replaceAll(",", "");
            
            // Check total length before decimal
            String[] parts = plainText.split("\\.");
            if (parts[0].length() > 6) return; // Max 6 digits before decimal
            
            // Allow only one decimal point and up to two digits after
            if (plainText.indexOf('.') != plainText.lastIndexOf('.')) return;
            if (parts.length > 1 && parts[1].length() > 2) return;
            
            // Insert the valid text
            super.insertString(offs, str, a);
        } catch (NumberFormatException e) {
            return;
        }
    }
}
    private void formatSalaryField(JTextField salaryField) {
    String text = salaryField.getText().trim().replaceAll("[^0-9.]", "");
    if (text.isEmpty()) {
        salaryField.setText("000000.00");
        return;
    }
    
    try {
        double salary = Double.parseDouble(text);
        salary = Math.min(salary, 999999.99);
        salaryField.setText(String.format("%09.2f", salary).substring(0, 9)); // Ensures 6 digits before .00
    } catch (NumberFormatException ex) {
        salaryField.setText("000000.00");
    }
}
    private void addSalaryFormatting(JTextField salaryField) {
    salaryField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            formatSalaryField(salaryField);
        }
    });
    // Set initial value
    salaryField.setText("000000.00");
}

    
    private class GovernmentIDDocument extends PlainDocument {
    private final int maxDigits;
    private final int[] hyphenPositions;
    
    public GovernmentIDDocument(int maxDigits) {
        this.maxDigits = maxDigits;
        
        // Set hyphen positions based on ID type/length
        if (maxDigits == 10) { // SSS: XX-XXXXXXX-X
            hyphenPositions = new int[]{2, 10};
        } else if (maxDigits == 9) { // TIN: XXX-XXX-XXX
            hyphenPositions = new int[]{3, 7};
        } else {
            hyphenPositions = new int[0]; // No hyphens for other types
        }
    }
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // Only allow digits
        if (str == null || !str.matches("\\d*")) return;
        
        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
        
        // Remove any existing hyphens for calculation
        String plainText = newText.replaceAll("-", "");
        
        // Check if we're exceeding the digit limit
        if (plainText.length() > maxDigits) return;
        
        // Format with hyphens based on ID type
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            // Add hyphen if needed
            for (int pos : hyphenPositions) {
                if (i == pos) formatted.append('-');
            }
            formatted.append(plainText.charAt(i));
        }
        
        // Replace the entire content with formatted text
        remove(0, getLength());
        super.insertString(0, formatted.toString(), a);
    }
}

    private class DateDocument extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // Only allow digits
        if (str == null || !str.matches("\\d*")) return;
        
        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
        
        // Remove any existing hyphens
        String plainText = newText.replaceAll("-", "");
        
        // Validate date length
        if (plainText.length() > 8) return;
        
        // Format with hyphens (YYYY-MM-DD)
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            if (i == 4 || i == 6) formatted.append('-');
            formatted.append(plainText.charAt(i));
        }
        
        // Replace the entire content with formatted text
        remove(0, getLength());
        super.insertString(0, formatted.toString(), a);
    }
}


    /**
    * Generates the next available EID based on the highest existing EID
    * @return String with the next EID
     */
    public String generateNextEID() {
    try (Connection conn = databaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT MAX(CAST(EID AS UNSIGNED)) AS max_eid FROM payrollmph")) {
        
        if (rs.next()) {
            int maxEid = rs.getInt("max_eid");
            return String.valueOf(maxEid + 1);
        }
        return "1001"; // Default starting EID if no records exist
        
    } catch (SQLException e) {
        System.err.println("Error generating next EID: " + e.getMessage());
        return "1001"; // Default starting EID if error occurs
    }
}

}