package be.stijnhooft.testing.demo.unit.repository;


import be.stijnhooft.testing.demo.TestingDemoApplication;
import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.DelayRepository;
import be.stijnhooft.testing.demo.repository.RouteRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

/** In this test, we check the working of repository.
 * We set up a fake (in-memory) database, to avoid being dependent on an actual database.
 * Before each test, a data set is inserted. This dataset is cleared after the test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestingDemoApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@Transactional
public class DelayRepositoryTest {

    @Autowired
    private DelayRepository delayRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findFirstByRouteOrderByCreationDescWhenSuccess() {
        Route route = routeRepository.findById(100L).get();

        Optional<Delay> delay = delayRepository.findFirstByRouteOrderByCreationDesc(route);

        assertTrue(delay.isPresent());
        assertEquals(Long.valueOf(201), delay.get().getId());
        assertEquals(Integer.valueOf(3), delay.get().getDelay());
        assertEquals(route, delay.get().getRoute());
        assertEquals(LocalDateTime.of(2019, 3, 7, 7, 24), delay.get().getDeparture());

    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findFirstByRouteOrderByCreationDescWhenThereIsNoDepartureForTheRoute() {
        Route route = routeRepository.findById(101L).get();
        Optional<Delay> delay = delayRepository.findFirstByRouteOrderByCreationDesc(route);
        assertFalse(delay.isPresent());
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAverageDelayByRouteWhenThereAreNoDelaysForTheRoute() {
        Route route = routeRepository.findById(101L).get();
        Optional<Double> delay = delayRepository.findAverageDelayByRoute(route);
        assertFalse(delay.isPresent());
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAverageDelayByRouteWhenThereIsOnlyOneDelayPerDayForThatRoute() {
        Route route = routeRepository.findById(100L).get();
        Optional<Double> delay = delayRepository.findAverageDelayByRoute(route);
        assertTrue(delay.isPresent());
        assertEquals(1.5, delay.get(), 3);
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-initial.xml")
    @DatabaseTearDown("/datasets/clear.xml")
    public void findAverageDelayByRouteWhenThereAreMultipleDelaysPerDayForThatRoute() {
        Route route = routeRepository.findById(102L).get();
        Optional<Double> delay = delayRepository.findAverageDelayByRoute(route);
        assertTrue(delay.isPresent());
        assertEquals(10.00, delay.get(), 3);
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-save-initial.xml")
    @ExpectedDatabase(value = "/datasets/DelayRepositoryTest-save-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void save() {
        Route route = routeRepository.findById(101L).get();
        Delay delay = new Delay(2, LocalDateTime.of(2019, 3,8, 7, 45), route, LocalDateTime.of(2019, 3,8, 7, 30, 10));
        delayRepository.saveAndFlush(delay);
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-delete-initial.xml")
    @ExpectedDatabase(value = "/datasets/DelayRepositoryTest-deleteWhenSuccess-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void deleteWhenSuccess() {
        delayRepository.deleteForRoute(100);
    }

    @Test
    @DatabaseSetup("/datasets/DelayRepositoryTest-delete-initial.xml")
    @ExpectedDatabase(value = "/datasets/DelayRepositoryTest-delete-initial.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("/datasets/clear.xml")
    public void deleteWhenNoDelaysExistForThatRouteId() {
        delayRepository.deleteForRoute(0);
    }
}
