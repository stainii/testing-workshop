package be.stijnhooft.testing.demo.unit.repository;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.IRailRepository;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

/** This unit test demonstrates the use of Wirmock.
 * Wiremock will set up a fake endpoint, mimicking the iRail API.
 *
 * Afterwards, we can ask Wiremock if the expected calls have been made.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "i-rail.base-url=http://localhost:8089/")
public class IRailRepositoryTest {

    @Autowired
    private IRailRepository iRailRepository;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    @Test
    public void callIRailApi() {
        // data set
        // based on this route...
        Route route = new Route("Gent-Sint-Pieters", "Poperinge", LocalTime.of(12, 36));
        // ...this url should be called
        String expectedUrl = "/?from=Gent-Sint-Pieters" +
                "&to=Poperinge" +
                "&time=1236" +
                "&date=" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) +
                "&arrdep=departure" +
                "&format=json";

        // stub
        stubFor(get(urlEqualTo(expectedUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBodyFile("Ghent-Sint-Pieters-to-Poperinge-10-seconds-delay.json"))); // you can find this file under /src/test/resources/__files/

        // execute
        Optional<Delay> delay = iRailRepository.getLatestDelay(route);


        // assert
        assertTrue(delay.isPresent());
        assertEquals(route, delay.get().getRoute());
        assertEquals(Integer.valueOf(10), delay.get().getDelay());
        assertNotNull(delay.get().getDeparture());
        assertNull(delay.get().getId());
    }

}
