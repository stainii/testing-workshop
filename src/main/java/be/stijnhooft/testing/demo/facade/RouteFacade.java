package be.stijnhooft.testing.demo.facade;

import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.mapper.RouteStatusMapper;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.service.DelayService;
import be.stijnhooft.testing.demo.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class RouteFacade {

    @Autowired
    private RouteService routeService;

    @Autowired
    private DelayService delayService;

    @Autowired
    private RouteStatusMapper mapper;

    public List<RouteStatus> getCurrentStatusForAllRoutes() {
        List<Route> allRoutes = routeService.findAll();
        Map<Route, Delay> routesAndTheirCurrentDelay = delayService.findAndSaveCurrentDelayFor(allRoutes);
        Map<Route, Double> routesAndTheirAverageDelay = delayService.findAverageDelayFor(allRoutes);

        return allRoutes.stream()
                .map(route -> mapper.map(route, routesAndTheirCurrentDelay.get(route), routesAndTheirAverageDelay.get(route)))
                .collect(Collectors.toList());
    }

    public void deleteRoute(long id) {
        delayService.deleteAllDelaysForRoute(id);
        routeService.delete(id);
    }

    public void createRoute(Route route) {
        routeService.create(route);
    }
}
