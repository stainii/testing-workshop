package be.stijnhooft.testing.demo.dto;

public class IRailConnection {

    private IRailDeparture departure;

    public IRailDeparture getDeparture() {
        return departure;
    }

    public void setDeparture(IRailDeparture departure) {
        this.departure = departure;
    }
}
