package org.dofacoma.akka.wakeup;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

import akka.actor.Cancellable;

@Getter
@Builder
public class WakeupTimerWithPayload<T> {
    private final Cancellable timer;
    private final UUID uuid;
    private final T payload;

    public boolean matches(final WakeupTimerFired wtf) {
        return uuid.equals(wtf.getUuid());
    }

    public boolean cancel() {
        return timer.cancel();
    }

}
