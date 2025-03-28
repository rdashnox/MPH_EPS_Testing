/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Controller;

/**
 *
 * @author dashcodes
 */

import DatabaseConnector.DatabaseConnection;
import GUI.ViewTimesheet;
import java.sql.*;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TimesheetController {
    private final DatabaseConnection databaseConnection;
    private final DBQueries dbQueries;
    
    public TimesheetController(){
        databaseConnection = new DatabaseConnection();
        dbQueries = new DBQueries();
    }
    
    public boolean loadTimesheetData(DefaultTableModel tableModel) throws SQLException {
    try (Connection conn = databaseConnection.getConnection();
         Statement stmt = conn.createStatement()) {
        
        System.out.println("Executing query: " + dbQueries.timesheetData);
        ResultSet rs = stmt.executeQuery(dbQueries.timesheetData);
            
        // Clear existing rows
        tableModel.setRowCount(0);
        
        int rowCount = 0;
        
        // Populate the table model with data
        while (rs.next()) {
            Object[] row = new Object[5];
            row[0] = rs.getString("AttendanceID");
            row[1] = rs.getInt("EID");
            row[2] = rs.getString("LogDate");
            row[3] = rs.getString("logTime");
            row[4] = rs.getString("AttStatus");
            tableModel.addRow(row);
            rowCount++;
        }
        
        System.out.println("Loaded " + rowCount + " rows of timesheet data");
        return rowCount > 0; // Return true if at least one row was loaded
    } catch (SQLException e) {
        System.err.println("Error loading timesheet data: " + e.getMessage());
        e.printStackTrace(); // Print the full stack trace for debugging
        throw e; // Re-throw to be handled by the caller
    }
}
    
    /**
     * Adds a timeLog to the database
     * @param timeLog An array of employee data
     * @return Boolean indicating success or failure
     */
    public boolean addTimeLog(Object[] timeLog) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getInsertTimeLog())) {
            
            stmt.setString(1, (String) timeLog[0]); // AttendanceID
            stmt.setString(2, (String) timeLog[1]); // EID
            stmt.setString(3, (String) timeLog[2]); // LogDate
            stmt.setString(4, (String) timeLog[3]); // LogTime
            stmt.setString(5, (String) timeLog[4]); // AttStatus
            
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding timeLog: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates an existing timeLog in the database
     * @param timeLog An array of updated employee data
     * @return Boolean indicating success or failure
     */
    public boolean updateTimeLog (Object[] timeLog) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getUpdateTimeLog())) {
            
            stmt.setString(1, (String) timeLog[1]); // AttendanceID
            stmt.setString(2, (String) timeLog[2]); // EID
            stmt.setString(3, (String) timeLog[3]); // LogDate
            stmt.setString(4, (String) timeLog[4]); // LogTime
            stmt.setString(5, (String) timeLog[5]); // AttStatus
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating timeLog: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a timeLog from the database
     * @param attendanceID to delete
     * @return Boolean indicating success or failure
     */
    public boolean deletetimeLog (String attendanceID) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getDeleteTimeLog())) {
            
            stmt.setString(1, attendanceID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves Timesheet data for a specific EID
     * @param eid Employee ID to retrieve
     * @return Object array containing employee data or null if not found
     */
    public Object[] getTimesheetByEid(String eid) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getGetTimesheetByEid())) {
            
            stmt.setString(1, eid);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Object[] timeLog = new Object[5];
                timeLog[0] = rs.getString(dbQueries.getAttendanceID());
                timeLog[1] = rs.getString(dbQueries.getEid());
                timeLog[2] = rs.getString(dbQueries.getLogDate());
                timeLog[3] = rs.getString(dbQueries.getLogTime());
                timeLog[4] = rs.getString(dbQueries.getAttStatus());
                                
                return timeLog;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving timeLog: " + e.getMessage());
        }
        return null;
    }

     /**
     * Shows a dialog to update timesheet information
     * @param parent The parent frame
     * @param eid Employee ID to update
     * @param tableModel Table model to refresh after update
     */
    public void showUpdateDialog(ViewTimesheet parent, String eid, DefaultTableModel tableModel) {
        try {
            // Get the timesheet data for the specified EID
            Object[] timesheetData = getTimesheetByEid(eid);

            if (timesheetData == null) {
                showMessage("No timesheet data found for employee ID: " + eid, "Data Not Found", false);
                return;
            }

            // Create a dialog for updating
            JDialog dialog = new JDialog(parent, "Update Timesheet Entry", true);
            dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(parent);

            // Create input fields with current data
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Attendance ID (non-editable)
            JLabel lblAttendanceID = new JLabel("Attendance ID:");
            JTextField txtAttendanceID = new JTextField((String) timesheetData[0]);
            txtAttendanceID.setEditable(false);

            // Employee ID
            JLabel lblEID = new JLabel("Employee ID:");
            JTextField txtEID = new JTextField((String) timesheetData[1]);

            // Log Date
            JLabel lblLogDate = new JLabel("Log Date (YYYY-MM-DD):");
            JTextField txtLogDate = new JTextField((String) timesheetData[2]);

            // Log Time
            JLabel lblLogTime = new JLabel("Log Time (HH:MM:SS):");
            JTextField txtLogTime = new JTextField((String) timesheetData[3]);

            // AttStatus
            JLabel lblStatus = new JLabel("AttStatus:");
            JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Present", "Late", "Absent", "Leave"});
            cmbStatus.setSelectedItem(timesheetData[4]);

            // Add components to panel
            panel.add(lblAttendanceID);
            panel.add(txtAttendanceID);
            panel.add(lblEID);
            panel.add(txtEID);
            panel.add(lblLogDate);
            panel.add(txtLogDate);
            panel.add(lblLogTime);
            panel.add(txtLogTime);
            panel.add(lblStatus);
            panel.add(cmbStatus);

            // Add buttons
            JPanel buttonPanel = new JPanel();
            JButton btnUpdate = new JButton("Update");
            JButton btnCancel = new JButton("Cancel");

            btnUpdate.addActionListener(e -> {
                // Validate inputs
                if (txtEID.getText().trim().isEmpty() || txtLogDate.getText().trim().isEmpty() ||
                    txtLogTime.getText().trim().isEmpty()) {
                    showMessage("All fields are required!", "Validation Error", false);
                    return;
                }

                // Create updated timesheet data
                Object[] updatedData = new Object[6];
                updatedData[0] = txtAttendanceID.getText().trim();
                updatedData[1] = txtEID.getText().trim();
                updatedData[2] = txtLogDate.getText().trim();
                updatedData[3] = txtLogTime.getText().trim();
                updatedData[4] = cmbStatus.getSelectedItem().toString();

                // Update timesheet
                boolean success = updateTimeLog(updatedData);

                if (success) {
                    showMessage("Timesheet entry updated successfully.", "Success", true);
                    try {
                        loadTimesheetData(tableModel);
                    } catch (SQLException ex) {
                        showMessage("Error refreshing data: " + ex.getMessage(), "Database Error", false);
                    }
                    dialog.dispose();
                } else {
                    showMessage("Failed to update timesheet entry.", "Update Error", false);
                }
            });

            btnCancel.addActionListener(e -> dialog.dispose());

            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnCancel);

            dialog.add(panel);
            dialog.add(buttonPanel);
            dialog.setVisible(true);

        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), "Error", false);
        }
    }

    /**
     * Shows a dialog to search for timesheet entries
     * @param parent The parent frame
     * @param tableModel Table model to display search results
     */
    public void showSearchDialog(ViewTimesheet parent, DefaultTableModel tableModel) {
        // Create a dialog for searching
        JDialog dialog = new JDialog(parent, "Search Timesheet Entries", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(parent);

        // Create search options
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Search field selection
        JLabel lblField = new JLabel("Search by:");
        String[] searchFields = {"AttendanceID", "EID", "LogDate", "AttStatus"};
        JComboBox<String> cmbField = new JComboBox<>(searchFields);

        // Search value
        JLabel lblValue = new JLabel("Search value:");
        JTextField txtValue = new JTextField(20);

        // Add components to panel
        panel.add(lblField);
        panel.add(cmbField);
        panel.add(lblValue);
        panel.add(txtValue);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSearch = new JButton("Search");
        JButton btnCancel = new JButton("Cancel");

        btnSearch.addActionListener(e -> {
            String searchField = cmbField.getSelectedItem().toString();
            String searchValue = txtValue.getText().trim();

            if (searchValue.isEmpty()) {
                showMessage("Please enter a search value", "Search Error", false);
                return;
            }

            try {
                // Perform search using the DBQueries
                DBQueries dbQueries = new DBQueries();
                String searchQuery = dbQueries.createSearchTimeLog(searchField);

                try (Connection conn = databaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(searchQuery)) {

                    stmt.setString(1, "%" + searchValue + "%");
                    ResultSet rs = stmt.executeQuery();

                    // Clear existing rows
                    tableModel.setRowCount(0);

                    // Populate with search results
                    boolean resultsFound = false;
                    while (rs.next()) {
                        resultsFound = true;
                        Object[] row = new Object[5];
                        row[0] = rs.getString("AttendanceID");
                        row[1] = rs.getInt("EID");
                        row[2] = rs.getString("LogDate");
                        row[3] = rs.getString("LogTime");
                        row[4] = rs.getString("AttStatus");
                        tableModel.addRow(row);
                    }

                    if (!resultsFound) {
                        showMessage("No results found for your search criteria.", "Search Results", true);
                    }

                    dialog.dispose();
                }
            } catch (SQLException ex) {
                showMessage("Search error: " + ex.getMessage(), "Database Error", false);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSearch);
        buttonPanel.add(btnCancel);

        dialog.add(panel);
        dialog.add(buttonPanel);
        dialog.setVisible(true);
    }

    /**
     * Shows a message dialog
     * @param message The message to display
     * @param title Dialog title
     * @param isInformation Whether this is an information message (true) or error (false)
     */
    public void showMessage(String message, String title, boolean isInformation) {
        int messageType = isInformation ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /**
     * Deletes a timesheet entry with confirmation
     * @param eid Employee ID to delete
     * @param tableModel Table model to refresh after deletion
     */
    public void deleteEmployeeWithConfirmation(String eid, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure you want to delete timesheet entries for employee ID " + eid + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = deletetimeLog(eid);

            if (success) {
                showMessage("Timesheet entries deleted successfully.", "Deletion Successful", true);
                try {
                    loadTimesheetData(tableModel);
                } catch (SQLException ex) {
                    showMessage("Error refreshing data: " + ex.getMessage(), "Database Error", false);
                }
            } else {
                showMessage("Failed to delete timesheet entries.", "Deletion Failed", false);
            }
        }
    }

    /**
     * Gets the selected row index from the table
     * @return The selected row index or -1 if no row is selected
     */
    public int getSelectedRow() {
        JTable table = new JTable(); // This would be a class field in the real implementation
        return table.getSelectedRow();
    }

    /**
     * Saves a new timesheet entry from the table data
     * @param tableModel Table model containing the new entry
     */
    public void saveNewTimeLogFromTable(DefaultTableModel tableModel) {
        try {
            // Get the last row (assuming it's the new entry)
            int rowIndex = tableModel.getRowCount() - 1;

            if (rowIndex < 0) {
                showMessage("No data to save.", "Save Error", false);
                return;
            }

            // Extract data from the table row
            Object[] timeLogData = new Object[5];

            // Check if any required field is empty
            for (int i = 0; i < 5; i++) {
                if (tableModel.getValueAt(rowIndex, i) == null || 
                    tableModel.getValueAt(rowIndex, i).toString().trim().isEmpty()) {
                    showMessage("All fields are required. Please complete all entries.", "Validation Error", false);
                    return;
                }
                timeLogData[i] = tableModel.getValueAt(rowIndex, i).toString().trim();
            }

            // Add the new timesheet entry
            boolean success = addTimeLog(timeLogData);

            if (success) {
                showMessage("New timesheet entry added successfully.", "Success", true);

                // Add a new empty row for the next entry
                addEmptyRowToTable(tableModel);
            } else {
                showMessage("Failed to add new timesheet entry.", "Save Error", false);
            }
        } catch (Exception e) {
            showMessage("Error saving data: " + e.getMessage(), "Error", false);
        }
    }

    /**
     * Shows a dialog to sort timesheet entries
     * @param parent The parent frame
     * @param tableModel Table model to display sorted results
     */
    public void showSortDialog(ViewTimesheet parent, DefaultTableModel tableModel) {
        // Create a dialog for sorting
        JDialog dialog = new JDialog(parent, "Sort Timesheet Entries", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(parent);

        // Create sort options
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Sort field selection
        JLabel lblField = new JLabel("Sort by:");
        String[] sortFields = {"AttendanceID", "EID", "LogDate", "LogTime", "AttStatus"};
        JComboBox<String> cmbField = new JComboBox<>(sortFields);

        // Sort direction
        JLabel lblDirection = new JLabel("Sort direction:");
        JRadioButton rbAscending = new JRadioButton("Ascending", true);
        JRadioButton rbDescending = new JRadioButton("Descending", false);

        ButtonGroup directionGroup = new ButtonGroup();
        directionGroup.add(rbAscending);
        directionGroup.add(rbDescending);

        // Add components to panel
        panel.add(lblField);
        panel.add(cmbField);
        panel.add(lblDirection);
        panel.add(rbAscending);
        panel.add(rbDescending);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSort = new JButton("Sort");
        JButton btnCancel = new JButton("Cancel");

        btnSort.addActionListener(e -> {
            String sortField = cmbField.getSelectedItem().toString();
            boolean isAscending = rbAscending.isSelected();

            try {
                // Perform sort using the DBQueries
                DBQueries dbQueries = new DBQueries();
                String sortQuery = dbQueries.createSortTimeLog(sortField, isAscending);

                try (Connection conn = databaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sortQuery)) {

                    // Clear existing rows
                    tableModel.setRowCount(0);

                    // Populate with sorted results
                    while (rs.next()) {
                        Object[] row = new Object[5];
                        row[0] = rs.getString("AttendanceID");
                        row[1] = rs.getInt("EID");
                        row[2] = rs.getString("LogDate");
                        row[3] = rs.getString("LogTime");
                        row[4] = rs.getString("AttStatus");
                        tableModel.addRow(row);
                    }

                    dialog.dispose();
                }
            } catch (SQLException ex) {
                showMessage("Sort error: " + ex.getMessage(), "Database Error", false);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSort);
        buttonPanel.add(btnCancel);

        dialog.add(panel);
        dialog.add(buttonPanel);
        dialog.setVisible(true);
    }

    /**
     * Adds an empty row to the table for a new entry
     * @param tableModel Table model to add empty row to
     */
    public void addEmptyRowToTable(DefaultTableModel tableModel) {
        // Get the next available attendance ID (this could be auto-generated)
        String nextAttendanceID = "A" + (System.currentTimeMillis() % 10000);

        // Add an empty row with the generated attendance ID
        Object[] emptyRow = {nextAttendanceID, "", "", "", ""};
        tableModel.addRow(emptyRow);
    }
}