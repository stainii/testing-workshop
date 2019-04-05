package be.stijnhooft.testing.demo.repository;

import be.stijnhooft.testing.demo.dto.IRailConnection;
import be.stijnhooft.testing.demo.dto.IRailConnections;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class IRailRepository {

    private Logger logger = LoggerFactory.getLogger(IRailRepository.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${i-rail.base-url}")
    private String baseUrl;

    public Optional<Delay> getLatestDelay(Route route) {
        LocalDateTime dateTimeToCheckDelayFor = assembleDateTimeToCheckDelayFor(route);
        Optional<IRailConnection> iRailConnection = callIRailApi(route, dateTimeToCheckDelayFor);
        return mapToDelay(route, dateTimeToCheckDelayFor, iRailConnection);
    }

    private Optional<Delay> mapToDelay(Route route, LocalDateTime dateTimeToCheckDelayFor, Optional<IRailConnection> iRailConnection) {
        return iRailConnection.map(i -> new Delay(i.getDeparture().getDelay(), dateTimeToCheckDelayFor, route, LocalDateTime.now()));
    }

    private Optional<IRailConnection> callIRailApi(Route route, LocalDateTime dateTimeToCheckDelayFor) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("from", route.getDepartureStation())
                .queryParam("to", route.getArrivalStation())
                .queryParam("time", dateTimeToCheckDelayFor.format(DateTimeFormatter.ofPattern("HHmm")))
                .queryParam("date", dateTimeToCheckDelayFor.format(DateTimeFormatter.ofPattern("ddMMyy")))
                .queryParam("arrdep", "departure")
                .queryParam("format", "json")
                .build().encode().toUriString();

        try {
            IRailConnections iRailConnections = restTemplate.getForObject(url, IRailConnections.class);
            if (iRailConnections != null && iRailConnections.getConnection() != null && iRailConnections.getConnection().get(0) != null) {
                return Optional.of(iRailConnections.getConnection().get(0));
            } else {
                return Optional.empty();
            }
        } catch (RuntimeException e) {
            logger.error("Could not reach the IRail API", e);
            return Optional.empty();
        }
    }

    private LocalDateTime assembleDateTimeToCheckDelayFor(Route route) {
        return LocalDateTime.now()
                .withHour(route.getDepartureTime().getHour())
                .withMinute(route.getDepartureTime().getMinute())
                .withSecond(route.getDepartureTime().getSecond())
                .withNano(route.getDepartureTime().getNano());
    }

}
