package be.stijnhooft.testing.demo.integration.junit.repository;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.IRailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests the integration with the actual IRail web service.
 *
 * Since the IRail web service has a rate limit, we don't want to execute this test too often.
 * Therefore, this test will not be run during a normal build. To run this test, you'll need to build with the integration-tests profile.
 *
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class IRailRepositoryIT {

    @Autowired
    private IRailRepository iRailRepository;

    @Test
    public void callIRailApi() {
        Route route = new Route("Gent-Sint-Pieters", "Poperinge", LocalTime.of(12, 36));
        Optional<Delay> delay = iRailRepository.getLatestDelay(route);
        assertTrue(delay.isPresent());
        assertEquals(route, delay.get().getRoute());

        //pop quiz: why can't I check for the actual value?
        assertNotNull(delay.get().getDelay());
        assertNotNull(delay.get().getDeparture());
    }

}
