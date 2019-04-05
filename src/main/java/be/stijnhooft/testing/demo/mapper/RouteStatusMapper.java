package be.stijnhooft.testing.demo.mapper;

import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import org.springframework.stereotype.Component;

@Component
public class RouteStatusMapper {

    public RouteStatus map(Route route, Delay currentDelay, Double averageDelay) {
        return new RouteStatus(route.getId(), route.getDepartureStation(), route.getDepartureTime(), route.getArrivalStation(), currentDelay.getDelay(), averageDelay);
    }
}
