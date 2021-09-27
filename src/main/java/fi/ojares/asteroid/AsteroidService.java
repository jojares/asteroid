package fi.ojares.asteroid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AsteroidService {

    private AsteroidDataSource source;

    @Autowired
    public AsteroidService(AsteroidDataSource source) {
        this.source = source;
    }

    public Asteroid getNearestAsteroid(LocalDate startDate, LocalDate endDate) {

        Collection<Range> ranges = getRanges(startDate, endDate);
        Collection<Asteroid> asteroids = new ArrayList<>();

        for (Range range : ranges) {
            asteroids.addAll(source.getAsteroids(range.startDate, range.endDate));
        }

        return asteroids.stream()
                .sorted(Comparator.comparing(Asteroid::getDistanceInKilometers))
                .findFirst()
                .orElse(null);
    }

    public Asteroid getLargestAsteroid(LocalDate startDate, LocalDate endDate) {

        Collection<Range> ranges = getRanges(startDate, endDate);
        Collection<Asteroid> asteroids = new ArrayList<>();

        for (Range range : ranges) {
            asteroids.addAll(source.getAsteroids(range.startDate, range.endDate));
        }

        return asteroids.stream()
                .sorted(Comparator.comparing(Asteroid::getDiameterInKilometers).reversed())
                .findFirst()
                .orElse(null);
    }

    private Collection<Range> getRanges(LocalDate startDate, LocalDate endDate) {

        long differenceInDays = DAYS.between(startDate, endDate);
        Collection<Range> ranges = new ArrayList<>();

        if (differenceInDays > 7) {
            ranges.add(new Range(startDate, startDate.plusDays(7)));
            ranges.addAll(getRanges(startDate.plusDays(8), endDate));
        } else {
            ranges.add(new Range(startDate, endDate));
        }
        return ranges;
    }

    class Range {

        private LocalDate startDate;
        private LocalDate endDate;

        public Range(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }
}
