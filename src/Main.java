import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        URL url;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/forecast");
        } catch (Exception MalformedURLException) { }
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    }
}
