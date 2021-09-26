package fi.ojares.asteroid.nasa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class NasaApiQuery {

    private final String apiRoot = "https://api.nasa.gov/neo/rest/v1/feed?";
    //private final String apiKey = "PS895iRsxk8JgWDNC6LkTJynIi9eEClOxTlHSS7k";
    private final String apiKey = "DEMO_KEY";

    private LocalDate startDate, endDate;

    public NasaApiQuery(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public JsonNode getResults() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL(apiRoot + "start_date=" + this.startDate.toString() + "&end_date=" + this.endDate.toString() + "&api_key=" + apiKey);
            JsonNode jsonNode = mapper.readTree(url);
            return jsonNode;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
