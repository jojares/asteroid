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
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public class NasaAsteroidDataSource implements AsteroidDataSource {

    private final int NASA_API_RANGE_LIMIT = 7;
    private Map<DateRange, Collection<Asteroid>> cache = new HashMap<>();
    private JsonNode rootNode;

    @Autowired
    public NasaAsteroidDataSource() {
        this.rootNode = null;
    }

    public NasaAsteroidDataSource(JsonNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public Collection<Asteroid> getAsteroids(LocalDate startDate, LocalDate endDate) {

        DateRange range = new DateRange(startDate, endDate);
        if (cache.containsKey(range)) {
            return cache.get(range);
        }

        Collection<Asteroid> asteroids = new ArrayList<>();
        Collection<DateRange> dateRanges = getRanges(startDate, endDate);

        for (DateRange dateRange : dateRanges) {
            asteroids.addAll(this.getAsteroids(dateRange));
        }

        cache.put(range, asteroids);
        return asteroids;
    }

    public Collection<Asteroid> getAsteroids(DateRange dateRange) {

        JsonNode results;
        if (rootNode == null) {
            NasaApiQuery query = new NasaApiQuery(dateRange.getStartDate(), dateRange.getEndDate());
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

    private Collection<DateRange> getRanges(LocalDate startDate, LocalDate endDate) {

        long differenceInDays = DAYS.between(startDate, endDate);

        Collection<DateRange> dateRanges = new ArrayList<>();

        if (differenceInDays > NASA_API_RANGE_LIMIT) {
            dateRanges.add(new DateRange(startDate, startDate.plusDays(7)));
            dateRanges.addAll(getRanges(startDate.plusDays(8), endDate));
        } else {
            dateRanges.add(new DateRange(startDate, endDate));
        }
        return dateRanges;
    }
}
