package be.stijnhooft.testing.demo.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Delay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer delay;

    @Column(name = "creation_date_time",
            nullable = false)
    private LocalDateTime creation;

    @Column(name = "departure_date_time",
            nullable = false)
    private LocalDateTime departure;

    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;

    public Delay() {
    }

    public Delay(Integer delay, LocalDateTime departure, Route route, LocalDateTime creation) {
        this.delay = delay;
        this.departure = departure;
        this.route = route;
        this.creation = creation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }


    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delay delay1 = (Delay) o;
        return Objects.equals(id, delay1.id) &&
                Objects.equals(delay, delay1.delay) &&
                Objects.equals(departure, delay1.departure) &&
                Objects.equals(route, delay1.route) &&
                Objects.equals(creation, delay1.creation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, delay, departure, route, creation);
    }

    @Override
    public String toString() {
        return "Delay{" +
                "id=" + id +
                ", delay=" + delay +
                ", creation=" + creation +
                ", departure=" + departure +
                ", route=" + route +
                '}';
    }
}
