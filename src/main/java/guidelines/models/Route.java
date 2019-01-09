package guidelines.models;

import java.util.Objects;

public class Route {
    private final int minutesLeft;
    private final int delay;
    private final String firstStation;
    private final String transport;

    public String getFirstStation() {
        return firstStation;
    }

    public Route(int minutesLeft, int delay, String firstStation, String transport) {
        this.minutesLeft = minutesLeft;
        this.delay = delay;
        this.firstStation = firstStation;
        this.transport = transport;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public int getDelay() {
        return delay;
    }

    public String getTransport() {
        return transport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return minutesLeft == route.minutesLeft &&
                delay == route.delay &&
                Objects.equals(firstStation, route.firstStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutesLeft, delay);
    }
}
