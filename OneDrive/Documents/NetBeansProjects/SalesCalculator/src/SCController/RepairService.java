/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCController;

/**
 *
 * @author dashcodes
 */
public class RepairService extends Sale implements Calculable {
    private double pricePerHour;
    private int numberOfHours;

    public RepairService(String itemName, double pricePerHour, int numberOfHours) {
        super(itemName);
        this.pricePerHour = pricePerHour;
        this.numberOfHours = numberOfHours;
    }

    @Override
    public double calculateTotalSales() {
        return pricePerHour * numberOfHours;
    }
}
