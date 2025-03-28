package Controller;

import BusinessLogic.Allowances;
import BusinessLogic.Deductions;
import BusinessLogic.Incentives;
import BusinessLogic.EmployeeDetails;
import BusinessLogic.DashboardData;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryCalculator {
    // Constants for deduction rates
    private static final float SSS_RATE = 0.0363f;        // 3.63% SSS contribution
    private static final float PHILHEALTH_RATE = 0.035f;  // 3.5% PhilHealth
    private static final float PAGIBIG_RATE = 0.02f;      // 2% Pag-IBIG
    private static final float TAX_RATE = 0.15f;          // 15% tax (simplified)
    private static final float OVERTIME_RATE = 1.25f;     // 25% additional for overtime
    private static final int WORKING_DAYS = 26;           // Standard working days per month
    
    private Map<String, Float> deductions = new HashMap<>();
    private Map<String, Float> incentives = new HashMap<>();
    private Map<String, Float> formattedDeductions = new HashMap<>();
    private float grossPay;
    private float overTimePay;
    private float holidayPay;
    private float totalAllowances;
    private float totalDeductions;
    private float totalIncentives;
    private float netPay;
    
    // Establish Connection
    private Connection connection;
    
    public SalaryCalculator() {
       this.grossPay = 0;
       this.overTimePay = 0;
       this.holidayPay = 0;
       this.totalAllowances = 0;
       this.totalDeductions = 0;
       this.totalIncentives = 0;
       this.netPay = 0;
    }
  
    
    public SalaryCalculator(Connection connection) {
        this();
        this.connection = connection;
    }
    
    /**
     * Formats Currency values
     */
    private String formatCurrency(float value){
        return String.format("₱%.2f", value);
    }
    
    public String getFormattedDeduction(String key) {
    if (deductions.containsKey(key)) {
        Float rawValue = deductions.get(key);  
        return rawValue != null ? formatCurrency(rawValue) : "₱0.00";
    }
    return "₱0.00";
    }

    /**
     * Gets allowances for an employee by employee ID
     * @param eid
     * @return 
     * @throws java.sql.SQLException
     */
    public Allowances getAllowancesForEmployee(int eid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM allowances WHERE EID = ?")) {
            ps.setInt(1, eid);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Allowances(
                    rs.getInt("EID"),
                    rs.getFloat("Basic_Salary"),
                    rs.getFloat("Rice_Allowance"),
                    rs.getFloat("Phone_Allowance"),
                    rs.getFloat("Clothing_Allowance"),
                    rs.getFloat("Half_Month_Rate"),
                    rs.getFloat("Hourly_Rate")
                );
            }
        }
        return null;
    }
    
    /**
     * Gets deductions for an employee by employee ID
     * @param eid
     * @return 
     * @throws java.sql.SQLException 
     */
    public Deductions getDeductionsForEmployee(int eid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM deductions WHERE EID = ?")) {
            ps.setInt(1, eid);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Deductions(
                    rs.getInt("EID"),
                    rs.getFloat("Basic_Salary"),
                    rs.getFloat("Pag_Ibig_Contribution"),
                    rs.getFloat("PhilHealth_Contribution"),
                    rs.getFloat("SSS_Contribution"),
                    rs.getFloat("Withholding_Tax"),
                    rs.getFloat("Total_Deductions")
                );
            }
        }
        return null;
    }
    
    /**
     * Gets incentives for an employee by employee ID
     * @param eid
     * @return 
     * @throws java.sql.SQLException 
     */
    public Incentives getIncentivesforEmployee(int eid) throws SQLException {
        DBQueries dbQueries = new DBQueries();
    try (PreparedStatement ps = connection.prepareStatement(dbQueries.getSearchIncentivesbyEID())) {
        ps.setInt(1, eid);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            // Get column names from DBQueries
            String overTimeColumn = dbQueries.getOverTime();
            String holidayPayColumn = dbQueries.getHolidayPay();
            String performanceBonusColumn = dbQueries.getPerformanceBonus();
            
            // Get values as strings
            String overTimeStr = rs.getString(overTimeColumn);
            String performanceBonusStr = rs.getString(performanceBonusColumn);
            String holidayPayStr = rs.getString(holidayPayColumn);
            
            // Parse the values after removing commas
            float overTime = overTimeStr != null ? Float.parseFloat(overTimeStr.replace(",", "")) : 0;
            float performanceBonus = performanceBonusStr != null ? Float.parseFloat(performanceBonusStr.replace(",", "")) : 0;
            float holidayPay = holidayPayStr != null ? Float.parseFloat(holidayPayStr.replace(",", "")) : 0;
            
            return new Incentives(
                rs.getInt("EID"),
                overTime,
                performanceBonus,
                holidayPay
            );
        }
    } catch (NumberFormatException ex) {
        System.err.println("Error parsing incentive values: " + ex.getMessage());
        ex.printStackTrace();
    }
    return null;
    }
    
    /** 
     * Calculates the gross pay for an employee
     * 
     * @param employee
     */
    public void calculateGrossPay(EmployeeDetails employee) {
        // Calculate Gross Pay as sum of basic pay, overtime, holiday pay, and bonuses. 
        float basicSalary = employee.getBasicSalary();

        // To sum up all components 
        this.grossPay = basicSalary; 
        
        // Used to update the employee object
        employee.setBasicSalary(basicSalary);
    }
    
    /**
     * Calculates total allowances from an Allowances object
     * @param allowances 
     * @return 
     */ 
    public float calculateTotalAllowances(Allowances allowances) {
        return allowances.getRiceAllowance() + 
               allowances.getPhoneAllowance() + 
               allowances.getClothingAllowance();
    }
    
    /**
     * Calculates the total allowances for an employee
     * 
     * @param employee The employee details
     */
    public void calculateAllowances(EmployeeDetails employee) {
        // Parse allowances from the employee object
        float riceAllowance = parseFloatSafely(employee.getRiceSubsidy());
        float phoneAllowance = parseFloatSafely(employee.getPhoneAllowance());
        float clothingAllowance = parseFloatSafely(employee.getClothingAllowance());
        
        // Calculate total allowances
        this.totalAllowances = riceAllowance + phoneAllowance + clothingAllowance;
        
        // Update the employee object
        employee.setTotalAllowances(this.totalAllowances);
    }
    
    /**
     * Calculates deductions based on employee's basic salary with detailed calculations
     * @param employee The employee details
     */
    public void calculateDeductions(EmployeeDetails employee) {
    // Calculates standard government deductions
    float basicSalary = employee.getBasicSalary();
    
    // Individual Calculations
    float sssContribution = basicSalary * SSS_RATE;
    float philHealthContribution = basicSalary * PHILHEALTH_RATE;
    float pagIbigContribution = basicSalary * PAGIBIG_RATE;
    float withholdingTax = calculateWithholdingTax(
        basicSalary,    
        sssContribution, 
        philHealthContribution, 
        pagIbigContribution);
    
    // Store individual deductions in the map
    deductions.put("sss", sssContribution);
    deductions.put("philhealth", philHealthContribution);
    deductions.put("pagibig", pagIbigContribution);
    deductions.put("tax", withholdingTax);
    
    // Calculates total deductions
    this.totalDeductions = 
            sssContribution + 
            philHealthContribution + 
            pagIbigContribution + 
            withholdingTax;

    // Update the employee object
    employee.setTotalDeductions(this.totalDeductions);
}
    
    
    /**
    * Calculates the incentives for an employee
    * 
    * @param employee The employee details
    */
    public void calculateIncentives(EmployeeDetails employee) {
    // Calculate overtime pay: (basicSalary / 26 days) * overtime hours * 1.25
    float basicSalary = employee.getBasicSalary();
    float dailyRate = basicSalary / WORKING_DAYS;
    float overtimeHours = employee.getOverTime();
    float overTimePay = dailyRate * overtimeHours * OVERTIME_RATE;
    
    // Get performance bonus
    float performanceBonus = employee.getPerformanceBonus();
    
    // Calculate holiday pay as daily rate × 2
    float holidayPay = dailyRate * 2;
    
    // Update overtime pay property
    this.overTimePay = overTimePay;
    this.holidayPay = holidayPay;
    
    // Stores individual incentives in the map
    incentives.put("overtimepay", overTimePay);
    incentives.put("holidaypay", holidayPay);
    incentives.put("performancebonus", performanceBonus);
    
    // Calculates total incentives
    this.totalIncentives = overTimePay + performanceBonus + holidayPay;
    
    // Update the employee object for use in other calculations
    employee.setOverTimePay(overTimePay);
    employee.setPerformanceBonus(performanceBonus);
    employee.setHolidayPay(holidayPay);
}
    
    /**
     * Calculates the net pay based on gross pay, allowances, and deductions
     * @param employee The employee details
     */
    public void calculateNetPay(EmployeeDetails employee) {
        // Validate that gross pay has been calculated
        if (this.grossPay == 0) {
            // Calculate gross pay if it hasn't been done already
            calculateGrossPay(employee);
        }
        
        // Parse allowances again to ensure we're using the latest values
        float riceSubsidy = parseFloatSafely(employee.getRiceSubsidy());
        float phoneAllowance = parseFloatSafely(employee.getPhoneAllowance());
        float clothingAllowance = parseFloatSafely(employee.getClothingAllowance());
        float calculatedAllowances = riceSubsidy + phoneAllowance + clothingAllowance;
        
        // Always calculate net pay based on gross pay
        this.netPay = this.grossPay + this.totalIncentives + calculatedAllowances - this.totalDeductions;
        
        // Update the employee object
        employee.setNetPay(this.netPay);
    }
    
    /**
     * Performs all necessary calculations for the employee
     * @param employee The employee details
     */
    public void performAllCalculations(EmployeeDetails employee) {
        calculateGrossPay(employee);
        calculateAllowances(employee);
        calculateIncentives(employee);
        calculateDeductions(employee);
        calculateNetPay(employee);
    }
    
    /**
     * Gets the raw numeric value of a deduction value
     * @param key The deduction key
     * @return Deduction amount as float
     */
    public float getDeduction(String key) {
        return deductions.getOrDefault(key, 0f);
    }
    
    /**
     * Gets the raw numeric value of a incentive value
     * @param key The deduction key
     * @return Deduction amount as float
     */
    public float getIncentive(String key) {
        return incentives.getOrDefault(key, 0f);
    }
    
    /**
     * Gets all deductions as an unmodifiable map
     * @return Map of all deductions
     */
    public Map<String, Float> getAllDeductions() {
        return Map.copyOf(deductions);
    }
    
    /**
     * Gets all incentives as an unmodifiable map
     * @return Map of all incentives
     */
    public Map<String, Float> getAllIncentives() {
        return Map.copyOf(incentives);
    }
    
    // Helper method for withholding tax calculation
    private float calculateWithholdingTax(
            float salary, 
            float sss, 
            float philHealth, 
            float pagIbig
    ) 
    {
        // Use simplified calculation with the TAX_RATE constant
        float taxableIncome = salary - sss - philHealth - pagIbig;
        
        // Apply the simple tax rate instead of the tax bracket calculations
        return taxableIncome * TAX_RATE;
    }
    
    /**
     * Safely parses a string to float, returning 0 if parsing fails
     */
    private float parseFloatSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    // Getter methods
    
    /**
     * Gets the gross pay amount
     * @return Gross pay
     */
    public float getGrossPay() {
        return grossPay;
    }
    
    /**
     * Gets the total allowances
     * @return Total allowances
     */
    public float getTotalAllowances() {
        return totalAllowances;
    }
    
    /**
     * Gets the total allowances
     * @return Total allowances
     */
    public float getTotalIncentives() {
        return totalIncentives;
    }
    
    /**
     * Gets the Overtime pay
     * @return Overtime Pay
     */
    public float getOverTimePay() {
        return overTimePay;
    }
    
    /**
     * Gets the Holiday Pay
     * @return Overtime Pay
     */
    public float getHolidayPay() {
        return holidayPay;
    }
    
    /**
     * Gets the total deductions amount
     * @return Total deductions
     */
    public float getTotalDeductions() {
        return totalDeductions;
    }
    
    /**
     * Gets the net pay amount
     * @return Net pay
     */
    public float getNetPay() {
        return netPay;
    }
    
    /**
     * Resets all calculation values
     */
    public void resetCalculations() {
        this.grossPay = 0;
        this.overTimePay = 0;
        this.holidayPay = 0;
        this.totalAllowances = 0;
        this.totalDeductions = 0;
        this.totalIncentives = 0;
        this.netPay = 0;
        this.deductions.clear();
        this.incentives.clear();
    }
    
}