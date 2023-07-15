package businesslogic.shift;

import java.time.Instant;

public abstract class Shift {
    private String start;
    private String end;

    public Shift(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
