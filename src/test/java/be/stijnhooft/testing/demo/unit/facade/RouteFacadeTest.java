package be.stijnhooft.testing.demo.unit.facade;

import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.facade.RouteFacade;
import be.stijnhooft.testing.demo.mapper.RouteStatusMapper;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.service.DelayService;
import be.stijnhooft.testing.demo.service.RouteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/** A simple JUnit test with Mockito **/
@RunWith(MockitoJUnitRunner.class)
public class RouteFacadeTest {

    @InjectMocks
    private RouteFacade routeFacade;

    @Mock
    private RouteService routeService;

    @Mock
    private DelayService delayService;

    @Mock
    private RouteStatusMapper mapper;

    @Test
    public void getCurrentStatusForAllRoutes() {
        // data
        Route route1 = new Route("Gent-Sint-Pieters", "Brussel-Noord", LocalTime.of(7,30));
        Route route2 = new Route("Aalst", "Gent-Sint-Pieters", LocalTime.of(18,8));
        Route route3 = new Route("Brussel-Noord", "Zottegem", LocalTime.of(9,36));

        Delay currentDelay1 = new Delay(2, LocalDateTime.of(2019, 3,8, 7, 32), route1, LocalDateTime.of(2019, 3,8, 7, 30, 10));
        Delay currentDelay2 = new Delay(5, LocalDateTime.of(2019, 3,8, 18, 12), route2, LocalDateTime.of(2019, 3,8, 7, 30, 10));
        Delay currentDelay3 = new Delay(0, LocalDateTime.of(2019, 3,8, 9, 36), route3, LocalDateTime.of(2019, 3,8, 7, 30, 10));

        double averageDelay1 = 13.0;
        double averageDelay2 = 3.2;
        double averageDelay3 = 0.4;

        RouteStatus routeStatus1 = new RouteStatus(null, "test1", null, null, 0, 0D);
        RouteStatus routeStatus2 = new RouteStatus(null, "test2", null, null, 0, 0D);
        RouteStatus routeStatus3 = new RouteStatus(null, "test3", null, null, 0, 0D);

        List<Route> allRoutes = Arrays.asList(route1, route2, route3);

        Map<Route, Delay> routesAndTheirCurrentDelay = new HashMap<>();
        routesAndTheirCurrentDelay.put(route1, currentDelay1);
        routesAndTheirCurrentDelay.put(route2, currentDelay2);
        routesAndTheirCurrentDelay.put(route3, currentDelay3);

        Map<Route, Double> routesAndTheirAverageDelay = new HashMap<>();
        routesAndTheirAverageDelay.put(route1, averageDelay1);
        routesAndTheirAverageDelay.put(route2, averageDelay2);
        routesAndTheirAverageDelay.put(route3, averageDelay3);


        // mock
        doReturn(allRoutes).when(routeService).findAll();
        doReturn(routesAndTheirCurrentDelay).when(delayService).findAndSaveCurrentDelayFor(allRoutes);
        doReturn(routesAndTheirAverageDelay).when(delayService).findAverageDelayFor(allRoutes);
        doReturn(routeStatus1).when(mapper).map(route1, currentDelay1, averageDelay1);
        doReturn(routeStatus2).when(mapper).map(route2, currentDelay2, averageDelay2);
        doReturn(routeStatus3).when(mapper).map(route3, currentDelay3, averageDelay3);


        // execute
        List<RouteStatus> result = routeFacade.getCurrentStatusForAllRoutes();

        // assert and verify
        assertEquals(3, result.size());
        assertEquals(routeStatus1, result.get(0));
        assertEquals(routeStatus2, result.get(1));
        assertEquals(routeStatus3, result.get(2));

        verify(routeService).findAll();
        verify(delayService).findAndSaveCurrentDelayFor(allRoutes);
        verify(delayService).findAverageDelayFor(allRoutes);
        verify(mapper).map(route1, currentDelay1, averageDelay1);
        verify(mapper).map(route2, currentDelay2, averageDelay2);
        verify(mapper).map(route3, currentDelay3, averageDelay3);
        verifyNoMoreInteractions(routeService, delayService, mapper);
    }
}
