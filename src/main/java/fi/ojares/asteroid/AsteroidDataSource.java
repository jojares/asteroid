package fi.ojares.asteroid;

import java.time.LocalDate;
import java.util.Collection;

public interface AsteroidDataSource {

    Collection<Asteroid> getAsteroids(LocalDate startDate, LocalDate endDate);
}
