package fi.ojares.asteroid;

import com.fasterxml.jackson.databind.JsonNode;
import fi.ojares.asteroid.nasa.NasaApiQuery;
import fi.ojares.asteroid.nasa.NasaAsteroidDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class NasaApiIntegrationTests {

    @Test
    @DisplayName("Nasa-api query returns any results")
    public void testNasaApi() {

        NasaApiQuery query = new NasaApiQuery(LocalDate.of(2015, 12, 19), LocalDate.of(2015, 12, 26));
        JsonNode results = query.getResults();
        
        assertThat(results, is(notNullValue()));
    }

    @Test
    @DisplayName("AsteroidService fetches data from production Nasa-api")
    public void integrationTest() {
        AsteroidService asteroidService = new AsteroidService(new NasaAsteroidDataSource());

        Asteroid nearest = asteroidService.getNearestAsteroid(LocalDate.of(2000, 12, 19), LocalDate.of(2000, 12, 26));
        Asteroid largest = asteroidService.getLargestAsteroid(LocalDate.of(2015, 12, 19), LocalDate.of(2015, 12, 26));
        
        assertThat(nearest, is(notNullValue()));
        assertThat(largest, is(notNullValue()));
    }
}
