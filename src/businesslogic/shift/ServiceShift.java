package businesslogic.shift;

import businesslogic.user.User;

import java.time.Instant;

public class ServiceShift extends Shift{
    private User cook;

    public ServiceShift(String start, String end) {
        super(start, end);
    }
}
