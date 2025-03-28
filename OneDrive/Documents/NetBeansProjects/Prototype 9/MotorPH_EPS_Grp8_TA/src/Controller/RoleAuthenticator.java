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
import BusinessLogic.Allowances;
import BusinessLogic.Deductions;
import BusinessLogic.EmployeeDetails;
import GUI.EmployeeDashboard;
import GUI.AdminDashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RoleAuthenticator {
    private DBQueries dbq = new DBQueries();
    private DatabaseConnection dbConnection;
    private Connection connection;
    private SalaryCalculator salaryCalculator;
    private static EmployeeDetails loggedInUser;
    
    public RoleAuthenticator(Connection connection) {
        this.salaryCalculator = new SalaryCalculator(connection);
    }
    
    /**
     * Gets all the data needed for the Employee Dashboard
     * @param eid
     * @return EmployeeDashboardData containing all necessary dashboard information
     * @throws SQLException if there's a database error
     */
    public EmployeeDashboardData getEmployeeDashboardData(int eid) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }
        
        EmployeeDetails employee = getEmployeeDetails(eid);
        Allowances allowances = salaryCalculator.getAllowancesForEmployee(eid);
        Deductions deductions = salaryCalculator.getDeductionsForEmployee(eid);

        if (employee != null && allowances != null && deductions != null) {
            return new EmployeeDashboardData(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDesignation(),
                allowances.getBasicSalary(),
                salaryCalculator.calculateTotalAllowances(allowances),
                deductions.getTotalDeductions()
            );
        }
        throw new SQLException("Could not retrieve all required employee data");
    }
    
    public static void setLoggedInUser(EmployeeDetails user) {
        loggedInUser = user;
    }
    
    public static EmployeeDetails getLoggedInUser() {
        return loggedInUser;
    }
    
    // An inner class to hold All Dashboard Data
    public static class EmployeeDashboardData {
        private final String firstName;
        private final String lastName;
        private final String designation;
        private final float basicSalary;
        private final float totalAllowances;
        private final float totalDeductions;
        private final float netPay;
        
        
        public EmployeeDashboardData(String firstName, String lastName, String designation, 
                                     float basicSalary, float totalAllowances, float totalDeductions) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.designation = designation;
            this.basicSalary = basicSalary;
            this.totalAllowances = totalAllowances;
            this.totalDeductions = totalDeductions;
            this.netPay = basicSalary + totalAllowances - totalDeductions;
        }

        // Getter methods
        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDesignation() {
            return designation;
        }

        public float getBasicSalary() {
            return basicSalary;
        }

        public float getTotalAllowances() {
            return totalAllowances;
        }

        public float getTotalDeductions() {
            return totalDeductions;
        }

        public float getNetPay() {
            return netPay;
        }
    }
    
    public RoleAuthenticator() throws SQLException {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean checkUsernameExists(String username) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }

        try (PreparedStatement stmt = connection.prepareStatement(dbq.getUsernameExistsQuery())) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
        
        /**
     * Authenticates a user and retrieves their details using an existing connection
     * This is useful for pages that already have an active connection and need to reload user details
     * 
     * @param username The username to authenticate
     * @param connection An existing database connection (can be null)
     * @return EmployeeDetails object if successful, null otherwise
     * @throws SQLException if database error occurs
     */
    public EmployeeDetails authenticateAndGetUserDetails(String username, Connection connection) throws SQLException {
        // Handle the case where connection is null by creating a new one
        boolean shouldCloseConnection = false;

    if (connection == null) {
        // Create a new database connection if none was provided
        DatabaseConnection dbConnection = new DatabaseConnection();
        connection = dbConnection.getConnection();
        shouldCloseConnection = true; // Mark to close this connection afterward
    }

    try {
        // Load user details using the connection (either provided or newly created)
        EmployeeDetails user = EmployeeDetails.loadByUsername(username, connection);
        if (user != null) {
            // Store the authenticated user in both places for consistency
            setLoggedInUser(user);
            UserSession.getInstance().setLoggedInUser(user);
            UserSession.getInstance().setUsername(username);
            return user;
        }
        return null;
    } finally {
        // Close the connection only if we created it in this method
        if (shouldCloseConnection && connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());
                logger.log(java.util.logging.Level.WARNING, "Error closing database connection", e);
                }
            }
        }
    }
    
    /**
     * Authenticates user and returns their details
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @return EmployeeDetails object with user information, or null if authentication fails
     * @throws SQLException If database access fails
     */    
    public EmployeeDetails authenticateAndGetUserDetails(String username, String password) throws SQLException {
        List<EmployeeDetails> userDetailsList = userLogin(username, password);

        if (!userDetailsList.isEmpty()) {
            // Authentication is successful here
            EmployeeDetails userDetails = userDetailsList.get(0);

            // Fetch financial data for the user
            float basicSalary = getBasicSalary(userDetails.getEid());
            float totalAllowances = getTotalAllowances(userDetails.getEid());
            float totalDeductions = getTotalDeductions(userDetails.getEid());
            float netPay = basicSalary + totalAllowances - totalDeductions;

            // Set financial data in the EmployeeDetails object
            userDetails.setBasicSalary(basicSalary);
            userDetails.setTotalAllowances(totalAllowances);
            userDetails.setTotalDeductions(totalDeductions);
            userDetails.setNetPay(netPay);

            // Set user in both the static field and UserSession singleton
            setLoggedInUser(userDetails);
            UserSession.getInstance().setLoggedInUser(userDetails);
            UserSession.getInstance().setUsername(username);

            return userDetails;
        }
        return null; // Authentication failed
        }

    
    /**
     * Redirects user to the appropriate dashboard based on their designation.
     * 
     * @param loggedInUser The authenticated user's details
     */
    public void redirectBasedOnRole(EmployeeDetails loggedInUser) {
        if (loggedInUser == null) {
            throw new IllegalArgumentException("Logged-in user details cannot be null.");
        }

        String designation = loggedInUser.getDesignation();

        if (isRankAndFile(designation)) {
            openEmployeeDashboard(loggedInUser);
        } else {
            openAdminDashboard(loggedInUser);
        }
    }

    /**
     * For use with ChangePassword - redirects user after looking up their details.
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @throws SQLException If database access fails or user details cannot be retrieved
     */
    public void redirectBasedOnRole(String username, String password) throws SQLException {
        EmployeeDetails loggedInUser = authenticateAndGetUserDetails(username, password);
        if (loggedInUser != null) {
            redirectBasedOnRole(loggedInUser);
        } else {
            throw new SQLException("Failed to retrieve user details after password change.");
        }
    }

    /**
     * Checks if the user's designation is "Rank and File".
     * @param designation The user's designation
     * @return True if the designation contains "Rank and File", otherwise false
     */
    private boolean isRankAndFile(String designation) {
        return designation != null && designation.contains("Rank and File");
    }

    /**
     * Opens the EmployeeDashboard with the provided user details.
     * // Newly added
     * @param loggedInUser The authenticated user's details
     */
    private void openEmployeeDashboard(EmployeeDetails loggedInUser) {
        new EmployeeDashboard(
            loggedInUser.getFirstName(),
            loggedInUser.getLastName(),
            loggedInUser.getDesignation(),
            loggedInUser.getEid(),
            loggedInUser.getBasicSalary(),
            loggedInUser.getTotalAllowances(),
            loggedInUser.getTotalDeductions(),
            loggedInUser.getNetPay()
        ).setVisible(true);
    }

    /**
     * Opens the AdminDashboard with the provided user details.
     * 
     * @param loggedInUser The authenticated user's details
     */
    private void openAdminDashboard(EmployeeDetails loggedInUser) {
        new AdminDashboard(
            loggedInUser.getFirstName(),
            loggedInUser.getLastName(),
            loggedInUser.getDesignation(),
            loggedInUser.getEid()
            ).setVisible(true);
    }
    
     
    public List<EmployeeDetails> userLogin(String username, String password) throws SQLException {
        List<EmployeeDetails> list = new ArrayList<>();

        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }

        try (PreparedStatement ps = connection.prepareStatement(dbq.getUserLoginQuery())) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new EmployeeDetails(
                    rs.getInt("EID"),
                    rs.getString("Last_Name"),
                    rs.getString("First_Name"),
                    rs.getDate("Birthday"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Designation"),
                    rs.getString("Address"),
                    rs.getString("Phone_Number"),
                    rs.getString("SSS_#"),
                    rs.getString("Philhealth_#"),
                    rs.getString("TIN_#"),
                    rs.getString("Pag-ibig_#"),
                    rs.getString("Status"),
                    rs.getString("Immediate_Supervisor")
                ));
            }

            if (!list.isEmpty()) {
                // Success message will be handled by the caller
            } else {
                // Error message will be handled by the caller
            }
        }
        return list;
    }
        
    /**
     * Gets employee details by employee ID
     */
    private EmployeeDetails getEmployeeDetails(int eid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM employee_details WHERE EID = ?")) {
            ps.setInt(1, eid);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new EmployeeDetails(
                    rs.getInt("EID"),
                    rs.getString("Last_Name"),
                    rs.getString("First_Name"),
                    rs.getDate("Birthday"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Designation"),
                    rs.getString("Address"),
                    rs.getString("Phone_Number"),
                    rs.getString("SSS_#"),
                    rs.getString("Philhealth_#"),
                    rs.getString("TIN_#"),
                    rs.getString("Pag-ibig_#"),
                    rs.getString("Status"),
                    rs.getString("Immediate_Supervisor")
                );
            }
        }
        return null;
    }

    
    /**
     * Calculates total allowances from an Allowances object
     */
    private float calculateTotalAllowances(Allowances allowances) {
        return allowances.getRiceAllowance() + 
               allowances.getPhoneAllowance() + 
               allowances.getClothingAllowance();
    }
    
    /**
     * Gets the current date formatted as a string
     * @return Formatted date string
     */
    public static String getCurrentDateFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return today.format(formatter);
    }
    
    /**
     * Gets the current time formatted as a string
     * @return Formatted time string
     */
    public static String getCurrentTimeFormatted() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return now.format(formatter);
    }
    
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        // First, verify the user exists
        String checkUserSql = "SELECT COUNT(*) FROM payrollmph WHERE username = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // User does not exist
                System.out.println("No user found with username: " + username);
                return false;
            }
        }

        // If user exists, proceed with password update
        String sql = "UPDATE payrollmph SET password = ? WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected + " for username: " + username);

            return rowsAffected > 0;
        }
    }
    
   
    /**
     * Fetches the basic pay for the given employee ID.
     */
    private float getBasicSalary(int eid) throws SQLException {
        
        return 25000.0f; // Replace with actual database query
    }

    /**
     * Fetches the total allowances for the given employee ID.
     */
    private float getTotalAllowances(int eid) throws SQLException {
        // Execute a query to fetch totalAllowances from the database
        // Example: SELECT totalAllowances FROM payroll WHERE EID = ?
        // Return the fetched value
        return 5000.0f; // Replace with actual database query
    }

    /**
     * Fetches the total deductions for the given employee ID.
     */
    private float getTotalDeductions(int eid) throws SQLException {
        // Execute a query to fetch totalDeductions from the database
        // Example: SELECT totalDeductions FROM payroll WHERE EID = ?
        // Return the fetched value
        return 3000.0f; // Replace with actual database query
    }

   /**
     * Authenticates user based on their designation and opens the appropriate dashboard
     * 
     * @param designation The user's designation from the database
     * @return boolean indicating successful authentication
     */
    public boolean authenticateAndRedirect(String designation) {
        try {
            if (designation != null) {
                if (designation.contains("Rank and File")) {
                    // For all Rank and File designations (HR, Payroll, Account)
                    EmployeeDashboard employeeDashboard = new EmployeeDashboard();
                    employeeDashboard.setVisible(true);
                } else {
                    // For all other designations (CEO, managers, TLs, etc.)
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                }
                return true;
                
            } else {
                System.out.println("Error: Designation is null");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error during authentication: " + e.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}