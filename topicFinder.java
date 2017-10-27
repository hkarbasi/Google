import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by habib on 9/29/16.
 */
public class topicFinder {

    public static void main(String[] args) {
        int firstTopics = 200, count = 0, tweetCounts = 1000;
        Map<String, Integer> topics = new HashMap<>();


        try (BufferedReader br = new BufferedReader(new FileReader("/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/tweets-stem-wom.txt"))) {
//        try (BufferedReader br = new BufferedReader(new FileReader("/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/tweets-stem-edu.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                count++;
                if (count % tweetCounts == 0)
                    System.out.println(count);

                Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
                Matcher match = pt.matcher(line);
                while (match.find()) {
                    String s = match.group();
                    line = line.replaceAll("\\" + s, " ");
                }

//                System.out.println(line);
                String[] words = line.split(" +");

                for (String s : words) {
//                    System.out.println(s);
                    if (topics.containsKey(s.toLowerCase()))
                        topics.put(s.toLowerCase(), topics.get(s.toLowerCase()) + 1);
                    else
                        topics.put(s.toLowerCase(), 1);
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }




        topics = MapUtil.sortByValue(topics);

        count = 0;
        Iterator it = topics.entrySet().iterator();
        while (it.hasNext() && count < firstTopics) {
            count++;
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + "\t" + pair.getValue());
            it.remove();
        }
    }
}
