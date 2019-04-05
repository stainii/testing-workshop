package be.stijnhooft.testing.demo.dto;

import java.util.List;

public class IRailConnections {

    private List<IRailConnection> connection;

    public List<IRailConnection> getConnection() {
        return connection;
    }

    public void setConnection(List<IRailConnection> connection) {
        this.connection = connection;
    }
}
