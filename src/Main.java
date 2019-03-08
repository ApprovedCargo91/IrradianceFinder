import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static URL url;
    private static HttpsURLConnection con;
    private static Map<String, String> parameters = new HashMap<>();
    private static DataOutputStream out;
    private static StringBuffer content;
    private static int zip = 30332;
    private static ArrayList<Integer> cloudCover = new ArrayList<>();
    private static ArrayList<String> times = new ArrayList<>();
    private static ArrayList<String> dates = new ArrayList<>();

    public static void main(String[] args) {
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/forecast?APPID=5b1eae5aa03d118038e19b8a1b2e61ac&zip="+zip);
            System.out.println("URL set!");

//        } catch (MalformedURLException e) {
//            System.out.println("Error: The URL is malformed.");
//        }
//        try {
            con = (HttpsURLConnection) url.openConnection();
            System.out.println("Connection opened!");
//        } catch (IOException e) {
//            System.out.println("Error: A connection could not be opened.");
//        }
//        try {
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("GET");
//        } catch (ProtocolException e) {
//            System.out.println("Error: The request method is invalid.");
//        }
//            parameters.put("zip", "30332");
//            parameters.put("APPID", "5b1eae5aa03d118038e19b8a1b2e61ac");

//            con.setDoOutput(true);
//        try {
//            out = new DataOutputStream(con.getOutputStream());
//        } catch (IOException e) {
//            System.out.println("Error: The output stream does not exist.");
//        }
//            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//            out.flush();
//            out.close();
        } catch (Exception e) {
            System.out.println("Error: Connection configuration is incorrect.");
        }
        try {
            int status = con.getResponseCode();
            System.out.println("HTTP Code: " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = in.readLine();
//            content = new StringBuffer();
            int lastIndex = 0;
            String temp;
            while(lastIndex != -1) {
                lastIndex = inputLine.indexOf("all",lastIndex);
                if(lastIndex != -1) {
                    temp = inputLine.substring(lastIndex + 5, lastIndex + 7);
                    lastIndex += 3;
                    if(temp.charAt(1) == '}') {
                        temp = temp.substring(0,1);
                    }
                    cloudCover.add(Integer.parseInt(temp));
                }
            }
            lastIndex = 0;
            while(lastIndex != -1) {
                lastIndex = inputLine.indexOf("dt_txt",lastIndex);
                if(lastIndex != -1) {
                    temp = inputLine.substring(lastIndex + 9, lastIndex + 19);
                    dates.add(temp);
                    temp = inputLine.substring(lastIndex + 20, lastIndex + 28);
                    times.add(temp);
                    lastIndex += 6;
                }
            }
//            while((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//                int index = inputLine.indexOf("clouds");
//                if(inputLine.contains("clouds")) {
//                    System.out.println(inputLine);
//                    inputLine = in.readLine();
//                    System.out.println(inputLine);
//                    cloudCover.add(Integer.parseInt(inputLine.substring(7)));
//                }
//            }
            in.close();
            con.disconnect();
        } catch (Exception e) {
            System.out.println("Error: Connection could not be made.");
        }
        System.out.println(cloudCover);
        System.out.println(dates);
        System.out.println(times);
        try (PrintWriter writer = new PrintWriter(new File(zip + ".csv"))) {
            StringBuilder sb = new StringBuilder();
            for(int cc:cloudCover) {
                sb.append(cc);
                sb.append(',');
            }
            sb.replace(sb.length()-1,sb.length(),"\n");
            for(String date:dates) {
                sb.append(date);
                sb.append(',');
            }
            sb.replace(sb.length()-1,sb.length(),"\n");
            for(String time:times) {
                sb.append(time);
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            writer.write(sb.toString());
            System.out.println(zip + ".csv written.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

//    private static class ParameterStringBuilder {
//        private static String getParamsString(Map<String, String> params) {
//                StringBuilder result = new StringBuilder();
//
//                for(Map.Entry<String, String> entry : params.entrySet()) {
//                    result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
//                    result.append("-");
//                    result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
//                    result.append("&");
//                }
//
//                String resultString = result.toString();
//                int len = resultString.length();
//                return len > 0 ? resultString.substring(0, len-1) : resultString;
//        }
//    }
}
