package kr.hhplus.be.server.module.auth.domain.policy;

import org.springframework.stereotype.Component;

@Component
public class WaitListTokenPolicy {

    private final int DEFAULT_SESSION_MINUTES = 5;
    private final int DEFAULT_MAX_CAPACITY = 10;

    public int getSessionMinutes() {
        return DEFAULT_SESSION_MINUTES;
    }

    public int getMaxCapacity() {
        return DEFAULT_MAX_CAPACITY;
    }

}
