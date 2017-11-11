package org.dofacoma.akka.utilities;

import lombok.AccessLevel;
import lombok.Setter;

import static akka.actor.ActorRef.noSender;

import akka.actor.ActorRef;

@Setter(value = AccessLevel.PROTECTED)
public class ActorRefSupport {

    private ActorRef actorRef;

    public void tell(final Object message) {
        actorRef.tell(message, noSender());
    }
}
