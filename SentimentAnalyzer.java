/**
 * Created by habib on 10/18/16.
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class SentimentAnalyzer {

    private String httpPost(String txt){
        String output = "";
        try {
//            URL url = new URL("http://sentiment.vivekn.com/api/text/");
            URL url = new URL("http://sentiment.vivekn.com/api/batch/");

            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            byte[] out = txt.getBytes(StandardCharsets.UTF_8);
//            byte[] out = ("{\"" + txt + "\" : \"txt\"}") .getBytes(StandardCharsets.UTF_8);
//            byte[] out = "{\"like\":\" txt\", \"amazing\":\" txt\", \"bad\":\" txt\", \"good\":\" \", \"horrible\":\" \"}" .getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);

            }

            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//                System.out.println(inputLine);
            if((inputLine = in.readLine()) != null) {
                output = inputLine;
            }
            else
                output = "error!";

            in.close();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return output;
    }

    private void httpTest(){
        try {
            URL oracle = new URL("https://www.yahoo.com/");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        }
        catch(Exception e)
        {
            System.out.println("E!");
        }

    }

    private void fileReader() {
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        int limit = 1000, count = 0;
        Map<String, ArrayList<Double>> results = new HashMap<>();
        StringBuilder tweets = new StringBuilder("{\"");
//        "{\"" + txt+ "\" : \"txt\"}"
//        try (BufferedReader br = new BufferedReader(new FileReader(path + "tweet-1.txt"))) {
        try (BufferedReader br = new BufferedReader(new FileReader(path + "tweets-orig.txt"))) {
            for (String line; (line = br.readLine()) != null; ) {
                count++;
//                System.out.println(line);
//                if(count % 1000 == 0)
//                    System.out.println(count);

                if (count % limit == 0) {
                    tweets.append(line).append(" ").append(count).append("\" : \"txt\"}");
                    String result = httpPost(tweets.toString());
//                    System.out.println(result);
                    String[] JSONs = result.split("\\}, \\{");
                    if (JSONs.length > 0) {
                        for (String JSON : JSONs) {
                            String[] keys = JSON.split("\", \"");
//                        System.out.println(JSON);
                            if (keys.length > 1) {
//                        System.out.println(keys[0]);
                                Double confidence = Double.parseDouble(keys[0].substring(keys[0].lastIndexOf("\"") + 1));
                                //                        System.out.println(confidence);

                                //                        System.out.println(keys[1]);
                                String sentiment = keys[1].substring(10, keys[1].lastIndexOf("\""));
                                //                        System.out.println(sentiment);

                                if (results.containsKey(sentiment)) {
                                    results.get(sentiment).add(confidence);
                                } else {
                                    ArrayList<Double> confidences = new ArrayList<>(Collections.singletonList(confidence));
                                    results.put(sentiment, confidences);
                                }
                            } else {
                                System.out.println("tweets illegal character @ " + count);
                            }

                        }
                    } else {
                        System.out.println("tweets illegal character @ " + count);
                    }

                    int tweetsNum = 0;
                    for (Map.Entry<String, ArrayList<Double>> e : results.entrySet()) {
                        BufferedWriter out = null;
                        try {
                            FileWriter fstream = new FileWriter(e.getKey() + ".txt", true); //true tells to append data.
                            out = new BufferedWriter(fstream);
                            StringBuilder output = new StringBuilder();
                            for (Double confidence : e.getValue()) {
                                output.append(confidence).append("\n");
                                tweetsNum++;
                            }

                            out.write(output.toString());
                            out.flush();
                            out.close();

                        } catch (IOException err) {
                            System.err.println("Error: " + err.getMessage());
                        }

                    }
//                    if(limit != tweetsNum)
                    System.out.println(count + " " + tweetsNum);

//                    System.out.println("");
                    results = new HashMap<>();
                    tweets = new StringBuilder("{\"");
                } else {
                    tweets.append(line).append(" ").append(count).append("\" : \"txt\", \"");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tweets.append("hello").append("\" : \"txt\"}");
        String result = httpPost(tweets.toString());
        String[] JSONs = result.split("\\}, \\{");
        if (JSONs.length > 0) {
            for (String JSON : JSONs) {
                String[] keys = JSON.split("\", \"");
                Double confidence = Double.parseDouble(keys[0].substring(keys[0].lastIndexOf("\"") + 1));
                String sentiment = keys[1].substring(10, keys[1].lastIndexOf("\""));

                if (results.containsKey(sentiment)) {
                    results.get(sentiment).add(confidence);
                } else {
                    ArrayList<Double> confidences = new ArrayList<>(Collections.singletonList(confidence));
                    results.put(sentiment, confidences);
                }
            }
        } else {
            System.out.println("tweets illegal character");
        }
        for (Map.Entry<String, ArrayList<Double>> e : results.entrySet()) {
            BufferedWriter out = null;
            try {
                FileWriter fstream = new FileWriter(e.getKey() + ".txt", true); //true tells to append data.
                out = new BufferedWriter(fstream);
                StringBuilder output = new StringBuilder();
                for (Double confidence : e.getValue())
                    output.append(confidence).append("\n");

                out.write(output.toString());
                out.flush();
                out.close();

            } catch (IOException err) {
                System.err.println("Error: " + err.getMessage());
            }

        }


    }

    private void fileReader2() {
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        int limit = 1, count = 0;
        Map<String, ArrayList<Double>> results = new HashMap<>();
        StringBuilder resultToFile = new StringBuilder();
        StringBuilder tweets = new StringBuilder("{\"");
//        "{\"" + txt+ "\" : \"txt\"}"
//        try (BufferedReader br = new BufferedReader(new FileReader(path + "tweet-1.txt"))) {
        try (BufferedReader br = new BufferedReader(new FileReader(path + "tweets-orig.txt"))) {
            for (String line; (line = br.readLine()) != null; ) {
                count++;
//                System.out.println(line);
//                if(count % 1000 == 0)
//                    System.out.println(count);

                if(count % limit == 0) {
                    tweets.append(line).append(" ").append("\" : \"txt\"}");
                    String result = httpPost(tweets.toString());
//                    System.out.println(result);
                    String[] JSONs = result.split("\\}, \\{");
                    if(JSONs.length > 0) {
                        for (String JSON : JSONs) {
                            String[] keys = JSON.split("\", \"");
//                        System.out.println(JSON);
                            if(keys.length > 1) {
//                        System.out.println(keys[0]);
                                Double confidence = Double.parseDouble(keys[0].substring(keys[0].lastIndexOf("\"") + 1));
                                //                        System.out.println(confidence);

                                //                        System.out.println(keys[1]);
                                String sentiment = keys[1].substring(10, keys[1].lastIndexOf("\""));
                                //                        System.out.println(sentiment);
                                resultToFile.append(count + "\t" + sentiment + "\t" + confidence + "\n");


                                if (count % (limit * 1000) == 0) {

                                    System.out.println(count);
                                    BufferedWriter out = null;
                                    try {
                                        FileWriter fstream = new FileWriter("sentiment-result.txt", true); //true tells to append data.
                                        out = new BufferedWriter(fstream);
                                        out.write(resultToFile.toString());
                                        out.flush();
                                        out.close();
                                        resultToFile = new StringBuilder();

                                    } catch (IOException err) {
                                        System.err.println("Error: " + err.getMessage());
                                    }
                                }


                            } else {
                                System.out.println("tweets illegal character @ " + count);
                            }


                        }
                    }
                    else{
                        System.out.println("tweets illegal character @ " + count);
                    }

                    int tweetsNum = 0;


//                    if(limit != tweetsNum)
//                    System.out.println(count + " " + tweetsNum);

//                    System.out.println("");
                    results = new HashMap<>();
                    tweets = new StringBuilder("{\"");
                }
                else{
                    tweets.append(line).append(" ").append(count).append("\" : \"txt\", \"");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    private void reportGeneratorDaily(){
         String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try  {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/Sentiments.txt"));
            int day = 0, week = 0, posCnt = 0, neutCnt = 0, negCnt = 0;
            double posCfd = 0, neutCfd = 0, negCfd = 0;
            FileWriter fstream = new FileWriter("reportDaily.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[0].trim());
                cal.setTime(date);

                if (day != cal.get(Calendar.DAY_OF_MONTH)) {
                    if(day != 0)
                        out.write(outputDate.format(cal.getTime()) + " \t" + posCnt + " \t" + posCfd + " \t" + neutCnt + " \t" + neutCfd + " \t" + negCnt + " \t" + negCfd + "\n");
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    posCnt = 0;
                    posCfd = 0;
                    neutCnt = 0;
                    neutCfd = 0;
                    negCnt = 0;
                    negCfd = 0;
                }
                switch (items[1]) {
                    case "Positive": posCnt++; posCfd += Double.parseDouble(items[2]); break;
                    case "Neutral": neutCnt++; neutCfd += Double.parseDouble(items[2]); break;
                    case "Negative": negCnt++; negCfd += Double.parseDouble(items[2]); break;
                }
            }
            out.write(outputDate.format(cal.getTime()) + " \t" + posCnt + " \t" + posCfd + " \t" + neutCnt + " \t" + neutCfd + " \t" + negCnt + " \t" + negCfd + "\n");
            out.flush();
            out.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

    private void reportOrgPerDistribution(){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try  {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/tweets-user_IDs copy.txt"));
            BufferedReader br2 = new BufferedReader(new FileReader(path + "data/org-IDs.txt"));
            Set<Double> orgIDs = new HashSet<>();
            Map<String, Double> userIDs = new HashMap<>();
            for (String line; (line = br2.readLine()) != null; ) {
                orgIDs.add(Double.parseDouble(line));
            }
            BufferedReader br3 = new BufferedReader(new FileReader(path + "data/users-IDs.txt"));
            for (String line; (line = br3.readLine()) != null; ) {
                String[] items = line.split("\t");
                userIDs.put(items[0], Double.parseDouble(items[1]));
            }


            int day = 0, week = 0, perCount = 0, orgCount= 0;
            FileWriter fstream = new FileWriter("reportOrgPer.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[0].trim());
                cal.setTime(date);

                if (day != cal.get(Calendar.DAY_OF_MONTH)) {
                    if(day != 0)
                        out.write(outputDate.format(cal.getTime()) + " \t" + perCount + " \t" + orgCount + "\n");
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    perCount = 0; orgCount= 0;
                }
                if(orgIDs.contains(userIDs.get(items[1])))
                    orgCount++;
                else
                    perCount++;

            }
            out.write(outputDate.format(cal.getTime()) + " \t" + perCount + " \t" + orgCount + "\n");
            out.flush();
            out.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

    class Sentiment{
        int pos = 0, neg = 0, neut = 0;
        public String toString(){
            return pos + "\t" + neut + "\t" + neg;
        }

    }

    private void reportOrgPerSentimentDistribution(){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try  {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/sentiments-users copy.txt"));
            BufferedReader br2 = new BufferedReader(new FileReader(path + "data/org-IDs.txt"));
            Set<Double> orgIDs = new HashSet<>();
            Map<String, Double> userIDs = new HashMap<>();
            for (String line; (line = br2.readLine()) != null; ) {
                orgIDs.add(Double.parseDouble(line));
            }
            BufferedReader br3 = new BufferedReader(new FileReader(path + "data/users-IDs.txt"));
            for (String line; (line = br3.readLine()) != null; ) {
                String[] items = line.split("\t");
                userIDs.put(items[0], Double.parseDouble(items[1]));
            }


            int day = 0, week = 0;
            Sentiment org = new Sentiment(), per = new Sentiment();

            FileWriter fstream = new FileWriter("reportOrgPerSentiment.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[1].trim());
                cal.setTime(date);

                if (day != cal.get(Calendar.DAY_OF_MONTH)) {
                    if (day != 0)
                        out.write(outputDate.format(cal.getTime()) + " \t" + per.toString() + " \t" + org.toString() + "\n");
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    org = new Sentiment();
                    per = new Sentiment();
                }
                Sentiment twitter;
                if (orgIDs.contains(userIDs.get(items[2])))
                    twitter = org;
                else
                    twitter = per;
                if (Double.parseDouble(items[0]) == 0)
                    twitter.neut++;
                else {
                    if (Double.parseDouble(items[0]) > 0)
                        twitter.pos++;
                    else
                        twitter.neg++;
                }
            }


            out.write(outputDate.format(cal.getTime()) + " \t" + per.toString() + " \t" + org.toString() + "\n");
            out.flush();
            out.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

    private void reportOrgPerSentimentNaiveDistribution(){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try  {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/sentiments-users naive copy.txt"));
            BufferedReader br2 = new BufferedReader(new FileReader(path + "data/org-IDs.txt"));

            FileWriter fstream = new FileWriter("ID-name-gender-org.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);

            Set<Double> orgIDs = new HashSet<>();
            Map<String, Double> userIDs = new HashMap<>();
            for (String line; (line = br2.readLine()) != null; ) {
                orgIDs.add(Double.parseDouble(line));
            }
            BufferedReader br3 = new BufferedReader(new FileReader(path + "data/users-IDs.txt"));
            for (String line; (line = br3.readLine()) != null; ) {
                String[] items = line.split("\t");
                userIDs.put(items[0], Double.parseDouble(items[1]));
            }


            int day = 0, week = 0;
            Sentiment org = new Sentiment(), per = new Sentiment();

             fstream = new FileWriter("reportOrgPerSentimentNaive.txt", false); //true tells to append data.
             out = new BufferedWriter(fstream);
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[0].trim());
                cal.setTime(date);

                if (day != cal.get(Calendar.DAY_OF_MONTH)) {
                    if (day != 0)
                        out.write(outputDate.format(cal.getTime()) + " \t" + per.toString() + " \t" + org.toString() + "\n");
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    org = new Sentiment();
                    per = new Sentiment();
                }
                Sentiment twitter;
                if (orgIDs.contains(userIDs.get(items[3])))
                    twitter = org;
                else
                    twitter = per;
                switch(items[1]) {
                    case "Positive": twitter.pos++; break;
                    case "Negative": twitter.neg++; break;
                    case "Neutral": twitter.neut++; break;
                }
            }


            out.write(outputDate.format(cal.getTime()) + " \t" + per.toString() + " \t" + org.toString() + "\n");
            out.flush();
            out.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SentimentAnalyzer test = new SentimentAnalyzer();
//        test.test();
//        test.httpTest();
//        test.fileReader();
//        test.fileReader2();
//        test.reportGeneratorDaily();
//        test.reportOrgPerDistribution();
//        test.reportOrgPerSentimentDistribution();
        test.reportOrgPerSentimentNaiveDistribution();

    }



}
