package be.stijnhooft.testing.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="departure_station",
            nullable = false)
    private String departureStation;

    @Column(name="departure_time",
            nullable = false)
    private LocalTime departureTime;

    @Column(name="arrival_station",
            nullable = false)
    private String arrivalStation;

    public Route() {
    }

    public Route(String departureStation, String arrivalStation, LocalTime departureTime) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) &&
                Objects.equals(departureStation, route.departureStation) &&
                Objects.equals(arrivalStation, route.arrivalStation) &&
                Objects.equals(departureTime, route.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureStation, arrivalStation, departureTime);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", departureStation='" + departureStation + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalStation='" + arrivalStation + '\'' +
                '}';
    }
}
