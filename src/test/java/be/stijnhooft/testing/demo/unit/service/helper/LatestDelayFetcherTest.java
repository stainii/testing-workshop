package be.stijnhooft.testing.demo.unit.service.helper;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.IRailRepository;
import be.stijnhooft.testing.demo.service.helper.LatestDelayFetcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/** A simple JUnit test with Mockito **/
@RunWith(MockitoJUnitRunner.class)
public class LatestDelayFetcherTest {

    @InjectMocks
    private LatestDelayFetcher latestDelayFetcher;

    @Mock
    private IRailRepository iRailRepository;

    @Test
    public void testCache() {
        Route route = new Route();
        Delay delay = new Delay();

        doReturn(Optional.of(delay)).when(iRailRepository).getLatestDelay(route);

        Optional<Delay> firstResult = latestDelayFetcher.fetchLatestDelay(route);
        Optional<Delay> secondResult = latestDelayFetcher.fetchLatestDelay(route);

        assertSame(firstResult, secondResult);
        assertSame(delay, firstResult.get());

        verify(iRailRepository, times(1)).getLatestDelay(route);
        verifyNoMoreInteractions(iRailRepository);
    }
}
