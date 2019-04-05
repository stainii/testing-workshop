package be.stijnhooft.testing.demo.repository;

import be.stijnhooft.testing.demo.model.Delay;
import be.stijnhooft.testing.demo.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DelayRepository extends JpaRepository<Delay, Long> {

    Optional<Delay> findFirstByRouteOrderByCreationDesc(Route route);

    @Query("select avg(delay.delay) " +
            "from Delay delay " +
            "where delay in " +
            "   (select max(delay2) " +
            "       from Delay delay2 " +
            "       where delay2.route = :route " +
            "       group by year(delay2.departure), month(delay2.departure), day(delay2.departure))")
    Optional<Double> findAverageDelayByRoute(Route route);

    @Modifying @Query("delete from Delay delay where delay.route.id = :id")
    void deleteForRoute(long id);
}
