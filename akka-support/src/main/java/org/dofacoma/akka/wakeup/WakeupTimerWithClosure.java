package org.dofacoma.akka.wakeup;

import java.util.UUID;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Getter;

import akka.actor.Cancellable;

@Getter
@Builder
public class WakeupTimerWithClosure<T> {
    private final Cancellable timer;
    private final UUID uuid;
    private final T argument;
    private final Consumer<T> consumer;

    public boolean matches(final WakeupTimerFired wtf) {
        return uuid.equals(wtf.getUuid());
    }

    public boolean cancel() {
        return timer.cancel();
    }

    public void consume() {
        consumer.accept(argument);
    }
    
    public void consumeOnMatch(final WakeupTimerFired wtf) {
        if(uuid.equals(wtf.getUuid())) {
            consume();
        }
    }
}
