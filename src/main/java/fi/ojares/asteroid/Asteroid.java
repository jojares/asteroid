package fi.ojares.asteroid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Asteroid {

    private String name;
    private LocalDate approachDate;
    private BigDecimal distanceInKilometers;
    private BigDecimal diameterInKilometers;

    public Asteroid(String name, LocalDate approachDate, BigDecimal distanceInKilometers, BigDecimal diameter) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(approachDate);
        Objects.requireNonNull(distanceInKilometers);
        Objects.requireNonNull(diameter);

        this.name = name;
        this.approachDate = approachDate;
        this.distanceInKilometers = distanceInKilometers;
        this.diameterInKilometers = diameter;
    }

    public String getName() {
        return name;
    }

    public LocalDate getApproachDate() {
        return approachDate;
    }

    public BigDecimal getDistanceInKilometers() {
        return distanceInKilometers;
    }

    public BigDecimal getDiameterInKilometers() {
        return diameterInKilometers;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Asteroid.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("approachDate=" + approachDate)
                .add("distanceInKilometers=" + distanceInKilometers)
                .add("diameter=" + diameterInKilometers)
                .toString();
    }
}
