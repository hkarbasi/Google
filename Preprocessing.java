import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by habib on 12/21/16.
 */
public class Preprocessing {

    public void URLremoval(){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        StringBuilder sb = new StringBuilder();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/in/tweets.txt"));
            FileWriter fstream = new FileWriter("tweets-cleaned.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);

            for (String line; (line = br.readLine()) != null; ) {
                if (++count % 1000 == 0) {
                    System.out.println(count);
                    out.write(sb.toString());
                    sb = new StringBuilder();
                }


                StringBuilder temp;
                if (line.contains("http") || line.contains("RT") || line.contains("@")) {
                    temp = new StringBuilder();
                    String[] items = line.split(" ");
                    for (String item : items) {
                        if (!(item.contains("http") || item.contains("RT") || item.contains("@")))
                            temp.append(item).append(" ");
                    }
                } else
                    temp = new StringBuilder(line);
                sb.append(temp).append("\n");

            }
            out.close();
        }

        catch (Exception e){

        }
    }

    class Entities {
        Double id;
        String hash, name, gender, org;
        public Entities(String hash, String name, String gender, String org, Double id){
            this.hash = hash;
            this.name = name;
            this.gender = gender;
            this.org = org;
            this.id = id;
        }
    }

    public void ORG_Name_Resolver() {
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Map<String, String> IDs = new HashMap<>();
        Set<Double> Orgs = new HashSet<>();
        Map<String, Entities> users = new HashMap<>();
        Map<String, Integer> orgs = new HashMap<>(), pers = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/Hash-ID-Name.txt"));
            BufferedReader br2 = new BufferedReader(new FileReader(path + "data/org-IDs.txt"));
            BufferedReader br3 = new BufferedReader(new FileReader(path + "data/name-gender.txt"));
            BufferedReader br4 = new BufferedReader(new FileReader(path + "data/user-hash.txt"));

            FileWriter fstream = new FileWriter("ID-name-gender-org.txt", false); //true tells to append data.
            BufferedWriter out = new BufferedWriter(fstream);

            for (String line; (line = br2.readLine()) != null; ) {
                Orgs.add(Double.parseDouble(line));
            }
            for (String line; (line = br3.readLine()) != null; ) {
                String[] items = line.split("\t");
                IDs.put(items[0].trim(), items[1].trim());
            }
            for (String line; (line = br.readLine()) != null; ) {
//                if (++count % 1000 == 0) {
//                    System.out.println(count);
//                    out.write(sb.toString());
//                    sb = new StringBuilder();
//                }
                String[] items = line.split("\t");

                String gender, org;
                if(Orgs.contains(Double.parseDouble(items[1])))
                    org = "org";
                else
                    org = "per";

                if(IDs.containsKey(items[2].trim()))
                    gender = IDs.get(items[2].trim());
                else
                    gender = "male";

                users.put(items[0], new Entities(items[0], items[2], gender, org, Double.parseDouble(items[1])));
            }

            for (String line; (line = br4.readLine()) != null; ) {
                line = line.trim();
                String name;
                if (++count % 1000 == 0) {
                    System.out.println(count);
                    out.write(sb.toString());
                    sb = new StringBuilder();
                }
                Map<String, Integer> temp;
                if(users.containsKey(line)) {
                    sb.append(line).append("\t").append(users.get(line).name).append("\t").append(users.get(line).gender).append("\t").append(users.get(line).org).append("\n");
                    if(users.get(line).org.equals("org"))
                        temp = orgs;
                    else
                        temp = pers;
                    name = users.get(line).name.trim();



                }
                else{
                    System.out.println(line);
                    sb.append(line).append("\t").append("NoName").append("\t").append("male").append("\t").append("per").append("\n");
                    name = "NoName";
                    temp = pers;
                }

                if(temp.containsKey(name))
                    temp.put(name, temp.get(name) + 1);
                else
                    temp.put(name, 1);



            }

            out.close();

            Map[] temps = {orgs, pers};
            count = 0;
            for (Map<String, Integer> map: temps) {
                sb = new StringBuilder();
                ++count;
                for (Map.Entry<String, Integer> e: map.entrySet()) {
                    String key = e.getKey();
                    Integer val = e.getValue();
                    sb.append(key).append("\t").append(val).append("\n");


                }
                fstream = new FileWriter("Type" + count + ".txt", false); //true tells to append data.);
                out = new BufferedWriter(fstream);
                out.write(sb.toString());
                out.close();

            }

            out.close();
            } catch (Exception e) {

        }
    }

    public void emotionalTone(){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        StringBuilder sb = new StringBuilder();
        int day = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/emotionalTone.txt"));
            FileWriter fstream1 = new FileWriter("emotionalTone-Female.txt", false); //true tells to append data.
            FileWriter fstream2 = new FileWriter("emotionalTone-Male.txt", false); //true tells to append data.
            FileWriter fstream3 = new FileWriter("emotionalTone-Per.txt", false); //true tells to append data.
            FileWriter fstream4 = new FileWriter("emotionalTone-Org.txt", false); //true tells to append data.
            FileWriter fstream5 = new FileWriter("emotionalTone-Non.txt", false); //true tells to append data.

            BufferedWriter outF = new BufferedWriter(fstream1);
            BufferedWriter outM = new BufferedWriter(fstream2);
            BufferedWriter outN = new BufferedWriter(fstream5);
            BufferedWriter outP = new BufferedWriter(fstream3);
            BufferedWriter outO = new BufferedWriter(fstream4);

            int countF = 0, countM = 0, countNon = 0, countPer = 0, countOrg = 0;
            double toneF = 0, toneM = 0, toneNon = 0, tonePer = 0, toneOrg = 0;

            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[2].trim());
                cal.setTime(date);

                if (day != cal.get(Calendar.DAY_OF_MONTH)) {
                    if (day != 0) {
                        System.out.println(cal.getTime() + " \t" + countF + " \t" + toneF + "\n");
                        outF.write(outputDate.format(cal.getTime()) + " \t" + countF + " \t" + toneF + "\n");
                        outM.write(outputDate.format(cal.getTime()) + " \t" + countM + " \t" + toneM + "\n");
                        outN.write(outputDate.format(cal.getTime()) + " \t" + countNon + " \t" + toneNon + "\n");
                        outP.write(outputDate.format(cal.getTime()) + " \t" + countPer + " \t" + tonePer + "\n");
                        outO.write(outputDate.format(cal.getTime()) + " \t" + countOrg + " \t" + toneOrg + "\n");
                        outF.flush();
                        outM.flush();
                        outN.flush();
                        outP.flush();
                        outO.flush();
                        countF = 0; countM = 0; countNon = 0; countPer = 0; countOrg = 0;
                        toneF = 0; toneM = 0; toneNon = 0; tonePer = 0; toneOrg = 0;
                    }
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }
                double tone = Double.parseDouble(items[3].trim());

                switch(items[0].trim()) {
                    case "female": countF++; toneF += tone; break;
                    case "male": countM++; toneM += tone; break;
                    case "None": countNon++; toneNon += tone; break;
                }

                switch(items[1].trim()) {
                    case "per": countPer++; tonePer += tone; break;
                    case "org": countOrg++; toneOrg += tone; break;
                }
            }

            outF.write(outputDate.format(cal.getTime()) + " \t" + countF + " \t" + toneF + "\n");
            outM.write(outputDate.format(cal.getTime()) + " \t" + countM + " \t" + toneM + "\n");
            outN.write(outputDate.format(cal.getTime()) + " \t" + countNon + " \t" + toneNon + "\n");
            outP.write(outputDate.format(cal.getTime()) + " \t" + countPer + " \t" + tonePer + "\n");
            outO.write(outputDate.format(cal.getTime()) + " \t" + countOrg + " \t" + toneOrg + "\n");

            outF.flush();
            outM.flush();
            outN.flush();
            outP.flush();
            outO.flush();

            outF.close();
            outM.close();
            outN.close();
            outP.close();
            outO.close();

        }
        catch(Exception e){

        }
    }
    class Parameters{
        int day, countF, countM, countPer, countOrg;
        double valF, valM, valPer, valOrg;
        SimpleDateFormat outputDate;
        Calendar cal;

        public Parameters(int day, int countF, int countM, int countPer, int countOrg, double valF, double valM, double valPer, double valOrg,
                          SimpleDateFormat outputDate, Calendar cal){
            this.day = day;
            this.countF = countF;
            this.countM = countM;
            this.countPer = countPer;
            this.countOrg = countOrg;
            this.valF = valF;
            this.valM = valM;
            this.valOrg = valOrg;
            this.valPer = valPer;
            this.outputDate = outputDate;
            this.cal = cal;
        }
    }

    class Buffers{
        BufferedWriter outF, outM, outP, outO;
    }

    public Buffers bufferedAllocation(String name){
        Buffers buffers = new Buffers();
        try {
            String dir = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/data/out";
            FileWriter fstream1 = new FileWriter(new File(dir, name + "-Female.txt"), false); //true tells to append data.
            FileWriter fstream2 = new FileWriter(new File(dir, name + "-Male.txt"), false); //true tells to append data.
            FileWriter fstream3 = new FileWriter(new File(dir, name + "-Per.txt"), false); //true tells to append data.
            FileWriter fstream4 = new FileWriter(new File(dir, name + "-Org.txt"), false); //true tells to append data.

            buffers.outF = new BufferedWriter(fstream1);
            buffers.outM = new BufferedWriter(fstream2);
            buffers.outP = new BufferedWriter(fstream3);
            buffers.outO = new BufferedWriter(fstream4);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return buffers;
    }

    public void processing(String line, Buffers buffers, Parameters params, int column, List<Set<String>> topPeople){
        String[] items = line.split("\t");
        try{
            Date date = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.S").parse(items[2].trim());

            params.cal.setTime(date);

            if (params.day != params.cal.get(Calendar.DAY_OF_MONTH)) {
                if (params.day != 0) {
                    buffers.outF.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valF / params.countF  + "\n");
                    buffers.outM.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valM / params.countM + "\n");
                    buffers.outP.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valPer / params.countPer + "\n");
                    buffers.outO.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valOrg / params.countOrg + "\n");
                    buffers.outF.flush();
                    buffers.outM.flush();
                    buffers.outP.flush();
                    buffers.outO.flush();
                    params.countF = 0; params.countM = 0; params.countPer = 0; params.countOrg = 0;
                    params.valF = 0; params.valM = 0; params.valPer = 0; params.valOrg = 0;
                }
                params.day = params.cal.get(Calendar.DAY_OF_MONTH);
            }
            double val = Double.parseDouble(items[column].trim());

            if(topPeople == null || topPeople.get(0).contains(items[10].trim())) {
                params.countF++;
                params.valF += val;
            }
            if(topPeople == null || topPeople.get(1).contains(items[10].trim())) {
                params.countM++;
                params.valM += val;
            }
            if(topPeople == null || topPeople.get(2).contains(items[10].trim())) {
                params.countPer++;
                params.valPer += val;
            }
            if(topPeople == null || topPeople.get(3).contains(items[10].trim())) {
                params.countOrg++;
                params.valOrg += val;
            }


        }
        catch (Exception e) {
            System.out.println("hello" + e.getMessage() +  params.day);
        }
    }

    public void LIWC(String name, int column, List<Set<String>> topPeople){
        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "data/in/LIWC-data 2.txt"));
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            Buffers buffers = bufferedAllocation(name);
            Parameters params = new Parameters(0, 0 , 0, 0, 0, 0, 0, 0, 0, outputDate, cal);
            String line = br.readLine();

            for (; (line = br.readLine()) != null; )
                processing(line, buffers, params, column, topPeople);

            buffers.outF.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valF / params.countF  + "\n");
            buffers.outM.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valM / params.countM + "\n");
            buffers.outP.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valPer / params.countPer + "\n");
            buffers.outO.write(params.outputDate.format(params.cal.getTime()) + " \t" + params.valOrg / params.countOrg + "\n");

            buffers.outF.flush();
            buffers.outM.flush();
            buffers.outP.flush();
            buffers.outO.flush();

            buffers.outF.close();
            buffers.outM.close();
            buffers.outP.close();
            buffers.outO.close();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return ( o2.getValue() ).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public Set<String> top10Percent(String type) {
        Set<String> topPeople = new HashSet<>();
        Map<String, Integer> peopleCounter = new HashMap<>();

        String path = "/Users/habib/Documents/!Courses/!DIA2/STEM/SandBox/src/";
        try {
//            BufferedReader br = new BufferedReader(new FileReader(path + "data/in/LIWC-data 2.txt"));
            BufferedReader br = new BufferedReader(new FileReader(path + "data/in/LIWC-data 2.txt"));
            for (String line; (line = br.readLine()) != null; ) {
                String[] items = line.split("\t");
                String people;
                if((type.equals("F") && items[0].trim().equals("female")) ||
                        (type.equals("M") && items[0].trim().equals("male")) ||
                        (type.equals("P") && items[1].trim().equals("per")) ||
                        (type.equals("O") && items[1].trim().equals("org")))
                    if (!peopleCounter.containsKey(items[10]))
                        peopleCounter.put(items[10].trim(), 1);
                    else
                        peopleCounter.put(items[10].trim(), peopleCounter.get(items[10].trim()) + 1);


            }

            Set mapKV = peopleCounter.entrySet();
            int count = 0;
            peopleCounter = sortByValue(peopleCounter);
            for (Map.Entry name : peopleCounter.entrySet()) {
                if(count > peopleCounter.size() * 0.1)
                    break;
                count++;
//                System.out.println(name.getKey() + "    " + name.getValue());
                topPeople.add((String) name.getKey());
            }


        }
        catch (Exception e) {
        }
        return topPeople;
    }

    public static void main(String[] args) {
        Preprocessing test = new Preprocessing();
//        test.URLremoval();
//        test.ORG_Name_Resolver();
//        test.emotionalTone();


//        test.LIWC("emotionalTone", 3, null);
//        test.LIWC("emotionPositive", 4, null);
//        test.LIWC("emotionNegative", 5, null);
//        test.LIWC("personalPronouns", 7, null);
//        test.LIWC("personalConcerns", 8, null);
//        test.LIWC("coreDrives", 9, null);

        Set<String> topFemale10 = test.top10Percent("F");
        Set<String> topMale10 = test.top10Percent("M");
        Set<String> topPer10 = test.top10Percent("P");
        Set<String> topOrg10 = test.top10Percent("O");
        List<Set<String>> topPeople = new ArrayList<Set<String>>(Arrays.asList(topFemale10, topMale10, topPer10, topOrg10));
        test.LIWC("emotionalTone", 3, topPeople);
        test.LIWC("emotionPositive", 4, topPeople);
        test.LIWC("emotionNegative", 5, topPeople);
        test.LIWC("personalPronouns", 7, topPeople);
        test.LIWC("personalConcerns", 8, topPeople);
        test.LIWC("coreDrives", 9, topPeople);


    }

}
