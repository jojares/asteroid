package fi.ojares.asteroid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

@Service
public class AsteroidService {

    private AsteroidDataSource source;

    @Autowired
    public AsteroidService(AsteroidDataSource source) {
        this.source = source;
    }

    public Asteroid getNearestAsteroid(LocalDate startDate, LocalDate endDate) {

        Collection<Asteroid> asteroids = source.getAsteroids(startDate, endDate);

        return asteroids
                .stream()
                .sorted(Comparator.comparing(Asteroid::getDistanceInKilometers))
                .findFirst()
                .orElse(null);
    }

    public Asteroid getLargestAsteroid(LocalDate startDate, LocalDate endDate) {

        Collection<Asteroid> asteroids = source.getAsteroids(startDate, endDate);

        return asteroids
                .stream()
                .sorted(Comparator
                                .comparing(Asteroid::getDiameterInKilometers)
                                .reversed())
                .findFirst()
                .orElse(null);
    }
}
