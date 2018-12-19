package guidelines.models;

import java.util.Objects;

public class Route {
    private final int minutesLeft;
    private final int delay;

    public Route(int minutesLeft, int delay) {
        this.minutesLeft = minutesLeft;
        this.delay = delay;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return minutesLeft == route.minutesLeft &&
                delay == route.delay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutesLeft, delay);
    }
}
