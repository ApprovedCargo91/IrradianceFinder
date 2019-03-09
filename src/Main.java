import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    private static HttpsURLConnection con;
    private static ArrayList<Double> lat = new ArrayList<>();
    private static ArrayList<Double> lon = new ArrayList<>();
    private static final String csvfn = "ASC 2018 Segment 1";
    private static final File coordFile = new File(csvfn + ".txt");
    private static int[][] cloudCover;
    private static final int remFactor = 25;
    private static ArrayList<String> dates = new ArrayList<>();

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(coordFile));
            String str;
            br.readLine();
            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");
                st.nextToken();
                st.nextToken();
                lat.add(Double.parseDouble(st.nextToken()));
                lon.add(Double.parseDouble(st.nextToken()));
            }
            cloudCover = new int[lat.size() / remFactor][40];
        } catch (Exception e) {
            System.out.println("Error: File could not be found/read!");
        }
        for(int i = 0; i < lat.size(); i += remFactor) {
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?APPID=5b1eae5aa03d118038e19b8a1b2e61ac&lat=" + lat.get(i) + "&lon=" + lon.get(i));
                System.out.println("URL set!");
                con = (HttpsURLConnection) url.openConnection();
                System.out.println("Connection opened!");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("GET");
            } catch (Exception e) {
                System.out.println("Error: Connection configuration is incorrect.");
            }
            try {
                int status = con.getResponseCode();
                System.out.println("HTTP Status Code: " + status);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = in.readLine();
//            content = new StringBuffer();
                int lastIndex = 0;
                String temp;
                int count = 0;
                while(lastIndex != -1) {
                    lastIndex = inputLine.indexOf("all",lastIndex);
                    if(lastIndex != -1) {
                        temp = inputLine.substring(lastIndex + 5, lastIndex + 7);
                        lastIndex += 3;
                        if(temp.charAt(1) == '}') {
                            temp = temp.substring(0,1);
                        }
                        cloudCover[i / remFactor][count]= Integer.parseInt(temp);
                        count++;
                    }
                }
                if (i == 0) {
                    lastIndex = 0;
                    while(lastIndex != -1) {
                        lastIndex = inputLine.indexOf("dt_txt",lastIndex);
                        if(lastIndex != -1) {
                            temp = inputLine.substring(lastIndex + 9, lastIndex + 28);
                            dates.add(temp);
//                            temp = inputLine.substring(lastIndex + 20, lastIndex + 28);
//                            times.add(temp);
                            lastIndex += 6;
                        }
                    }
                }
                in.close();
                con.disconnect();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error: Connection could not be made.");
            }
        }

        try (PrintWriter writer = new PrintWriter(new File(csvfn + ".csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append(',');
            sb.append(',');
            for(String date:dates) {
                sb.append(date);
                sb.append(',');
            }
            sb.replace(sb.length()-1,sb.length(),"\n");
            int[] lastAppended = cloudCover[0];
            for(int i = 0; i < lat.size() / remFactor; i++) {
                if (i == 0 || i == lat.size() / remFactor || !Arrays.equals(lastAppended, cloudCover[i])) {
                    lastAppended = cloudCover[i];
                    sb.append(lat.get(i * remFactor));
                    sb.append(',');
                    sb.append(lon.get(i * remFactor));
                    sb.append(',');
                    for(int cc:cloudCover[i]) {
                        sb.append(cc);
                        sb.append(',');
                    }
                    sb.replace(sb.length()-1,sb.length(),"\n");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            writer.write(sb.toString());
            System.out.println(csvfn + ".csv written.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}