import javax.swing.*;

/**
 * Created by habib on 12/13/16.
 */
public class Sorting {
    public static void main(String[] args) {
        int[] numbers = {10, 4, 6, 2, 3, 16};
        int[] sortedNumbers;
        sortedNumbers = sortAscending(numbers);
        printArray(sortedNumbers);
        sortedNumbers = sortDescending(numbers);
        printArray(sortedNumbers);
    }

    public static int[] sortAscending(int[] numbers){
        int[] sortedNumbers = new int[numbers.length];
        for (int i = 0; i < sortedNumbers.length; i++) {
            int lowest = findLowest(numbers);
            numbers = dropping(numbers, lowest);
            sortedNumbers[i] = lowest;
        }
        return sortedNumbers;
    }

    public static int[] sortDescending(int[] numbers){
        int[] sortedNumbers = new int[numbers.length];
        for (int i = 0; i < sortedNumbers.length; i++) {
            int highest = findHighest(numbers);
            numbers = dropping(numbers, highest);
            sortedNumbers[i] = highest;
        }
        return sortedNumbers;

    }

    public static int[] dropping(int[] numbers, int number){
        int[] remainingNumbers = new int[numbers.length - 1];
        int j = 0;
        for (int i = 0; i < numbers.length; i++) {
            if(numbers[i] != number){
                remainingNumbers[j] = numbers[i];
                j = j + 1;
            }
        }
        return remainingNumbers;
    }

    public static int findLowest(int[] numbers){
        int lowest = numbers[0];
        for (int i = 0; i < numbers.length; i++) {
            if(numbers[i] < lowest)
                lowest = numbers[i];
        }
        return lowest;
    }

    public static int findHighest(int[] numbers){
        int higest = numbers[0];
        for (int i = 0; i < numbers.length; i++) {
            if(numbers[i] > higest)
                higest = numbers[i];
        }
        return higest;
    }

    public static void printArray(int[] numbers){
        String report = "";
        for (int i = 0; i < numbers.length; i++) {
            report += numbers[i] + "\t";
        }
        JOptionPane.showMessageDialog(null, new JTextArea(report));
    }
}
