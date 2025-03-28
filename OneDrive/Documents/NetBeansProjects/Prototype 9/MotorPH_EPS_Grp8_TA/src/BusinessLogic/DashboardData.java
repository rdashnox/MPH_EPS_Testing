/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package BusinessLogic;

/**
 *
 * @author dashcodes
 */
import Controller.SalaryCalculator;
import java.util.HashMap;
import java.util.Map;

public class DashboardData {
    private Map<String, String> displayData = new HashMap<>();
        
    /**
     * Processes employee details to generate dashboard display data
     * @param employee The employee details to process
     */
    public void processEmployeeData(EmployeeDetails employee) {
        // Store basic information for display
        displayData.put("firstName", employee.getFirstName());
        displayData.put("lastName", employee.getLastName());
        displayData.put("designation", employee.getDesignation());
        displayData.put("birthday", employee.formatDate(employee.getBirthday(), "MM/dd/yyyy"));
        displayData.put("address", employee.getAddress());
        displayData.put("phoneNumber", employee.getPhoneNumber());
        displayData.put("status", employee.getStatus());
        
        // Store government IDs
        displayData.put("sssNumber", employee.getSss());
        displayData.put("philHealthNumber", employee.getPhilHealth());
        displayData.put("tinNumber", employee.getTin());
        displayData.put("pagIbigNumber", employee.getPagIbig());
        
        // Format salary-related information
        displayData.put("basicSalary", formatCurrency(employee.getBasicSalary()));
    }
    
    /**
     * Processes allowance data for display
     * @param employee The employee with allowance data
     */
    public void processAllowanceData(EmployeeDetails employee) {
        // Format allowances for display
        displayData.put("riceSubsidy", formatCurrency(parseFloatSafely(employee.getRiceSubsidy())));
        displayData.put("phoneAllowance", formatCurrency(parseFloatSafely(employee.getPhoneAllowance())));
        displayData.put("clothingAllowance", formatCurrency(parseFloatSafely(employee.getClothingAllowance())));
        displayData.put("totalAllowances", formatCurrency(employee.getTotalAllowances()));
        displayData.put("hourlyRate", formatCurrency(employee.getHourlyRate()));
    }
    
    /**
     * Processes deduction data for display
     * @param employee The employee with deduction data
     * @param calculator The salary calculator with deduction information
     */
    public void processDeductionData(EmployeeDetails employee, SalaryCalculator calculator) {
        // Format deductions for display
        displayData.put("sssDeduction", formatCurrency(calculator.getDeduction("sss")));
        displayData.put("philHealthDeduction", formatCurrency(calculator.getDeduction("philhealth")));
        displayData.put("pagIbigDeduction", formatCurrency(calculator.getDeduction("pagibig")));
        displayData.put("taxDeduction", formatCurrency(calculator.getDeduction("tax")));
        displayData.put("totalDeductions", formatCurrency(employee.getTotalDeductions()));
    }
    
    /**
     * Processes salary calculation results for display
     * @param employee The employee with calculation results
     * @param calculator The salary calculator with calculation results
     */
    public void processSalaryData(EmployeeDetails employee, SalaryCalculator calculator) {
        // Format gross pay, allowances, deductions, and net pay
        displayData.put("grossPay", formatCurrency(employee.getBasicSalary()));
        displayData.put("overTimePay", formatCurrency(employee.getOverTimePay()));
        displayData.put("holidayPay", formatCurrency(employee.getHolidayPay()));
        displayData.put("performanceBonus", formatCurrency(employee.getPerformanceBonus()));
        displayData.put("totalGrossPay", formatCurrency(calculator.getGrossPay()));
        displayData.put("netPay", formatCurrency(employee.getNetPay()));
        displayData.put("overTime", String.valueOf(employee.getOverTime()));
    }
    
    /**
     * Gets a display value by key
     * @param key The key for the display value
     * @return The formatted display value
     */
    public String getDisplayValue(String key) {
        return displayData.getOrDefault(key, "N/A");
    }
    
    /**
     * Formats a float value as currency with the peso sign (₱)
     * @param value The value to format
     * @return Formatted currency string
     */
    private String formatCurrency(float value) {
        if (value <= 0) {
            return "N/A";
        }
        return String.format("₱%.2f", value);
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
    
    /**
     * Clears all display data
     */
    public void clearDisplayData() {
        displayData.clear();
    }
    
    /**
     * Gets all display data as an unmodifiable map
     * @return Map of all display data
     */
    public Map<String, String> getAllDisplayData() {
        return Map.copyOf(displayData);
    }
}