import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by habib on 11/8/16.
 */
public class OrgData {

    private Map<Double, String> names;
    private String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
    private Set<Double> orgIDs;

    private void userInfoReading() {
        names = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path + "IDs.csv"))) {
            br.readLine();
            for (String line; (line = br.readLine()) != null; ) {
                String[] elements = line.split(",");
                names.put(Double.parseDouble(elements[0]), line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void orgIDsReading(){
        orgIDs = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path + "data/org-IDs.txt"))) {
            for (String line; (line = br.readLine()) != null; )
                orgIDs.add(Double.parseDouble(line));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void dataMatching() {

        try
        {
            FileWriter fstream = new FileWriter(path + "orgInfo.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);

            for(Double d : orgIDs) {
                out.write(names.get(d) + "\n");
//                System.out.println(names.get(d));
            }
            out.close();

        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }

        public static void main(String[] args) {
        OrgData obj = new OrgData();
        obj.userInfoReading();
        obj.orgIDsReading();
        obj.dataMatching();
    }
}
