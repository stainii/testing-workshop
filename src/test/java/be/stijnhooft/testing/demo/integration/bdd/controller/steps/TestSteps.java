package be.stijnhooft.testing.demo.integration.bdd.controller.steps;

import be.stijnhooft.testing.demo.controller.RouteController;
import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.DelayRepository;
import be.stijnhooft.testing.demo.repository.RouteRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.jbehave.core.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Component
public class TestSteps {

    @Autowired
    private RouteController routeController;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private DelayRepository delayRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private String fromBrusselsNorthToZottegemUrl = "/?from=Brussel-Noord" +
            "&to=Zottegem" +
            "&time=1000" +
            "&date=" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) +
            "&arrdep=departure" +
            "&format=json";

    // state
    private TransactionStatus transaction;
    private List<RouteStatus> allRoutesAndTheirDelayStatus;


    @BeforeScenario
    public void beforeScenario() {
        // start database transaction
        transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        // empty database
        delayRepository.deleteAll();
        routeRepository.deleteAll();

        // reset counter that counts how many times a request has been made
        WireMock.resetAllRequests();
    }

    // roll back transaction
    @AfterScenario
    public void afterScenario() {
        // rollback database tranaction
        if (transaction != null) {
            transactionManager.rollback(transaction);
        }
    }

    // methods
    @Given("there is 20 seconds delay from Brussels-North to Zottegem")
    public void insertRouteFromBrusselsNorthToZottegemInTheDatabaseAndExpectARESTCall() {
        routeRepository.saveAndFlush(new Route("Brussel-Noord", "Zottegem", LocalTime.of(10, 0)));
        stubFor(get(urlEqualTo(fromBrusselsNorthToZottegemUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Brussels-North-to-Zottegem-20-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/
    }

    @Given("at $date there was $delay seconds delay from Brussels-North to Zottegem")
    public void insertIntoDatabaseAnXMinuteDelayFromBrusselsNorthToZottegem(@Named("date") String date, @Named("delay") int delay) {
        Route route = routeRepository.findAll().get(0); // TODO: write a proper search function to find this route.
        delayRepository.saveAndFlush(new Delay(delay, LocalDate.parse(date).atTime(10,0), route, LocalDateTime.now()));
    }

    @Given("today there was $delay seconds delay from Brussels-North to Zottegem")
    public void insertIntoDatabaseAnXMinuteDelayFromBrusselsNorthToZottegemToday(@Named("date") String date, @Named("delay") int delay) {
        Route route = routeRepository.findAll().get(0); // TODO: write a proper search function to find this route.
        delayRepository.saveAndFlush(new Delay(delay, LocalDateTime.now(), route, LocalDateTime.now()));
    }

    @Given("there were no lookups in the past")
    public void thereWereNoLookupsInThePast() {
        // nothing to do
    }

    @When("I get all routes and their request status")
    public void getAllRoutes() {
        allRoutesAndTheirDelayStatus = routeController.getAllRoutesAndTheirDelayStatus();
    }

    @Then("I get $numberOfRoutes route back")
    public void countRoute(@Named("numberOfRoutes") int numberOfRoutes) {
        countRoutes(numberOfRoutes);
    }

    @Then("I get $numberOfRoutes routes back")
    public void countRoutes(@Named("numberOfRoutes") int numberOfRoutes) {
        assertEquals(numberOfRoutes, allRoutesAndTheirDelayStatus.size());
    }

    @Then("the current delay of route $routeIndex is $delay seconds")
    public void checkCurrentDelay(@Named("routeIndex") int routeIndex, @Named("delay") Integer delay) {
        assertEquals(delay, allRoutesAndTheirDelayStatus.get(routeIndex-1).getCurrentDelay());
    }

    @Then("the average delay of route $routeIndex is $delay seconds")
    public void checkAverageDelay(@Named("routeIndex") int routeIndex, @Named("delay") Double delay) {
        assertEquals(delay, allRoutesAndTheirDelayStatus.get(routeIndex-1).getAverageDelay());
    }

    @Then("the second lookup for Brussels-North to Zottegem was pulled out of the cache")
    public void checkCache() {
        verify(exactly(1), getRequestedFor(urlEqualTo(fromBrusselsNorthToZottegemUrl))); //only one call, the second call was cached
    }
}
