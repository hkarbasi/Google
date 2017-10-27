/*
Name: Abbas Zaghari
Date: 12/10/2016
IT-106 002
Assignment 10

--------------------
when this program is run the program will ask for the name, current price, and year of the product.
All this information will be saved in separate arrays inside a loop.
The program will then validate the number of products and calculates the average price.
After finding the average, it will then calculate the new price for each product even
though they have not changed. If the price is less than the average then they will be increased accordingly.
After calculations and determinations, a report will be printed which includes
the name, current price, new price, numOfProductsWithPriceIncrease, nameOfHighestNewPrice, priceOfHighestNewPrice.

*/

import javax.swing.*;
import static javax.swing.JOptionPane.showInputDialog;


public class GroceryStore {


    public static void main(String[] args) {
        int numOfProducts = getNumOfProducts();
        String[] name = new String[numOfProducts];
        double[] currentPrice = new double[numOfProducts];
        double[] newPrice = new double[numOfProducts];
        int[] year = new int[numOfProducts];
        int numOfProductsWithPriceIncrease = 0;
        String nameOfHighestNewPrice = "";
        double priceOfHighestNewPrice = 0;

        for (int i = 0; i < numOfProducts; i++) {
            name[i] = getName();
            currentPrice[i] = getCurrentPrice();
            year[i] = getYear();

        }

        double average = getAverage(currentPrice, numOfProducts);


        for (int i = 0; i < 1; i++) {
            double priceIncrease = getPriceIncrease(currentPrice[i], year[i], average);
            newPrice[i] = currentPrice[i] + currentPrice[i] * priceIncrease;
            if (priceIncrease > 0) {
                numOfProductsWithPriceIncrease = numOfProductsWithPriceIncrease + 1;
                if (priceOfHighestNewPrice < newPrice[i]) {
                    priceOfHighestNewPrice = newPrice[i];
                    nameOfHighestNewPrice = name[i];
                }
            }
        }
        displayReport(name, currentPrice, newPrice, year, numOfProductsWithPriceIncrease, nameOfHighestNewPrice, priceOfHighestNewPrice, numOfProducts);
    }


    private static int getNumOfProducts() {

        int numOfProducts = 0;
        boolean string = false;

        do {
            try {
                numOfProducts = Integer.parseInt(showInputDialog(null, "How many products are there in the store?"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Number of products should be numeric!");
                string = true;
            }
            if (!string && (numOfProducts <= 0 || numOfProducts >= 98))
                JOptionPane.showMessageDialog(null, "Invalid product number! (between 1 and 98)");
            string = false;
        } while (numOfProducts <= 0 || numOfProducts >= 98);

        return numOfProducts;
    }

    private static String getName() {
        String name = "$";
        do {
            name = showInputDialog(null, "What is the name of the product?");

            if (name.equals(""))
                JOptionPane.showMessageDialog(null, "Name cannot be empty!");
        } while (name.equals(""));
        return name;
    }

    private static double getCurrentPrice() {
        double currentPrice = 0;
        boolean string = false;

        do {
            try {
                currentPrice = Double.parseDouble(showInputDialog(null, "What is the current price of the product?"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Price of products should be numeric!");
                string = true;
            }
            if (!string && currentPrice <= 0)
                JOptionPane.showMessageDialog(null, "Product price should be positive!");
            string = false;
        } while (currentPrice <= 0);

        return currentPrice;
    }

    private static int getYear() {

        int year = 0;
        boolean string = false;
        do {
            try {
                year = Integer.parseInt(showInputDialog("What is the year of the product?"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Year should be numeric!");
                string = true;
            }
            if (!string && year <= 1900)
                JOptionPane.showMessageDialog(null, "Year should be greater than 1900!");
            string = false;

        } while (year <= 1900);

        return year;
    }

    private static double getAverage(double[] currentPrice, int numOfProducts) {

        double sum = 0;
        for (int i = 0; i < numOfProducts; i++) {
            sum += currentPrice[i];
        }
        if (numOfProducts == 0)
            return 0;
        return sum / numOfProducts;
    }

    private static double getPriceIncrease(double price, int year, double average) {
        if (price >= average)
            return 0;
        if (year < 1)
            return 0.039;
        if (year < 4)
            return 0.024;
        if (year < 10)
            return 0.018;
        return 0.002;
    }

    private static void displayReport(String[] name, double[] currentPrice, double[] newPrice, int[] year, int numOfProductsWithPriceIncrease, String nameOfHighestNewPrice, double priceOfHighestNewPrice, int numOfProducts) {
        String report = "";
        for (int i = 0; i < numOfProducts; i++) {
            report += "Name " + (i + 1) + " = " + name[i] + "\t";
            report += "Current Price " + (i + 1) + " = " + currentPrice[i] + "\t";
            report += "New Price " + (i + 1) + " = " + newPrice[i] + "\n";
        }

        report += "\nNumber of Products whose price have increased is " + numOfProductsWithPriceIncrease;
        report += "\nName Of The Highest New Price is " + nameOfHighestNewPrice;
        report += "\nPrice Of The Highest New Price is " + priceOfHighestNewPrice;

        JOptionPane.showMessageDialog(null, new JTextArea(report));
    }
}