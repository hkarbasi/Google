import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by habib on 10/1/16.
 */
public class CommonTwitters {

    private Map<String, Tweet> mapSTEM = new HashMap<>(), mapEngineering = new HashMap<>(), mapCommon = new HashMap<>();

    public class Tweet {
        int tweetCountSTEM, tweetCountEngineering, followerCount;
    }


    private enum Order {Engineering, STEM}

    private class Comparators implements Comparator<Map.Entry<String, Tweet>> {

        private Order sortingBy;
        Comparators(Order o){
            sortingBy = o;
        }

        @Override
        public int compare(Map.Entry<String, Tweet> person1, Map.Entry<String, Tweet> person2) {
            switch(sortingBy) {
                case Engineering: return (person2.getValue().tweetCountEngineering - person1.getValue().tweetCountEngineering);
                case STEM: return (person2.getValue().tweetCountSTEM - person1.getValue().tweetCountSTEM);
            }
            return 0;
        }
    }




    public void fileReading() {

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/STEM.csv"))) {
            String twitterName = "";
            int followers = 0, tweetCount = 0;
            String line;

            line = br.readLine();
            String[] words = line.split(",");
            twitterName = words[0];
            followers = Integer.parseInt(words[1]);
            tweetCount = 1;

            for (; (line = br.readLine()) != null; ) {
                words = line.split(",");
                if(words[0].equals(twitterName))
                    tweetCount++;
                else{
                    Tweet tweet = new Tweet();
                    tweet.tweetCountSTEM = tweetCount;
                    tweet.followerCount = followers;
                    mapSTEM.put(twitterName, tweet);
                    twitterName = words[0];
                    followers = Integer.parseInt(words[1]);
                    tweetCount = 1;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

//        for(Map.Entry<String, Tweet> entry : mapSTEM.entrySet()){
//            String key = entry.getKey();
//            Tweet val = entry.getValue();
//            System.out.println(key + " " + val.tweetCountSTEM + " " + val.followerCount);
//        }

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/STEM-Engineering.csv"))) {
            String twitterName = "";
            int followers = 0, tweetCount = 0;
            String line;

            line = br.readLine();
            String[] words = line.split(",");
            twitterName = words[0];
            followers = Integer.parseInt(words[1]);
            tweetCount = 1;

            for (; (line = br.readLine()) != null; ) {
                words = line.split(",");
                if(words[0].equals(twitterName))
                    tweetCount++;
                else{
                    Tweet tweet = new Tweet();
                    tweet.tweetCountEngineering = tweetCount;
                    tweet.followerCount = followers;
                    mapEngineering.put(twitterName, tweet);
                    twitterName = words[0];
                    followers = Integer.parseInt(words[1]);
                    tweetCount = 1;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }



    }

    public void commonTwitterDetector(){
        for(Map.Entry<String, Tweet> entry : mapEngineering.entrySet()){
            String key = entry.getKey();
            Tweet val = entry.getValue();
            if(mapSTEM.containsKey(key)){
                Tweet tweet = new Tweet();
                tweet.tweetCountSTEM = mapSTEM.get(key).tweetCountSTEM;
                tweet.tweetCountEngineering = val.tweetCountEngineering;
                tweet.followerCount = val.followerCount;
                mapCommon.put(key, tweet);
            }
        }

//        System.out.println("\n\n");
        System.out.println("Twitter Name"+ "\t" + "Tweet Count - STEM" + "\t" + "Tweet Count - STEM Engineering" + "\t" + "Followers");

        for(Map.Entry<String, Tweet> entry : mapCommon.entrySet()){
            String key = entry.getKey();
            Tweet val = entry.getValue();
//            System.out.println(key + "\t" + val.tweetCountSTEM + "\t" + val.tweetCountEngineering + "\t" + val.followerCount);
        }


        List<Map.Entry<String, Tweet>> list =
                new LinkedList<Map.Entry<String, Tweet>>( mapSTEM.entrySet());

//        Collections.sort( list, (o1, o2) -> (o2.getValue().tweetCountEngineering < o1.getValue().tweetCountEngineering)? 0 : 1);
        Collections.sort(list, new Comparators(Order.STEM));
//        System.out.println("\n Women Entries");

        for(Map.Entry<String, Tweet> entry : list){
            String key = entry.getKey();
            Tweet val = entry.getValue();
            System.out.println(key + "\t" + val.tweetCountSTEM + "\t" + val.followerCount);
        }

        System.out.println("\n STEM Entries");

        for(Map.Entry<String, Tweet> entry : mapEngineering.entrySet()){
            String key = entry.getKey();
            Tweet val = entry.getValue();
//            System.out.println(key + "\t" + val.tweetCountEngineering + "\t" + val.followerCount);
        }

        System.out.println("\n\n");

    }

    public static void main(String[] args) {
        CommonTwitters test = new CommonTwitters();
        test.fileReading();
        test.commonTwitterDetector();
    }
}
