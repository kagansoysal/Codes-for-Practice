import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class WebScraper {
    public String pullDatas(String season) {
        String HTMLOfPots = "";

        try {
            String apiUrl = "https://tr.wikipedia.org/wiki/20" + season + "_UEFA_Şampiyonlar_Ligi";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;

            response = reader.lines().collect(Collectors.joining());
            reader.close();

            String jsonResponse = response;
            HTMLOfPots = jsonResponse.substring(jsonResponse.indexOf("<caption>1. Torba"), jsonResponse.indexOf("\"Gruplar\"") - 80);

        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Select a season after 2013-14\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTMLOfPots;
    }
}