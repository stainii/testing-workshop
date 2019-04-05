package be.stijnhooft.testing.demo.unit.service;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.DelayRepository;
import be.stijnhooft.testing.demo.repository.RouteRepository;
import be.stijnhooft.testing.demo.service.DelayService;
import be.stijnhooft.testing.demo.service.helper.LatestDelayFetcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/** A simple JUnit test with Mockito **/
@RunWith(MockitoJUnitRunner.class)
public class DelayServiceTest {

    @Mock
    private DelayRepository delayRepository;

    @Mock
    private LatestDelayFetcher latestDelayFetcher;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private DelayService delayService;

    @Test
    public void findAndSaveCurrentDelayFor() {
        // data set
        Route route1 = new Route("Gent-Sint-Pieters", "Brussel-Noord", LocalTime.of(7,30));
        Route route2 = new Route("Aalst", "Gent-Sint-Pieters", LocalTime.of(18,8));
        Route route3 = new Route("Brussel-Noord", "Zottegem", LocalTime.of(9,36));
        List<Route> routes = Arrays.asList(route1, route2, route3);

        Delay latestDelayForRoute1 = new Delay(2, LocalDateTime.of(2019, Month.FEBRUARY, 27, 7, 30), route1, LocalDateTime.now());
        Delay latestDelayForRoute3 = new Delay(30, LocalDateTime.of(2019, Month.FEBRUARY, 25, 9, 36), route3, LocalDateTime.now());

        // mock
        doReturn(Optional.of(latestDelayForRoute1)).when(latestDelayFetcher).fetchLatestDelay(route1);
        doReturn(Optional.empty()).when(latestDelayFetcher).fetchLatestDelay(route2);
        doReturn(Optional.of(latestDelayForRoute3)).when(latestDelayFetcher).fetchLatestDelay(route3);

        // execute
        Map<Route, Delay> statusForAllRoutes = delayService.findAndSaveCurrentDelayFor(routes);

        // verify
        verify(latestDelayFetcher).fetchLatestDelay(route1);
        verify(delayRepository).save(latestDelayForRoute1);
        verify(latestDelayFetcher).fetchLatestDelay(route2);
        verify(latestDelayFetcher).fetchLatestDelay(route3);
        verify(delayRepository).save(latestDelayForRoute3);
        verifyNoMoreInteractions(delayRepository, routeRepository);

        // assert
        assertEquals(2, statusForAllRoutes.size());
        assertEquals(latestDelayForRoute1, statusForAllRoutes.get(route1));
        assertEquals(latestDelayForRoute3, statusForAllRoutes.get(route3));
        assertNull(statusForAllRoutes.get(route2));
    }

    @Test
    public void findAverageDelayFor() {
        // data set
        Route route1 = new Route("Gent-Sint-Pieters", "Brussel-Noord", LocalTime.of(7,30));
        Route route2 = new Route("Aalst", "Gent-Sint-Pieters", LocalTime.of(18,8));
        Route route3 = new Route("Brussel-Noord", "Zottegem", LocalTime.of(9,36));
        List<Route> routes = Arrays.asList(route1, route2, route3);

        // mock
        doReturn(Optional.of(10.03)).when(delayRepository).findAverageDelayByRoute(route1);
        doReturn(Optional.empty()).when(delayRepository).findAverageDelayByRoute(route2);
        doReturn(Optional.of(3.34)).when(delayRepository).findAverageDelayByRoute(route3);

        // execute
        Map<Route, Double> averageDelayForAllRoutes = delayService.findAverageDelayFor(routes);

        // verify
        verify(delayRepository).findAverageDelayByRoute(route1);
        verify(delayRepository).findAverageDelayByRoute(route2);
        verify(delayRepository).findAverageDelayByRoute(route3);
        verifyNoMoreInteractions(delayRepository, routeRepository);

        // assert
        assertEquals(3, averageDelayForAllRoutes.size());
        assertEquals(Double.valueOf(10.03), averageDelayForAllRoutes.get(route1));
        assertEquals(Double.valueOf(0.0), averageDelayForAllRoutes.get(route2));
        assertEquals(Double.valueOf(3.34), averageDelayForAllRoutes.get(route3));
    }

}
