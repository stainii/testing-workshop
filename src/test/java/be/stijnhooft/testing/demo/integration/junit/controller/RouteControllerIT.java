package be.stijnhooft.testing.demo.integration.junit.controller;

import be.stijnhooft.testing.demo.TestingDemoApplication;
import be.stijnhooft.testing.demo.controller.RouteController;
import be.stijnhooft.testing.demo.dto.RouteStatus;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * This integration test will verify everything starting from the controller to the bottom,
 * only mocking external APIs and the database.
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestingDemoApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@TestPropertySource(properties = "i-rail.base-url=http://localhost:8089/")
public class RouteControllerIT {

    @Autowired
    private RouteController controller;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    private final String fromZottegemToBrusselsNorthUrl = "/?from=Zottegem" +
            "&to=Brussel-Noord" +
            "&time=0724" +
            "&date=" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) +
            "&arrdep=departure" +
            "&format=json";

    private final String fromBrusselsNorthToGhentSintPietersUrl = "/?from=Brussel-Noord" +
            "&to=Gent-Sint-Pieters" +
            "&time=1245" +
            "&date=" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) +
            "&arrdep=departure" +
            "&format=json";

    private final String fromGhentSintPietersToZottegemUrl = "/?from=Gent-Sint-Pieters" +
            "&to=Zottegem" +
            "&time=1714" +
            "&date=" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) +
            "&arrdep=departure" +
            "&format=json";

    @Test
    @DatabaseSetup("/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusWhenThereAreAlreadyDelaysInTheDatabase-initial.xml")
    @ExpectedDatabase(value = "/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusWhenThereAreAlreadyDelaysInTheDatabase-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    @DirtiesContext // when executing this test, data will be cached. By adding the annotation, Spring will recreate all beans, and so empty the cache.
    public void getAllRoutesAndTheirDelayStatusWhenThereAreAlreadyDelaysInTheDatabase() {
        // stub
        stubFor(get(urlEqualTo(fromZottegemToBrusselsNorthUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Zottegem-to-Brussels-North-60-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Brussels-North-to-Ghent-Sint-Pieters-no-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromGhentSintPietersToZottegemUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Ghent-Sint-Pieters-to-Zottegem-10-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        // execute
        List<RouteStatus> allRoutesAndTheirDelayStatus = controller.getAllRoutesAndTheirDelayStatus();

        // assert and verify
        assertEquals(3, allRoutesAndTheirDelayStatus.size());

        assertEquals(Long.valueOf(100), allRoutesAndTheirDelayStatus.get(0).getId());
        assertEquals("Zottegem", allRoutesAndTheirDelayStatus.get(0).getDepartureStation());
        assertEquals(LocalTime.of(7,24), allRoutesAndTheirDelayStatus.get(0).getDepartureTime());
        assertEquals("Brussel-Noord", allRoutesAndTheirDelayStatus.get(0).getArrivalStation());
        assertEquals(Integer.valueOf(60), allRoutesAndTheirDelayStatus.get(0).getCurrentDelay());
        assertEquals(Double.valueOf(21), allRoutesAndTheirDelayStatus.get(0).getAverageDelay());

        assertEquals(Long.valueOf(101), allRoutesAndTheirDelayStatus.get(1).getId());
        assertEquals("Brussel-Noord", allRoutesAndTheirDelayStatus.get(1).getDepartureStation());
        assertEquals(LocalTime.of(12,45), allRoutesAndTheirDelayStatus.get(1).getDepartureTime());
        assertEquals("Gent-Sint-Pieters", allRoutesAndTheirDelayStatus.get(1).getArrivalStation());
        assertEquals(Integer.valueOf(0), allRoutesAndTheirDelayStatus.get(1).getCurrentDelay());
        assertEquals(Double.valueOf(0), allRoutesAndTheirDelayStatus.get(1).getAverageDelay());

        assertEquals(Long.valueOf(102), allRoutesAndTheirDelayStatus.get(2).getId());
        assertEquals("Gent-Sint-Pieters", allRoutesAndTheirDelayStatus.get(2).getDepartureStation());
        assertEquals(LocalTime.of(17,14), allRoutesAndTheirDelayStatus.get(2).getDepartureTime());
        assertEquals("Zottegem", allRoutesAndTheirDelayStatus.get(2).getArrivalStation());
        assertEquals(Integer.valueOf(10), allRoutesAndTheirDelayStatus.get(2).getCurrentDelay());
        assertEquals(Double.valueOf(5), allRoutesAndTheirDelayStatus.get(2).getAverageDelay());

        verify(exactly(1), getRequestedFor(urlEqualTo(fromZottegemToBrusselsNorthUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromGhentSintPietersToZottegemUrl)));
    }

    @Test
    @DatabaseSetup("/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusWhenThereAreNoDelaysInTheDatabase-initial.xml")
    @ExpectedDatabase(value = "/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusWhenThereAreNoDelaysInTheDatabase-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    @DirtiesContext // when executing this test, data will be cached. By adding the annotation, Spring will recreate all beans, and so empty the cache.
    public void getAllRoutesAndTheirDelayStatusWhenThereAreNoDelaysInTheDatabase() {
        // stub
        stubFor(get(urlEqualTo(fromZottegemToBrusselsNorthUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Zottegem-to-Brussels-North-60-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Brussels-North-to-Ghent-Sint-Pieters-no-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromGhentSintPietersToZottegemUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Ghent-Sint-Pieters-to-Zottegem-10-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        // execute
        List<RouteStatus> allRoutesAndTheirDelayStatus = controller.getAllRoutesAndTheirDelayStatus();

        // assert and verify
        assertEquals(3, allRoutesAndTheirDelayStatus.size());

        assertEquals(Long.valueOf(100), allRoutesAndTheirDelayStatus.get(0).getId());
        assertEquals("Zottegem", allRoutesAndTheirDelayStatus.get(0).getDepartureStation());
        assertEquals(LocalTime.of(7,24), allRoutesAndTheirDelayStatus.get(0).getDepartureTime());
        assertEquals("Brussel-Noord", allRoutesAndTheirDelayStatus.get(0).getArrivalStation());
        assertEquals(Integer.valueOf(60), allRoutesAndTheirDelayStatus.get(0).getCurrentDelay());
        assertEquals(Double.valueOf(60), allRoutesAndTheirDelayStatus.get(0).getAverageDelay());

        assertEquals(Long.valueOf(101), allRoutesAndTheirDelayStatus.get(1).getId());
        assertEquals("Brussel-Noord", allRoutesAndTheirDelayStatus.get(1).getDepartureStation());
        assertEquals(LocalTime.of(12,45), allRoutesAndTheirDelayStatus.get(1).getDepartureTime());
        assertEquals("Gent-Sint-Pieters", allRoutesAndTheirDelayStatus.get(1).getArrivalStation());
        assertEquals(Integer.valueOf(0), allRoutesAndTheirDelayStatus.get(1).getCurrentDelay());
        assertEquals(Double.valueOf(0), allRoutesAndTheirDelayStatus.get(1).getAverageDelay());

        assertEquals(Long.valueOf(102), allRoutesAndTheirDelayStatus.get(2).getId());
        assertEquals("Gent-Sint-Pieters", allRoutesAndTheirDelayStatus.get(2).getDepartureStation());
        assertEquals(LocalTime.of(17,14), allRoutesAndTheirDelayStatus.get(2).getDepartureTime());
        assertEquals("Zottegem", allRoutesAndTheirDelayStatus.get(2).getArrivalStation());
        assertEquals(Integer.valueOf(10), allRoutesAndTheirDelayStatus.get(2).getCurrentDelay());
        assertEquals(Double.valueOf(10), allRoutesAndTheirDelayStatus.get(2).getAverageDelay());

        verify(exactly(1), getRequestedFor(urlEqualTo(fromZottegemToBrusselsNorthUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromGhentSintPietersToZottegemUrl)));
    }

    @Test
    @DatabaseSetup("/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusWhenThereAreAlreadyDelaysInTheDatabase-initial.xml")
    @ExpectedDatabase(value = "/datasets/RouteControllerIT-getAllRoutesAndTheirDelayStatusTwiceAndCheckIfTheCacheWorks-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("/datasets/clear.xml")
    @DirtiesContext // when executing this test, data will be cached. By adding the annotation, Spring will recreate all beans, and so empty the cache.
    public void getAllRoutesAndTheirDelayStatusTwiceAndCheckIfTheCacheWorks() {
        // stub
        stubFor(get(urlEqualTo(fromZottegemToBrusselsNorthUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Zottegem-to-Brussels-North-60-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Brussels-North-to-Ghent-Sint-Pieters-no-delay.json"))); // you can find this file under /src/test/resources/__files/

        stubFor(get(urlEqualTo(fromGhentSintPietersToZottegemUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Ghent-Sint-Pieters-to-Zottegem-10-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        // execute two times
        List<RouteStatus> firstAttempt = controller.getAllRoutesAndTheirDelayStatus();
        List<RouteStatus> secondAttempt = controller.getAllRoutesAndTheirDelayStatus();

        // assert
        assertEquals(firstAttempt, secondAttempt);

        assertEquals(3, secondAttempt.size());

        assertEquals(Long.valueOf(100), secondAttempt.get(0).getId());
        assertEquals("Zottegem", secondAttempt.get(0).getDepartureStation());
        assertEquals(LocalTime.of(7,24), secondAttempt.get(0).getDepartureTime());
        assertEquals("Brussel-Noord", secondAttempt.get(0).getArrivalStation());
        assertEquals(Integer.valueOf(60), secondAttempt.get(0).getCurrentDelay());
        assertEquals(Double.valueOf(21), secondAttempt.get(0).getAverageDelay());

        assertEquals(Long.valueOf(101), secondAttempt.get(1).getId());
        assertEquals("Brussel-Noord", secondAttempt.get(1).getDepartureStation());
        assertEquals(LocalTime.of(12,45), secondAttempt.get(1).getDepartureTime());
        assertEquals("Gent-Sint-Pieters", secondAttempt.get(1).getArrivalStation());
        assertEquals(Integer.valueOf(0), secondAttempt.get(1).getCurrentDelay());
        assertEquals(Double.valueOf(0), secondAttempt.get(1).getAverageDelay());

        assertEquals(Long.valueOf(102), secondAttempt.get(2).getId());
        assertEquals("Gent-Sint-Pieters", secondAttempt.get(2).getDepartureStation());
        assertEquals(LocalTime.of(17,14), secondAttempt.get(2).getDepartureTime());
        assertEquals("Zottegem", secondAttempt.get(2).getArrivalStation());
        assertEquals(Integer.valueOf(10), secondAttempt.get(2).getCurrentDelay());
        assertEquals(Double.valueOf(5), secondAttempt.get(2).getAverageDelay());

        // verify that all calls have been made only once!
        verify(exactly(1), getRequestedFor(urlEqualTo(fromZottegemToBrusselsNorthUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromBrusselsNorthToGhentSintPietersUrl)));
        verify(exactly(1), getRequestedFor(urlEqualTo(fromGhentSintPietersToZottegemUrl)));
    }
}
