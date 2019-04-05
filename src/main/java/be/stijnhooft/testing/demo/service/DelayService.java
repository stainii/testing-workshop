package be.stijnhooft.testing.demo.service;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.DelayRepository;
import be.stijnhooft.testing.demo.service.helper.LatestDelayFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DelayService {

    @Autowired
    private DelayRepository delayRepository;

    @Autowired
    private LatestDelayFetcher latestDelayFetcher;

    public Map<Route, Delay> findAndSaveCurrentDelayFor(List<Route> routes) {
        return routes.stream()                                               // loop over all routes
                .map(route -> latestDelayFetcher.fetchLatestDelay(route))    // fetch the latest delay
                .filter(Optional::isPresent)                                 // the latest delay is wrapped in an optional. Keep only those that actually have a value
                .map(Optional::get)                                          // unwrap that delay out of the optional
                .peek(delay -> delayRepository.save(delay))                  // persist each delay in the database, so that we can calculate the averages later
                .collect(Collectors.toMap(Delay::getRoute, delay -> delay)); // create a map with the route as a key, and the delay as a value
    }


    public Map<Route, Double> findAverageDelayFor(List<Route> routes) {
        HashMap<Route, Double> averagesForAllRoutes = new HashMap<>();
        for (Route route: routes) {
            averagesForAllRoutes.put(route, delayRepository.findAverageDelayByRoute(route).orElse(0.0));
        }
        return averagesForAllRoutes;
    }

    public void deleteAllDelaysForRoute(long id) {
        delayRepository.deleteForRoute(id);
    }
}
