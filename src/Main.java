import com.gridnine.testing.Flight;
import com.gridnine.testing.FlightBuilder;
import com.gridnine.testing.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();

        try {
            System.out.println("\nДоступные перелёты:");
            flights.stream().filter(Objects::nonNull).filter(
                    f -> !(f.getSegments().get(0).getDepartureDate().isBefore(LocalDateTime.now()))
            ).filter(
                    f -> f.getSegments().stream().noneMatch(s -> s.getDepartureDate().isAfter(s.getArrivalDate()))
            ).filter(
                    f -> {
                        ListIterator<Segment> listIterator = f.getSegments().listIterator();
                        Duration totalTimeOnEarth = Duration.ZERO;
                        LocalDateTime arrivalDatePrevious, departDateNext;
                        listIterator.next();
                        while (listIterator.hasNext()) {
                            arrivalDatePrevious = listIterator.previous().getArrivalDate();
                            listIterator.next();
                            departDateNext = listIterator.next().getDepartureDate();
                            totalTimeOnEarth = totalTimeOnEarth.plus(Duration.between(arrivalDatePrevious, departDateNext));
                            if (totalTimeOnEarth.compareTo(Duration.ofHours(2)) > 0) {
                                return false;
                            }
                        }
                        return true;
                    }
            ).forEach(System.out::println);

            System.out.println("\nПерелёты с вылетом до текущего момента времени:");
            flights.stream().filter(Objects::nonNull).filter(
                    f -> f.getSegments().get(0).getDepartureDate().isBefore(LocalDateTime.now())
            ).forEach(System.out::println);

            System.out.println("\nПерелёты с сегментами с датой прилёта раньше даты вылета:");
            flights.stream().filter(Objects::nonNull).filter(
                    f -> f.getSegments().stream().anyMatch(s -> s.getDepartureDate().isAfter(s.getArrivalDate()))
            ).forEach(System.out::println);

            System.out.println("\nПерелёты с пребыванием на земле в сумме больше 2 часов:");
            flights.stream().filter(Objects::nonNull).filter(
                    f -> {
                        ListIterator<Segment> listIterator = f.getSegments().listIterator();
                        Duration totalTimeOnEarth = Duration.ZERO;
                        LocalDateTime arrivalDatePrevious, departDateNext;
                        listIterator.next();
                        while (listIterator.hasNext()) {
                            arrivalDatePrevious = listIterator.previous().getArrivalDate();
                            listIterator.next();
                            departDateNext = listIterator.next().getDepartureDate();
                            totalTimeOnEarth = totalTimeOnEarth.plus(Duration.between(arrivalDatePrevious, departDateNext));
                            if (totalTimeOnEarth.compareTo(Duration.ofHours(2)) > 0) {
                                return true;
                            }
                        }
                        return false;
                    }
            ).forEach(System.out::println);
        } finally {
            System.out.println("\ndone");
        }
    }
}