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
            System.out.println(status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                int index = inputLine.indexOf("clouds");
                if(inputLine.contains("clouds")) {
                    System.out.println(inputLine);
                    inputLine = in.readLine();
                    System.out.println(inputLine);
                    cloudCover.add(Integer.parseInt(inputLine.substring(7)));
                }
            }
            in.close();
            con.disconnect();
        } catch (Exception e) {
            System.out.println("Error: Connection could not be made.");
        }
        System.out.println(content);
        System.out.println(cloudCover);
    }

    private static class ParameterStringBuilder {
        private static String getParamsString(Map<String, String> params) {
                StringBuilder result = new StringBuilder();

                for(Map.Entry<String, String> entry : params.entrySet()) {
                    result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                    result.append("-");
                    result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                    result.append("&");
                }

                String resultString = result.toString();
                int len = resultString.length();
                return len > 0 ? resultString.substring(0, len-1) : resultString;
        }
    }
}
