package org.dofacoma.akka.wakeup;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WakeupTimerFired {

    private UUID uuid;
}
