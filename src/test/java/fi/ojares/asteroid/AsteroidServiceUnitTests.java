package fi.ojares.asteroid;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.ojares.asteroid.nasa.NasaAsteroidDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AsteroidServiceUnitTests {

    @Test
    @DisplayName("Nearest asteroid is returned from given collection")
    void getNearestAsteroid() {

        Asteroid nearest = new Asteroid("Nearest", LocalDate.of(2015, 12, 20), new BigDecimal("500"), BigDecimal.ZERO);
        List<Asteroid> asteroids = List.of(
                new Asteroid("Asteroid A", LocalDate.of(2015, 12, 19), new BigDecimal("1000"), BigDecimal.ZERO),
                new Asteroid("Asteroid B", LocalDate.of(2015, 12, 21), new BigDecimal("2000"), BigDecimal.ZERO),
                nearest,
                new Asteroid("Asteroid C", LocalDate.of(2015, 12, 26), new BigDecimal("3000"), BigDecimal.ZERO)
        );
        AsteroidService asteroidService = new AsteroidService((startDate, endDate) -> asteroids);

        Asteroid actual = asteroidService.getNearestAsteroid(LocalDate.MIN, LocalDate.MAX);

        assertThat(actual, equalTo(nearest));
    }

    @Test
    @DisplayName("Nearest asteroid is returned from given json")
    public void testNearestAsteroidFromJson() throws IOException {
        URL testJson = getClass().getResource("/asteroids.json");
        ObjectMapper objectMapper = new ObjectMapper();
        AsteroidDataSource dataSource = new NasaAsteroidDataSource(objectMapper.readTree(testJson));
        AsteroidService asteroidService = new AsteroidService(dataSource);

        Asteroid asteroid = asteroidService.getNearestAsteroid(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 1));

        assertThat(asteroid.getDistanceInKilometers(), equalTo(new BigDecimal("2450214.654065658")));
    }

    @Test
    @DisplayName("Largest asteroid is returned from given collection")
    void getLargestAsteroid() {

        Asteroid largest = new Asteroid("Nearest", LocalDate.of(2015, 12, 20), BigDecimal.ZERO, new BigDecimal("9000"));
        List<Asteroid> asteroids = List.of(
                new Asteroid("Asteroid A", LocalDate.of(2015, 12, 19), BigDecimal.ZERO, new BigDecimal("1000")),
                new Asteroid("Asteroid B", LocalDate.of(2015, 12, 21), BigDecimal.ZERO, new BigDecimal("2000")),
                largest,
                new Asteroid("Asteroid C", LocalDate.of(2015, 12, 26), BigDecimal.ZERO, new BigDecimal("3000"))
        );
        AsteroidService asteroidService = new AsteroidService((startDate, endDate) -> asteroids);

        Asteroid actual = asteroidService.getLargestAsteroid(LocalDate.MIN, LocalDate.MAX);

        assertThat(actual, equalTo(largest));
    }

    @Test
    @DisplayName("Largest asteroid is returned from given json")
    public void testLargestAsteroidFromJson() throws IOException {
        URL testJson = getClass().getResource("/asteroids.json");
        ObjectMapper objectMapper = new ObjectMapper();
        AsteroidDataSource dataSource = new NasaAsteroidDataSource(objectMapper.readTree(testJson));
        AsteroidService asteroidService = new AsteroidService(dataSource);

        Asteroid asteroid = asteroidService.getLargestAsteroid(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 1));

        assertThat(asteroid.getDiameterInKilometers(), equalTo(new BigDecimal("0.8204270649")));
    }
}