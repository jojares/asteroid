package fi.ojares.asteroid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class AsteroidController {

    private AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("/")
    public String viewAsteroids(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        model.addAttribute("largest", asteroidService.getLargestAsteroid(startDate, endDate));

        // Fixed date range used for nearest Asteroid
        LocalDate startDateConstant = LocalDate.of(2015, 12, 19);
        LocalDate endDateConstant = LocalDate.of(2015, 12, 26);
        model.addAttribute("startDate", startDateConstant);
        model.addAttribute("endDate", endDateConstant);
        model.addAttribute("nearest", asteroidService.getNearestAsteroid(startDateConstant, endDateConstant));

        return "view-asteroids";
    }
}
