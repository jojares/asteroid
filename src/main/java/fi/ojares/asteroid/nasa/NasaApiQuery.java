package fi.ojares.asteroid.nasa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class NasaApiQuery {

    private final String apiRoot = "https://api.nasa.gov/neo/rest/v1/feed?";
    private final String apiKey = "DEMO_KEY";
    private final long NASA_API_DAY_LIMIT = 7;

    private LocalDate startDate, endDate;

    public NasaApiQuery(LocalDate startDate, LocalDate endDate) {

        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        long differenceInDays = DAYS.between(startDate, endDate);

        if (differenceInDays > NASA_API_DAY_LIMIT) {
            throw new IllegalArgumentException(String.format("difference in days %d exceeds allowed limit: %d", differenceInDays, NASA_API_DAY_LIMIT));
        }

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
