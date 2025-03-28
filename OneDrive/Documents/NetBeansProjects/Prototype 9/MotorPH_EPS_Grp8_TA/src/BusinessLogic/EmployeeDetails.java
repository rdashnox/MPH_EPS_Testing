/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package BusinessLogic;

/**
 *
 * @author dashcodes
 */

import Controller.DBQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeDetails
{

    // Employee basic details
    private int eid;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String designation;
    private String address;
    private String phoneNumber;
    private Date birthday;
    private String status;
    private String immediateSupervisor;
    
    // Government IDs
    private String sss;
    private String philHealth;
    private String tin;
    private String pagIbig;
    
    // Financial details
    private float basicSalary;
    private String riceSubsidy;
    private String phoneAllowance;  
    private String clothingAllowance;
    
    // Financial fields
    private float totalAllowances;
    private float totalDeductions;
    private float totalIncentives;
    private float netPay;
    
    // What I newly added for the Gross Pay Section
    private float overTimePay;
    private float overTime;
    private float holidayPay;
    private float performanceBonus;
    
    // For newly added Earnings Section
    private float hourlyRate;
    private float daysWorked;
    
    // Establish Connection
    Connection connection;

    
    /**
     * Constructor overloading allows a class to have multiple constructors 
     * with different parameters, providing flexible ways to create objects.
     */
    
    public EmployeeDetails (){}
    
    public EmployeeDetails
        (
        int aInt,
        int aInt1,
        String string,
        String string1,
        String string2,
        Date date,
        String string3,
        String string4
        ){}
    
    public EmployeeDetails
            (
            int eid,
            String lastName,
            String firstName,
            Date birthday
            )
                
        {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        }
    
    public EmployeeDetails
            (
            String userName,
            String designation,
            String address,
            String phoneNumber
            )
                
        {
        this.userName = userName;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        }
   
    public EmployeeDetails
            (
            String sss,
            String philHealth,
            String tin,
            String pagIbig,
            String status,
            String immediateSupervisor
            )
                    
        {
         this.sss = sss;
         this.philHealth = philHealth;
         this.tin = tin;
         this.pagIbig = pagIbig;
         this.status = status;
         this.immediateSupervisor = immediateSupervisor;
        }
    
    public EmployeeDetails
            (
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance
            )
           
        {
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        }
    
    public EmployeeDetails
            (
            int eid,
            String lastName,
            String firstName,
            Date birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber
            )
                    
        {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        }
            
    public EmployeeDetails
            (
            int eid,
            String lastName,
            String firstName,
            Date birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber,
            String status,
            String immediateSupervisor
            )
        
        {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
        }
    
    public EmployeeDetails
            (
            int eid,
            String lastName,
            String firstName,
            Date birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber,
            String sss,
            String philHealth,
            String tin,
            String pagIbig,
            String status,
            String immediateSupervisor
            )
                    
        {
        
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;    
        }
    
    public EmployeeDetails
            (
            int eid,
            String lastName,
            String firstName,
            Date birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber,
            String sss,
            String philHealth,
            String tin,
            String pagIbig,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance,
            String status,
            String immediateSupervisor,
            Float basicSalary
            )
            
        {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        }
            
      public EmployeeDetails
            (float basicSalary,
             float totalAllowances,
             float totalDeductions,
             float totalIncentives,
             float netPay
            )
        {
        this.basicSalary = basicSalary;
        this.totalAllowances = totalAllowances;
        this.totalDeductions = totalDeductions;
        this.totalIncentives = totalIncentives;
        this.netPay = netPay;
        }
            
       public EmployeeDetails (
            float overTimePay,
            float overTime,
            float holidayPay,
            float performanceBonus
            )
       {
       this.overTimePay = overTimePay;
       this.overTime = overTime;
       this.hourlyRate = hourlyRate;
       this.holidayPay = holidayPay;
       this.performanceBonus = performanceBonus;
       }
       
       public EmployeeDetails (
            String username,
            Connection connection               
       ) {
            this.userName = username;
            this.connection = connection;
       }
               

    // Getter methods

    public int getEid() {
        return eid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDesignation() {
        return designation;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getStatus() {
        return status;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public String getSss() {
        return sss;
    }

    public String getPhilHealth() {
        return philHealth;
    }

    public String getTin() {
        return tin;
    }

    public String getPagIbig() {
        return pagIbig;
    }

    public float getBasicSalary() {
        return basicSalary;
    }

    public String getRiceSubsidy() {
        return riceSubsidy;
    }

    public String getPhoneAllowance() {
        return phoneAllowance;
    }

    public String getClothingAllowance() {
        return clothingAllowance;
    }

    public float getTotalAllowances() {
        return totalAllowances;
    }
    
    public float getTotalIncentives() {
        return totalIncentives;
    }

    public float getTotalDeductions() {
        return totalDeductions;
    }

    public float getNetPay() {
        return netPay;
    }
    
    public float getOverTimePay() {
        return overTimePay;
    }
    
    public float getOverTime() {
        return overTime;
    }
    
    public float getHolidayPay() {
        return holidayPay;
    }
    
    public float getPerformanceBonus() {
        return performanceBonus;
    }
    
    public float getHourlyRate() {
        return hourlyRate;
    }
    
    public float getDaysWorked() {
        return daysWorked;
    }
    
    
    
    // Setter methods

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public void setPhilHealth(String philHealth) {
        this.philHealth = philHealth;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public void setPagIbig(String pagIbig) {
        this.pagIbig = pagIbig;
    }

    public void setBasicSalary(float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setRiceSubsidy(String riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(String phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(String clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public void setTotalAllowances(float totalAllowances) {
        this.totalAllowances = totalAllowances;
    }

    public void setTotalIncentives(float totalIncentives) {
        this.totalIncentives = totalIncentives;
    }
    
    
    public void setTotalDeductions(float totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public void setNetPay(float netPay) {
        this.netPay = netPay;
    }
    
    // Newly added setters 

    public void setOverTimePay(float overTimePay) {
        this.overTimePay = overTimePay;
    }
    
    public void setOverTime (float overTime) {
        this.overTime = overTime;
    }

    public void setHolidayPay(float holidayPay) {
        this.holidayPay = holidayPay;
    }
    
    public void setPerformanceBonus(float performanceBonus) {
        this.performanceBonus = performanceBonus;
    }
    
    public void setHourlyRate (float hourlyRate) {
        this.hourlyRate= hourlyRate;
    }
    
    public void setDaysWorked (float daysWorked) {
        this.daysWorked= daysWorked;
    }
       
    /* Loads employee data from database based on username
     * @param username The username of the logged-in user
     * @param connection Database connection
     * @return EmployeeDetails object populated with data, or null if not found
     */
    public static EmployeeDetails loadByUsername(String username, Connection connection) {
        try {
            DBQueries dbQueries = new DBQueries();
            PreparedStatement statement = connection.prepareStatement(dbQueries.getSearchUsername());
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                EmployeeDetails employee = new EmployeeDetails();
                
                // Get employee details from result set
                employee.setEid(resultSet.getInt(dbQueries.getEid()));
                employee.setFirstName(resultSet.getString(dbQueries.getFirstName()));
                employee.setLastName(resultSet.getString(dbQueries.getLastName()));
                employee.setDesignation(resultSet.getString(dbQueries.getDesignation()));
                employee.setUserName(resultSet.getString(dbQueries.getUsername()));
                employee.setAddress(resultSet.getString(dbQueries.getAddress()));
                employee.setPhoneNumber(resultSet.getString(dbQueries.getPhoneNumber()));
                employee.setBirthday(resultSet.getDate(dbQueries.getBirthDay()));
                employee.setStatus(resultSet.getString(dbQueries.getStatus()));
                
                // Get government IDs
                employee.setSss(resultSet.getString(dbQueries.getSSS()));
                employee.setPhilHealth(resultSet.getString(dbQueries.getPhilHealth()));
                employee.setTin(resultSet.getString(dbQueries.getTin()));
                employee.setPagIbig(resultSet.getString(dbQueries.getPagIbig()));
                
                // Get basic salary if available
                try {
                    employee.setBasicSalary(resultSet.getFloat(dbQueries.getBasicSalary())); //change to basic salary
                } catch (SQLException e) {
                    // Field might not be available in this query
                    employee.setBasicSalary(0);
                }
                
                // Close resources
                resultSet.close();
                statement.close();
                
                // Load allowances for this employee
                loadAllowances(employee, connection);
                loadIncentives(employee, connection);
                return employee;
            }
            
            // Close resources
            resultSet.close();
            statement.close();
            
        } catch (SQLException ex) {
            System.err.println("Database error while loading employee: " + ex.getMessage());
        }
        
        return null;
    }
    
    /**
     * Loads employee data from database based on EID
     * @param eid The employee ID as a string
     * @param connection Database connection
     * @return EmployeeDetails object populated with data, or null if not found
     */
    public static EmployeeDetails loadByEid(String eid, Connection connection) {
        try {
            DBQueries dbQueries = new DBQueries();
            PreparedStatement statement = connection.prepareStatement(dbQueries.getSearchByEID());

            // Convert String EID to integer
            int employeeId = Integer.parseInt(eid);
            statement.setInt(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                EmployeeDetails employee = new EmployeeDetails();

                // Get employee details from result set
                employee.setEid(resultSet.getInt(dbQueries.getEid()));
                employee.setFirstName(resultSet.getString(dbQueries.getFirstName()));
                employee.setLastName(resultSet.getString(dbQueries.getLastName()));
                employee.setDesignation(resultSet.getString(dbQueries.getDesignation()));
                employee.setUserName(resultSet.getString(dbQueries.getUsername()));
                employee.setAddress(resultSet.getString(dbQueries.getAddress()));
                employee.setPhoneNumber(resultSet.getString(dbQueries.getPhoneNumber()));
                employee.setBirthday(resultSet.getDate(dbQueries.getBirthDay()));
                employee.setStatus(resultSet.getString(dbQueries.getStatus()));

                // Get government IDs
                employee.setSss(resultSet.getString(dbQueries.getSSS()));
                employee.setPhilHealth(resultSet.getString(dbQueries.getPhilHealth()));
                employee.setTin(resultSet.getString(dbQueries.getTin()));
                employee.setPagIbig(resultSet.getString(dbQueries.getPagIbig()));

                // Get financial data
                try {
                    employee.setBasicSalary(resultSet.getFloat(dbQueries.getBasicSalary()));
                } catch (SQLException e) {
                    employee.setBasicSalary(0);
                }

                // Close resources
                resultSet.close();
                statement.close();

                // Load additional data
                loadAllowances(employee, connection);
                loadIncentives(employee, connection);
                return employee;
            }

            // Close resources if no results
            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            System.err.println("Database error while loading employee: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.err.println("Invalid EID format: " + ex.getMessage());
        }

        return null;
    }
    
    /**
     * Loads employee allowance data from database
     * @param employee The employee object to update with allowances
     * @param connection Database connection
     */
    private static void loadAllowances(EmployeeDetails employee, Connection connection) {
        try {
            DBQueries dbQueries = new DBQueries();
            PreparedStatement ps = connection.prepareStatement(dbQueries.getSearchAllowancebyEID());
            ps.setInt(1, employee.getEid());
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Get allowance values if available
                employee.setRiceSubsidy(rs.getString(dbQueries.getRiceAllowance()));
                employee.setPhoneAllowance(rs.getString(dbQueries.getPhoneAllowance()));
                employee.setClothingAllowance(rs.getString(dbQueries.getClothingAllowance()));
                employee.setHourlyRate(rs.getFloat(dbQueries.getHourlyRate()));
            }
            
            // Close resources
            rs.close();
            ps.close();
            
        } catch (SQLException ex) {
            System.err.println("Could not load allowance data: " + ex.getMessage());
        }
    }
    
    /**
    * Loads employee incentive data from database
    * @param employee The employee object to update with incentives
    * @param connection Database connection
    */
    private static void loadIncentives(EmployeeDetails employee, Connection connection) {
    try {
        DBQueries dbQueries = new DBQueries();
        PreparedStatement ps = connection.prepareStatement(dbQueries.getSearchIncentivesbyEID());
        ps.setInt(1, employee.getEid());
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            // Get column names from DBQueries to maintain consistency
            String overTimeColumn = dbQueries.getOverTime();
            String holidayPayColumn = dbQueries.getHolidayPay();
            String performanceBonusColumn = dbQueries.getPerformanceBonus();
            
            // Get incentive values as strings first
            String overTimeStr = rs.getString(overTimeColumn);
            String holidayPayStr = rs.getString(holidayPayColumn);
            String performanceBonusStr = rs.getString(performanceBonusColumn);
            
            // Convert strings to float after removing commas
            if (overTimeStr != null) {
                employee.setOverTime(Float.parseFloat(overTimeStr.replace(",", "")));
            }
            
            if (holidayPayStr != null) {
                employee.setHolidayPay(Float.parseFloat(holidayPayStr.replace(",", "")));
            }
            
            if (performanceBonusStr != null) {
                employee.setPerformanceBonus(Float.parseFloat(performanceBonusStr.replace(",", "")));
            }
        }
        // Close resources
        rs.close();
        ps.close();
        
    } catch (SQLException ex) {
        System.err.println("Could not load incentive data: " + ex.getMessage());
    } catch (NumberFormatException ex) {
        System.err.println("Error parsing incentive values: " + ex.getMessage());
        }
    }
    
    /**
     * Formats a date to string using the specified pattern
     * @param date The date to format
     * @param pattern The date pattern (e.g., "MM/dd/yyyy")
     * @return Formatted date string or "N/A" if date is null
     */
    public String formatDate(Date date, String pattern) {
        if (date == null) {
            return "N/A";
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    
    /**
     * Gets the full name of the employee
     * @return First and last name combined
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void saveToDatabase(Connection connection) {
        
    }

    public void setBirthday(String formattedBirthday) {
        
    }
  
}
