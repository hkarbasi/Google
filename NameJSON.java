import java.io.*;

/**
 * Created by habib on 10/11/16.
 */
public class NameJSON {
    public static void main(String[] args) {
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/Data/Tweets";
        StringBuilder output = new StringBuilder("");

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
//            System.out.println("Screen Name" + "\t" + "Name" + "\t" + "Location");
            output.append("Screen Name" + "\t" + "Name" + "\t" + "Location" + "\n");
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
//                    System.out.println("File Path =  " + path + "/" +listOfFile.getName());
                    try (BufferedReader br = new BufferedReader(new FileReader(path + "/" +listOfFile.getName() ))) {
                        for(String line; (line = br.readLine()) != null; ) {
//                            System.out.println(line);
                            int lastIndexName = line.lastIndexOf("\"name\":\"");
                            int lastIndexScreenName = line.lastIndexOf("\"screen_name\":\"");
                            int lastIndexLocation = line.lastIndexOf("\"location\":\"");

                            int indexName = lastIndexName + 8,
                                    indexScreenName = lastIndexScreenName + 15,
                                    indexLocation = lastIndexLocation + 12;

                            if (lastIndexName > 0 && lastIndexScreenName > 0 && lastIndexLocation > 0) {
                                while (line.charAt(indexName) != '"')
                                    indexName++;
                                while (line.charAt(indexScreenName) != '"')
                                    indexScreenName++;
                                while (line.charAt(indexLocation) != '"')
                                    indexLocation++;
                                String name = line.subSequence(lastIndexName + 8, indexName).toString();
                                String screenName = line.subSequence(lastIndexScreenName + 15, indexScreenName).toString();
                                String location = line.subSequence(lastIndexLocation + 12, indexLocation).toString();

//                                System.out.println(screenName + "\t" + name + "\t" + location);
                                output.append(screenName).append("\t").append(name).append("\t").append(location).append("\n");
                            }
                            else
                                if(lastIndexName > 0 || lastIndexScreenName > 0 || lastIndexLocation > 0){
                                    System.exit(13);
                                }
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                } else if (listOfFile.isDirectory()) {
//                    System.out.println("Directory " + listOfFile.getName());
                }
            }
        }

        try
        {
            FileWriter fstream = new FileWriter("JSON-Name.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(output.toString() + "\n");
//                    System.out.print(JSONs.toString() + "\n");
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }


    }




//        try (BufferedReader br = new BufferedReader(new FileReader("/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/STEM.csv"))) {
//            String twitterName = "";
//
//        }

}
