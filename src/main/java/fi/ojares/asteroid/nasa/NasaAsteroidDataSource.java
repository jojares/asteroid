package fi.ojares.asteroid.nasa;

import com.fasterxml.jackson.databind.JsonNode;
import fi.ojares.asteroid.Asteroid;
import fi.ojares.asteroid.AsteroidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class NasaAsteroidDataSource implements AsteroidDataSource {

    private JsonNode rootNode;

    @Autowired
    public NasaAsteroidDataSource() {
        this.rootNode = null;
    }

    public NasaAsteroidDataSource(JsonNode rootNode) {
        this.rootNode = rootNode;
    }

    public Collection<Asteroid> getAsteroids(LocalDate startDate, LocalDate endDate) {

        JsonNode results;
        if (rootNode == null) {
            NasaApiQuery query = new NasaApiQuery(startDate, endDate);
            results = query.getResults();
        } else {
            results = rootNode;
        }

        Collection<Asteroid> asteroids = new ArrayList<>();

        for (JsonNode nearEarthObject : results.get("near_earth_objects")) {
            for (JsonNode nearEarthObjectChild : nearEarthObject) {

                String name  = parseName(nearEarthObjectChild);
                BigDecimal kilometers = parseKilometers(nearEarthObjectChild);
                BigDecimal diameter = parseDiameter(nearEarthObjectChild);
                LocalDate approachDate = parseApproachDate(nearEarthObjectChild);

                asteroids.add(new Asteroid(name, approachDate, kilometers, diameter));
            }
        }

        return asteroids;
    }

    private String parseName(JsonNode nearEarthObject) {
        return nearEarthObject.get("name").asText();
    }

    private BigDecimal parseDiameter(JsonNode nearEarthObjectChild) {
        return new BigDecimal(
                nearEarthObjectChild
                        .get("estimated_diameter")
                        .get("kilometers")
                        .get("estimated_diameter_max")
                        .asText());
    }

    private BigDecimal parseKilometers(JsonNode nearEarthObjectChild) {
        for (JsonNode closeApproachData : nearEarthObjectChild.get("close_approach_data")) {
            JsonNode missDistance = closeApproachData.get("miss_distance");
            return new BigDecimal(missDistance.get("kilometers").asText());
        }
        throw new RuntimeException("no kilometers found");
    }

    private LocalDate parseApproachDate(JsonNode nearEarthObjectChild) {
        for (JsonNode closeApproachData : nearEarthObjectChild.get("close_approach_data")) {
            return LocalDate.parse(closeApproachData.get("close_approach_date").asText());
        }
        throw new RuntimeException("no approach date found");
    }
}
