/**
 * Created by habib on 12/16/16.
 */
public class Employee {

    public static int highestEmployee(int[][] hours){
        int highestHour = 0;
        int employeeNumber = 0;
        for (int i = 0; i < hours.length; i++) {
            int sum = 0;
            for (int j = 0; j < hours[0].length; j++) {
                sum = sum + hours[i][j];
            }
            if (sum > highestHour) {
                highestHour = sum;
                employeeNumber = i + 1;
            }
        }
        return employeeNumber;
    }

    public static int lowestEmployee(int[][] hours){
        int lowestHour = 0;
        int employeeNumber = 0;
        for (int i = 0; i < hours.length; i++) {
            int sum = 0;
            for (int j = 0; j < hours[0].length; j++) {
                sum = sum + hours[i][j];
            }
            if (sum > lowestHour) {
                lowestHour = sum;
                employeeNumber = i + 1;
            }
        }
        return employeeNumber;
    }


}
