package be.stijnhooft.testing.demo.service.helper;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.IRailRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class LatestDelayFetcher {

    private LoadingCache<Route, Optional<Delay>> cache;

    @Autowired
    private IRailRepository iRailRepository;

    public LatestDelayFetcher() {
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .build(CacheLoader.from(route -> iRailRepository.getLatestDelay(route)));
    }

    public Optional<Delay> fetchLatestDelay(Route route) {
        return cache.getUnchecked(route);
    }
}
