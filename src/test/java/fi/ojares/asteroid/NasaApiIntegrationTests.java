package fi.ojares.asteroid;

import com.fasterxml.jackson.databind.JsonNode;
import fi.ojares.asteroid.nasa.DateRange;
import fi.ojares.asteroid.nasa.NasaApiQuery;
import fi.ojares.asteroid.nasa.NasaAsteroidDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

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

    @Test
    @DisplayName("AsteroidService fetches data from production Nasa-api")
    public void cacheTest() {
        NasaAsteroidDataSource dataSource = spy(NasaAsteroidDataSource.class);
        AsteroidService asteroidService = new AsteroidService(dataSource);
        LocalDate startDate = LocalDate.of(2015, 12, 19);
        LocalDate endDate = LocalDate.of(2015, 12, 26);

        // Service is called 3 times with same parameters -> there should be only one api-call made
        asteroidService.getLargestAsteroid(startDate, endDate);
        asteroidService.getLargestAsteroid(startDate, endDate);
        asteroidService.getLargestAsteroid(startDate, endDate);

        Mockito.verify(dataSource, Mockito.times(1)).getAsteroids(any(DateRange.class));
    }
}
