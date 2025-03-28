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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import javax.swing.JOptionPane;

public class LeaveController {
    private DatabaseConnection databaseConnection;
    private DBQueries dbQueries;
    private Connection connection;
    private DefaultTableModel dtm;
    private String loggedInUsername;
    private int eid;
    
    public LeaveController() {
        databaseConnection = new DatabaseConnection();
        dbQueries = new DBQueries();
    }
    
    // Set the logged-in employee details
    public void setLoggedInUser(String username, int eid) {
        this.loggedInUsername = username;
        this.eid = eid;
    }
    
    // Get current date formatted as string
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
    
    // Generate a unique Leave ID
    private String generateLeaveID(Connection existingConnection) {
        String prefix = "L";
        int nextSequence = 1000; // Default starting value if no previous IDs exist
        String leaveID = null;

        Connection conn = existingConnection; // Uses the passed connection
        boolean needToClose = false;
        ResultSet rs = null; // Declare ResultSet outside of try block

        try {
            // Only creates a new connection if one wasn't provided
            if (conn == null || conn.isClosed()) {
                conn = databaseConnection.getConnection();
                needToClose = true; 
            }

            while (true) {
                // Gets the highest Leave_ID
                String query = "SELECT MAX(Leave_ID) as maxID FROM leaves WHERE Leave_ID LIKE ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, prefix + "%");
                rs = ps.executeQuery(); // Assign to the declared variable

                if (rs.next() && rs.getString("maxID") != null) {
                    String lastID = rs.getString("maxID");
                    // Extract the numeric part
                    String numericPart = lastID.substring(prefix.length());
                    // Increment by 1
                    nextSequence = Integer.parseInt(numericPart) + 1;
                }

                leaveID = prefix + nextSequence;

                // Check if the generated Leave ID already exists
                String checkQuery = "SELECT COUNT(*) FROM leaves WHERE Leave_ID = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                checkPs.setString(1, leaveID);
                ResultSet checkRs = checkPs.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) == 0) {
                    // Leave ID is unique, break the loop
                    checkRs.close(); // Close checkRs here
                    break;
                }

                // If not unique, increment and try again
                nextSequence++;
                checkRs.close(); // Close checkRs here
            }

        } catch (SQLException e) {
            System.err.println("Error generating Leave ID: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) {
                    rs.close(); // Close the ResultSet if it was opened
                }
                if (conn != null && needToClose) {
                    conn.close(); // Close the connection if it was created here
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return leaveID;
    }
    
    public boolean submitLeaveApplication(JDateChooser dateFrom, JDateChooser dateTo, JComboBox<String> reasonCombo) {
        if (dateFrom.getDate() == null || dateTo.getDate() == null || reasonCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, 
                "Please select both dates and a reason for leave.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false; // Validation failed
        }
        boolean success = false;
        try {
            connection = databaseConnection.getConnection();
            // Generate unique Leave ID
            String leaveID = generateLeaveID(connection);

            // Debug print statements
            System.out.println("Generated Leave ID: " + leaveID);
            System.out.println("Employee ID: " + eid);

            String leavesql = "INSERT INTO leaves"
                    + "(Leave_ID,"
                    + "EID,"
                    + "Date_Filed,"
                    + "Date_From,"
                    + "Date_To,"
                    + "Reason_For_Leave,"
                    + "Leave_Status)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Format dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDateFrom = dateFormat.format(dateFrom.getDate());
            String formattedDateTo = dateFormat.format(dateTo.getDate());
            String dateToday = getCurrentDate();
            String reason = reasonCombo.getSelectedItem().toString();

            // Inserts the leave record using the same connection
            PreparedStatement ps = connection.prepareStatement(leavesql);
            ps.setString(1, leaveID);     // Leave_ID
            ps.setInt(2, eid);            // EID 
            ps.setString(3, dateToday);   // Date_Filed (current date)
            ps.setString(4, formattedDateFrom); // Date_From
            ps.setString(5, formattedDateTo);   // Date_To
            ps.setString(6, reason);       // Reason_For_Leave
            ps.setString(7, "Pending");    // Leave_Status - default to Pending

            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error submitting leave application: " + e.getMessage());
            // Print full stack trace for more details
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to submit leave application. " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return success; 
    }

        // Clear all form fields
    public void clearLeaveForm(JDateChooser dateFrom, JDateChooser dateTo, JComboBox<String> reasonCombo) {
        dateFrom.setDate(null);
        dateTo.setDate(null);
        reasonCombo.setSelectedIndex(0); // Assuming index 0 is a prompt like "-- Select One --"
    }
    
    // Get all leaves for the current employee
    public DefaultTableModel getEmployeeLeaves() {
        // Create the table model with column headers
        String[] columnNames = {"Leave ID", "Date Filed", "Date From", "Date To", "Reason For Leave", "Leave Status"};
        dtm = new DefaultTableModel(columnNames, 0);
        
        try {
            connection = databaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(dbQueries.getLeaveByEid);
            ps.setInt(1, eid);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"),
                    rs.getString("Date_Filed"),
                    rs.getString("Date_From"),
                    rs.getString("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                dtm.addRow(row);
            }
            
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error fetching employee leaves: " + e.getMessage());
        }
        
        return dtm;
    }
    
    // Method to populate the table model with all leave data
    public DefaultTableModel getAllLeavesTableModel() {
        // Create the table model with column headers
        String[] columnNames = {"Leave ID", "EID", "Date Filed", "Date From", "Date To", "Reason For Leave", "Leave Status"};
        dtm = new DefaultTableModel(columnNames, 0);
        
        try {
            connection = databaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(dbQueries.leaveData);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"),
                    rs.getString("EID"),
                    rs.getString("Date_Filed"),
                    rs.getString("Date_From"),
                    rs.getString("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                dtm.addRow(row);
            }
            
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error fetching leave data: " + e.getMessage());
        }
        
        return dtm;
    }
    
    // Method to refresh/update the table with current data
    public void refreshLeavesTable(javax.swing.JTable leavesTable) {
        leavesTable.setModel(getAllLeavesTableModel());
    }
    
    // Method to update leave status
    public boolean updateLeaveStatus(String leaveId, String newStatus) {
        boolean success = false;
        try {
            connection = databaseConnection.getConnection();
            // Correcting the SQL syntax in your query
            String updateQuery = "UPDATE leaves SET Leave_Status = ? WHERE Leave_ID = ?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setString(1, newStatus);
            ps.setString(2, leaveId);
            
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
            
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error updating leave status: " + e.getMessage());
        }
        
        return success;
    }

    
    // Method to sort the table by a specific column
    public void sortLeavesTable(javax.swing.JTable leavesTable, String columnName, boolean ascending) {
        try {
            connection = databaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(dbQueries.createSortLeave(columnName, ascending));
            ResultSet rs = ps.executeQuery();
            
            // Clear existing data
            dtm = (DefaultTableModel) leavesTable.getModel();
            dtm.setRowCount(0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("EID"),
                    rs.getString("Leave_ID"),
                    rs.getString("Date_Filed"),
                    rs.getString("Date_From"),
                    rs.getString("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                dtm.addRow(row);
            }
            
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error sorting leave data: " + e.getMessage());
        }
    }
    
   // Method to search leaves by any field
    public void searchLeaves(javax.swing.JTable leavesTable, String searchTerm) {
        try {
            connection = databaseConnection.getConnection();
            // Create a query that searches across multiple fields
            String searchQuery = "SELECT * FROM leaves WHERE " +
                                "EID LIKE ? OR " +
                                "Leave_ID LIKE ? OR " +
                                "Date_Filed LIKE ? OR " +
                                "Date_From LIKE ? OR " +
                                "Date_To LIKE ? OR " +
                                "Reason_For_Leave LIKE ? OR " +
                                "Leave_Status LIKE ?";
                                
            PreparedStatement ps = connection.prepareStatement(searchQuery);
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, searchPattern);
            }
            
            ResultSet rs = ps.executeQuery();
            
            // Clear existing data
            dtm = (DefaultTableModel) leavesTable.getModel();
            dtm.setRowCount(0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"),
                    rs.getString("EID"),
                    rs.getString("Date_Filed"),
                    rs.getString("Date_From"),
                    rs.getString("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                dtm.addRow(row);
            }
            
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error searching leave data: " + e.getMessage());
        }
    }
}