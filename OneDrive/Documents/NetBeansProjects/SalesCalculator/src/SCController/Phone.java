/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SCController;

/**
 *
 * @author dashcodes
 */
public class Phone extends Sale implements Calculable {
    private double price;
    private int quantitySold;

    public Phone(String itemName, double price, int quantitySold) {
        super(itemName);
        this.price = price;
        this.quantitySold = quantitySold;
    }

    @Override
    public double calculateTotalSales() {
        return price * quantitySold;
    }
}
