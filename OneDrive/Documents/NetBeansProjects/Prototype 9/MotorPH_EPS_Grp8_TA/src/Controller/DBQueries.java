/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Controller;

/**
 *
 * @author dashcodes
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBQueries {

    // Read-only query strings
    public String userName = "SELECT * FROM payrollmph WHERE username = ?";
    private final String userLogin = "SELECT * FROM payrollmph WHERE username = ? AND password = ?";
    private final String usernameExistsQuery = "SELECT COUNT(*) FROM payrollmph WHERE username = ?";
    private final String userLoginQuery = "SELECT * FROM payrollmph WHERE username = ? AND password = ?";
    public final String userDesignation = "SELECT designation FROM payrollmph WHERE username = ? AND password = ?";   
    public final String adminAccess = "SELECT * FROM payrollmph";
    public final String timesheetData = "SELECT * FROM timesheet";
    public final String incentives = "SELECT * FROM payrollmph WHERE over_Time= ?, perfomance_bonus = ?, AND holiday_pay = ?";
    public final String leaveData = "SELECT * FROM leaves";
    public final String getUserDesignationByUsername = "SELECT designation FROM payrollmph WHERE username = ?";
    
    /* CRUD OPTION + Sort
    * Create/Add/Insert
    * Read/Search/View
    * Update
    * Delete/Disable    
    */
    
    private final String getEmployeeByEid = "SELECT * FROM payrollmph WHERE EID = ?";
    public String getTimesheetByEid = "SELECT * FROM timesheet WHERE EID = ?";
    public String getLeaveByLeaveId = "SELECT * FROM leaves WHERE Leave_ID = ?";
    public String getLeaveByEid = "SELECT * FROM leaves WHERE EID = ?";
    
    public String generateNextEid = "SELECT MAX(EID) AS maxEid FROM payrollmph";
    
    private final String insertEmployee = "INSERT INTO payrollmph "
            + "(EID, Last_Name, First_Name, Birthday, Address, Phone_Number, "
            + "SSS_#, Philhealth_#, TIN_#, `Pag-ibig_#`, Status, Designation, Half_Month_Rate) " 
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
    public String insertTimeLog = "INSERT INTO timesheet "
                + "(AttendanceID, EID, LogDate, LogTime, Status)";
    
    public String insertLeave = "INSERT INTO leaves"
                + "(Leave_ID, EID, Date_Filed, Date_From, Date_To, Reason_For_Leave, Leave_Status)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private final String updateEmployee = "UPDATE payrollmph SET"
                + "Last_Name = ?,"
                + "First_Name = ?,"
                + "Address = ?,"
                + "Employee = ?,"
                + "Phone_Number = ?,"
                + "Status = ?,"
                + "Designation = ?,"
                + "Basic_Salary = ?,"
                + "WHERE EID = ?";
          
            
    public String updateTimeLog = "UPDATE timesheet SET"
                + "AttedanceID = ?,"
                + "EID = ?,"
                + "LogDate = ?,"
                + "LogTime = ?,"
                + "AttStatus = ?";
    
    final String updatePasswordQuery = "UPDATE payrollmph SET Password = ? WHERE Username = ?";
    
    public String updateLeaveStatus = "UPDATE leaves SET Leave_Status = ? WHERE Leave_ID = ?";
    
    private final String deleteEmployee = "DELETE FROM payrollmph WHERE EID = ?";
    
    public String deleteTimeLog = "DELETE FROM timesheet WHERE EID = ?";
    
    public String createSearchQuery(String columnName) {
        return "SELECT * FROM payrollmph WHERE " + columnName + " LIKE ?";
    }
    
    public String createSearchTimeLog (String columnName){
        return "SELECT * FROM timesheet WHERE " + columnName + " LIKE ?";
    }
    
    public String createSortQuery(String columnName, boolean ascending) {
        String orderDirection = ascending ? "ASC" : "DESC";
        return "SELECT * FROM payrollmph ORDER BY " + columnName + " " + orderDirection;
    }
    
    public String createSortTimeLog (String columnName, boolean ascending){
        String orderDirection = ascending ? "ASC" : "DESC";
        return "SELECT * FROM timesheet ORDER BY " + columnName + " " + orderDirection;
    }
    
    public String createSortLeave (String columnName, boolean ascending){
        String orderDirection = ascending ? "ASC" : "DESC";
        return "SELECT * FROM leaves ORDER BY " + columnName + " " + orderDirection;
    }
    
    
     // Mutable query strings
    private String insertData;
    private String returnAllTimeLog;
    private String returnAllData;
    private String updateDetails;
    private String updateDefaultPassword;
    private String updatePassword;
    private String loadData;
    private String searchEmployee;
    private String searchTimeLog;
    private String displayEmployeeInfo;
    private String searchAllowancebyEID;
    private String searchContactInformationbyEID;
    private String searchIncentivesbyEID; 
    private String searchByEID;
    private String searchUsername;
    public String userSearchByEID;
    public String DisplayLogInfoByEIDandDate;

    // Column names
    public String eid;
    public String lastName;
    public String firstName;
    public String birthDay;
    private String password;
    public String designation;
    private String address;
    private String phoneNumber;
    private String SSS;
    private String philHealth;
    private String tin;
    private String pagIbig;
    public String status;
    private String immediateSupervisor;
    private String basicSalary;
    private String halfMonthRate;
    private String riceAllowance;
    private String phoneAllowance;
    private String clothingAllowance;
    private String hourlyRate;
    public String logDate;
    public String logTime;
    public String attendanceID;
    public String attStatus;
    public String overTime;
    public String performanceBonus;
    public String holidayPay;
    public String leaveID;
    public String dateFrom;
    public String dateTo;
    public String reasonForLeave;
    public String leaveStatus;
    

    public DBQueries() {
        initializeQueries();
        initializeColumnNames();
    }
    
    // 
    private void initializeQueries() {
        this.insertData = "INSERT INTO payrollmph"
                + "(Last_Name,"
                + "First_Name,"
                + "Birthday,"
                + "Username,"
                + "Password,"
                + "Designation,"
                + "Address,"
                + "Phone_Number,"
                + "SSS_#,"
                + "Philhealth_#,"
                + "TIN_#,"
                + "Pag-ibig_#,"
                + "Status,"
                + "Immediate_Supervisor,"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
        
        this.insertTimeLog = "INSERT INTO timesheet"
                + "AttendanceID,"
                + "EID,"
                + "LogDate,"
                + "LogTime,"
                + "Status";

        this.returnAllData = "SELECT * FROM payrollmph WHERE"
                + "Last_Name = ?,"
                + "First_Name = ?,"
                + "Birthday = ?,"
                + "Username = ?,"
                + "Password = ?,"
                + "Designation = ?"
                + "WHERE ID = ?";
        
        this.returnAllTimeLog = "SELECT * FROM timesheet WHERE"
                + "AttedanceID = ?,"
                + "EID = ?,"
                + "LogDate = ?,"
                + "LogTime = ?,"
                + "AttStatus = ?";

        this.updateDetails = "UPDATE payrollmph SET"
                + "Last_Name = ?,"
                + "First_Name = ?,"
                + "Birthday = ?,"
                + "Username = ?,"
                + "Designation = ?,"
                + "Address = ?,"
                + "Phone_Number = ?,"
                + "SSS_# = ?,"
                + "Philhealth_# = ?,"
                + "TIN_# = ?,"
                + "Pag-ibig_# = ?,"
                + "Status = ?,"
                + "Immediate_Supervisor = ? WHERE EID = ?";
        
        this.updateTimeLog = "UPDATE timesheet SET"
                + "AttedanceID = ?,"
                + "EID = ?,"
                + "LogDate = ?,"
                + "LogTime = ?,"
                + "AttStatus = ?";

        this.updateDefaultPassword = "UPDATE payrollmph SET"
                + "Password = ?,"
                + "WHERE EID = ?";
        
        this.updatePassword = "UPDATE payrollmph SET"
                + "Password = ? WHERE Username = ?";

        this.loadData = "SELECT * FROM payrollmph";
        
        this.searchEmployee = "SELECT * FROM payrollmph "
                + "WHERE EID = ? OR Last_Name "
                + "LIKE ? OR First_Name LIKE ?";
        
        this.searchTimeLog = "SELECT * FROM timesheet WHERE"
                + "AttedanceID = ?,"
                + "EID = ?,"
                + "LogDate = ?,"
                + "LogTime = ?,"
                + "AttStatus = ?";

        this.displayEmployeeInfo = "SELECT SSS_#,"
                + "Philhealth_#,"
                + "Pag-ibig_#,"
                + "TIN_# FROM payrollmph WHERE EID = ?";

        this.searchAllowancebyEID = "SELECT EID, "
        + "Basic_Salary, "
        + "Rice_Subsidy, "
        + "Phone_Allowance, "
        + "Clothing_Allowance, "
        + "Hourly_Rate, "  
        + "Half_Month_Rate "  
        + "FROM payrollmph WHERE EID = ?";
        
        this.searchIncentivesbyEID = "SELECT EID, "
        + "Over_Time, "
        + "Holiday_Pay, "
        + "Performance_Bonus "
        + "FROM payrollmph WHERE EID = ?";
        
        this.searchContactInformationbyEID = "SELECT EID, "
        + "Phone_Number, "
        + "Username, "
        + "Address, "
        + "Birthday "
        + "FROM payrollmph WHERE EID = ?";
        
        this.searchByEID = "SELECT * FROM payrollmph WHERE EID = ? ";
        this.searchUsername = "SELECT * FROM payrollmph WHERE Username = ?";
        
        this.userSearchByEID = "SELECT EID,"
                + "Last_Name,"
                + "First_Name,"
                + "Birthday,"
                + "Username,"
                + "Designation FROM payrollmph WHERE ID = ? ORDER BY ID ASC LIMIT 1";

        this.DisplayLogInfoByEIDandDate = "SELECT EID,"
                + "EID,"
                + "LogDate,"
                + "LogTime,"
                + "AttStatus FROM attendance WHERE ID = ? AND LogDate BETWEEN ? AND ?";
        
        this.updateLeaveStatus = "UPDATE leaves SET Leave_Status = ?";
    }

    private void initializeColumnNames() {
        this.eid = "EID";
        this.lastName = "Last_Name";
        this.firstName = "First_Name";
        this.birthDay = "Birthday";
        this.password = "Password";
        this.userName = "Username";
        this.designation = "Designation";
        this.address = "Address";
        this.phoneNumber = "Phone_Number";
        this.SSS = "SSS_#";
        this.philHealth = "Philhealth_#";
        this.tin = "TIN_#";
        this.pagIbig = "Pag-ibig_#";
        this.status = "Status";
        this.immediateSupervisor = "Immediate_Supervisor";
        this.basicSalary = "Basic_Salary";
        this.halfMonthRate = "Half_Month_Rate";
        this.riceAllowance = "Rice_Subsidy";
        this.phoneAllowance = "Phone_Allowance";
        this.clothingAllowance = "Clothing_Allowance";
        this.hourlyRate = "Hourly_Rate";
        this.logDate = "LogDate";
        this.logTime = "LogTime";
        this.attStatus = "AttStatus";
        this.performanceBonus = "Performance_Bonus";
        this.holidayPay = "Holiday_Pay";
        this.overTime = "Over_Time";
        this.leaveID = "Leave_ID";
        this.leaveStatus = "Leave_Status";
        this.dateFrom = "Date_From";
        this.dateTo = "Date_To";
        this.reasonForLeave = "Reason_For_Leave";
    }
    
    
    

    // Helper method to convert date string to java.sql.Date
    public java.sql.Date convertStringToSqlDate(String dateString) {
    try {
        // First, try to parse in MM/dd/yyyy format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        Date parsedDate = dateFormat.parse(dateString);
        return new java.sql.Date(parsedDate.getTime());
    } catch (ParseException e) {
        try {
            // If that fails, try yyyy-MM-dd format
            SimpleDateFormat altFormat = new SimpleDateFormat("yyyy-MM-dd");
            altFormat.setLenient(false);
            Date parsedDate = altFormat.parse(dateString);
            return new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e2) {
            // If both formats fail, throw an exception with a clear message
            throw new IllegalArgumentException("Invalid date format. Please use MM/dd/yyyy or yyyy-MM-dd format. Input was: " + dateString);
            }
        }
    }

    // Getter methods
    public String getUsername() {
        return userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getInsertData() {
        return insertData;
    }

    public String getReturnAllData() {
        return returnAllData;
    }

    public String getUpdateDetails() {
        return updateDetails;
    }

    public String getUpdateDefaultPassword() {
        return updateDefaultPassword;
    }

    public String getUpdatePassword() {
        return updatePassword;
    }

    public String getLoadData() {
        return loadData;
    }

    public String getSearchEmployee() {
        return searchEmployee;
    }

    public String getDisplayEmployeeInfo() {
        return displayEmployeeInfo;
    }

    public String getSearchAllowancebyEID() {
        return searchAllowancebyEID;
    }
    
    public String getSearchIncentivesbyEID() {
        return searchIncentivesbyEID;
    }

    public String getSearchByEID() {
        return searchByEID;
    }

    public String getSearchUsername() {
        return searchUsername;
    }

    public String getUserSearchByEID() {
        return userSearchByEID;
    }

    public String getDisplayLogInfoByEIDandDate() {
        return DisplayLogInfoByEIDandDate;
    }

    public String getEid() {
        return eid;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBirthDay() {
        return birthDay;
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

    public String getSSS() {
        return SSS;
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

    public String getStatus() {
        return status;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public String getBasicSalary() {
        return basicSalary;
    }
    
    public String getHalfMonthRate() {
        return halfMonthRate;
    }

    public String getRiceAllowance() {
        return riceAllowance;
    }

    public String getPhoneAllowance() {
        return phoneAllowance;
    }

    public String getClothingAllowance() {
        return clothingAllowance;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getUsernameExistsQuery() {
        return usernameExistsQuery;
    }

    public String getUserLoginQuery() {
        return userLoginQuery;
    }

    public String getUpdatePasswordQuery() {
        return updatePasswordQuery;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public String getAdminAccess() {
        return adminAccess;
    }

    public String getGetEmployeeByEid() {
        return getEmployeeByEid;
    }

    public String getInsertEmployee() {
        return "INSERT INTO payrollmph (EID, Last_Name, First_Name, Birthday, Address, Phone_Number, " +
            "SSS_#, PhilHealth_#, TIN_#, `Pag-ibig_#`, Status, Designation, Half_Month_Rate) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public String getUpdateEmployee() {
        return updateEmployee;
    }

    public String getDeleteEmployee() {
        return deleteEmployee;
    }

    public String getTimesheetData() {
        return timesheetData;
    }

    public String getGetTimesheetByEid() {
        return getTimesheetByEid;
    }

    public String getInsertTimeLog() {
        return insertTimeLog;
    }

    public String getUpdateTimeLog() {
        return updateTimeLog;
    }

    public String getDeleteTimeLog() {
        return deleteTimeLog;
    }

    public String getReturnAllTimeLog() {
        return returnAllTimeLog;
    }

    public String getSearchTimeLog() {
        return searchTimeLog;
    }

    public String getAttendanceID() {
        return "AttendanceID";
    }

    public String getAttStatus() {
        return attStatus;
    }

    public String getIncentives() {
        return incentives;
    }

    public String getOverTime() {
        return overTime;
    }

    public String getPerformanceBonus() {
        return performanceBonus;
    }

    public String getHolidayPay() {
        return holidayPay;
    }

    public String getLeaveData() {
        return leaveData;
    }

    public String getGetLeaveByLeaveId() {
        return getLeaveByLeaveId;
    }

    public String getUpdateLeaveStatus() {
        return updateLeaveStatus;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getReasonForLeave() {
        return reasonForLeave;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public String getGetLeaveByEid() {
        return getLeaveByEid;
    }

    public String getInsertLeave() {
        return insertLeave;
    }
    
    public String getAttendanceIDColumn(){
        return "AttendanceID";
    }
    
    public String getEidColumn() {
        return "EID";
    }    

    public String getGetUserDesignationByUsername() {
        return getUserDesignationByUsername;
    }
    
    
    
    //Setter Methods

    public void setInsertData(String insertData) {
        this.insertData = insertData;
    }

    public void setReturnAllData(String returnAllData) {
        this.returnAllData = returnAllData;
    }

    public void setUpdateDetails(String updateDetails) {
        this.updateDetails = updateDetails;
    }

    public void setUpdateDefaultPassword(String updateDefaultPassword) {
        this.updateDefaultPassword = updateDefaultPassword;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public void setLoadData(String loadData) {
        this.loadData = loadData;
    }

    public void setSearchEmployee(String searchEmployee) {
        this.searchEmployee = searchEmployee;
    }

    public void setDisplayEmployeeInfo(String displayEmployeeInfo) {
        this.displayEmployeeInfo = displayEmployeeInfo;
    }

    public void setSearchAllowancebyEID(String searchAllowancebyEID) {
        this.searchAllowancebyEID = searchAllowancebyEID;
    }
    
    public void setSearchIncentivesbyEID(String searchIncentivesbyEID) {
        this.searchIncentivesbyEID = searchIncentivesbyEID;
    }

    public void setSearchByEID(String searchByEID) {
        this.searchByEID = searchByEID;
    }

    public void setSearchUsername(String searchUsername) {
        this.searchUsername = searchUsername;
    }

    public void setUserSearchByEID(String userSearchByEID) {
        this.userSearchByEID = userSearchByEID;
    }

    public void setDisplayLogInfoByEIDandDate(String DisplayLogInfoByEIDandDate) {
        this.DisplayLogInfoByEIDandDate = DisplayLogInfoByEIDandDate;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
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

    public void setSSS(String SSS) {
        this.SSS = SSS;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }

    public void setBasicSalary (String basicSalary) {
        this.basicSalary = basicSalary;
    }
    
    public void setHalfMonthRate(String halfMonthRate) {
        this.halfMonthRate = halfMonthRate;
    }

    public void setRiceAllowance(String riceAllowance) {
        this.riceAllowance = riceAllowance;
    }

    public void setPhoneAllowance(String phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(String clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGetTimesheetByEid(String getTimesheetByEid) {
        this.getTimesheetByEid = getTimesheetByEid;
    }

    public void setInsertTimeLog(String insertTimeLog) {
        this.insertTimeLog = insertTimeLog;
    }

    public void setUpdateTimeLog(String updateTimeLog) {
        this.updateTimeLog = updateTimeLog;
    }

    public void setDeleteTimeLog(String deleteTimeLog) {
        this.deleteTimeLog = deleteTimeLog;
    }

    public void setReturnAllTimeLog(String returnAllTimeLog) {
        this.returnAllTimeLog = returnAllTimeLog;
    }

    public void setSearchTimeLog(String searchTimeLog) {
        this.searchTimeLog = searchTimeLog;
    }

    public void setAttendanceID(String attendanceID) {
        this.attendanceID = attendanceID;
    }

    public void setAttStatus(String attStatus) {
        this.attStatus = attStatus;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public void setPerformanceBonus(String performanceBonus) {
        this.performanceBonus = performanceBonus;
    }

    public void setHolidayPay(String holidayPay) {
        this.holidayPay = holidayPay;
    }

    public void setGetLeaveByLeaveId(String getLeaveByLeaveId) {
        this.getLeaveByLeaveId = getLeaveByLeaveId;
    }

    public void setUpdateLeaveStatus(String updateLeaveStatus) {
        this.updateLeaveStatus = updateLeaveStatus;
    }

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setReasonForLeave(String reasonForLeave) {
        this.reasonForLeave = reasonForLeave;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public void setGetLeaveByEid(String getLeaveByEid) {
        this.getLeaveByEid = getLeaveByEid;
    }

    public void setInsertLeave(String insertLeave) {
        this.insertLeave = insertLeave;
    }
    
}

    