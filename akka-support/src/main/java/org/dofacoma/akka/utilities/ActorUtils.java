package org.dofacoma.akka.utilities;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import akka.actor.Actor;
import akka.japi.Procedure;

public class ActorUtils {

    public static <T extends Actor> MessageHandler withMessage(final Object message, final T actorInstance) {
        final Logger log = LoggerFactory.getLogger(actorInstance.getClass());

        log.info("Handling message of type {}", message.getClass().getName());

        return new MessageHandler(message, log);
    }

    public static <T extends Procedure<Object>> MessageHandler withMessage(final Object message, final T actorInstance) {
        final Logger log = LoggerFactory.getLogger(actorInstance.getClass());

        log.info("Handling message of type {}", message.getClass().getName());

        return new MessageHandler(message, log);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MessageHandler {

        private final Object message;
        private final Logger log;

        @SuppressWarnings("unchecked")
        public <T> MessageHandler onType(final Class<T> messageType, final Consumer<T> messageHandler) {
            if (log.isDebugEnabled()) {
                log.debug(
                        "Checking match for type {} on message {}",
                        messageType.getName(),
                        message != null ? message.getClass().getName() : "null");
            }

            if (message != null && messageType.isInstance(message)) {
                log.info("Match for type {} on message {}", messageType.getName(), message.getClass().getName());

                messageHandler.accept((T) message);

                return new MessageHandler(null, log);
            }

            return new MessageHandler(message, log);
        }

        public <T> MessageHandler ignoreType(final Class<T> messageType) {
            if (log.isDebugEnabled()) {
                log.debug(
                        "Checking match for type {} on message {} for ignore",
                        messageType.getName(),
                        message != null ? message.getClass().getName() : "null");
            }

            if (message != null && messageType.isInstance(message)) {
                log.info("Match for type {} on message {} to ignore", messageType.getName(), message.getClass().getName());

                return new MessageHandler(null, log);
            }

            return new MessageHandler(message, log);
        }

        public void unhandled(final Consumer<Object> defaultHandler) {
            if (message != null) {
                log.info("Unhandled message {}", message.getClass().getName());

                defaultHandler.accept(message);
            }
        }
    }
}
