package be.stijnhooft.testing.demo.unit.mapper;

import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.mapper.RouteStatusMapper;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

/**
 * A simple JUnit test without any frameworks besides JUnit.
 **/
public class RouteStatusMapperTest {

    private RouteStatusMapper mapper;

    @Before
    public void init() {
        mapper = new RouteStatusMapper();
    }

    @Test
    public void map() {
        // data set
        long routeId = 1L;
        String departureStation = "Gent-St-Pieters";
        Integer currentDelayValue = 15;
        LocalTime departureTime = LocalTime.of(12, 16);
        String arrivalStation = "Aalst";
        Double averageDelay = 12.05;

        Route route = new Route();
        route.setId(routeId);
        route.setDepartureStation(departureStation);
        route.setDepartureTime(departureTime);
        route.setArrivalStation(arrivalStation);

        Delay currentDelay = new Delay();
        currentDelay.setId(10L);
        currentDelay.setRoute(route);
        currentDelay.setDelay(currentDelayValue);

        // execute
        RouteStatus result = mapper.map(route, currentDelay, averageDelay);

        // assert
        assertEquals(Long.valueOf(1), result.getId());
        assertEquals(departureStation, result.getDepartureStation());
        assertEquals(departureTime, result.getDepartureTime());
        assertEquals(arrivalStation, result.getArrivalStation());
        assertEquals(currentDelayValue, result.getCurrentDelay());
        assertEquals(averageDelay, result.getAverageDelay());
    }
}
