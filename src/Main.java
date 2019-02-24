import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static URL url;
    private static HttpsURLConnection con;
    private static Map<String, String> parameters = new HashMap<>();
    private static DataOutputStream out;

    public static void main(String[] args) {
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/forecast");
//        } catch (MalformedURLException e) {
//            System.out.println("Error: The URL is malformed.");
//        }
//        try {
            con = (HttpsURLConnection) url.openConnection();
//        } catch (IOException e) {
//            System.out.println("Error: A connection could not be opened.");
//        }
//        try {
            con.setRequestMethod("GET");
//        } catch (ProtocolException e) {
//            System.out.println("Error: The request method is invalid.");
//        }
            parameters.put("zip", "30332");
            parameters.put("appid", "5b1eae5aa03d118038e19b8a1b2e61ac");

            con.setDoOutput(true);
//        try {
            out = new DataOutputStream(con.getOutputStream());
//        } catch (IOException e) {
//            System.out.println("Error: The output stream does not exist.");
//        }
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Error: Connection configuration is incorrect.");
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        try {
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            ///* continue from here *///
        } catch (Exception e) {
            System.out.println("Error: Connection could not be made.");
        }
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
