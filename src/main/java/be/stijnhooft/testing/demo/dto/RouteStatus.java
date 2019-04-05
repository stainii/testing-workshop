package be.stijnhooft.testing.demo.dto;

import java.time.LocalTime;
import java.util.Objects;

public class RouteStatus {

    private Long id;
    private String departureStation;
    private LocalTime departureTime;
    private String arrivalStation;
    private Integer currentDelay;
    private Double averageDelay;

    public RouteStatus(Long id, String departureStation, LocalTime departureTime, String arrivalStation, Integer currentDelay, Double averageDelay) {
        this.id = id;
        this.departureStation = departureStation;
        this.departureTime = departureTime;
        this.arrivalStation = arrivalStation;
        this.currentDelay = currentDelay;
        this.averageDelay = averageDelay;
    }

    public Long getId() {
        return id;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public Integer getCurrentDelay() {
        return currentDelay;
    }

    public Double getAverageDelay() {
        return averageDelay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteStatus that = (RouteStatus) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(departureStation, that.departureStation) &&
                Objects.equals(departureTime, that.departureTime) &&
                Objects.equals(arrivalStation, that.arrivalStation) &&
                Objects.equals(currentDelay, that.currentDelay) &&
                Objects.equals(averageDelay, that.averageDelay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureStation, departureTime, arrivalStation, currentDelay, averageDelay);
    }

    @Override
    public String toString() {
        return "RouteStatus{" +
                "id=" + id +
                ", departureStation='" + departureStation + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalStation='" + arrivalStation + '\'' +
                ", currentDelay=" + currentDelay +
                ", averageDelay=" + averageDelay +
                '}';
    }
}
