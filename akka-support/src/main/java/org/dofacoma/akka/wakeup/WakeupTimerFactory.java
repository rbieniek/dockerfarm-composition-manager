package org.dofacoma.akka.wakeup;

import java.util.UUID;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import scala.concurrent.duration.FiniteDuration;

@RequiredArgsConstructor
public class WakeupTimerFactory {

    private final UntypedActorContext context;
    private final ActorRef targetActor;

    public WakeupTimer oneShotWakeup(final FiniteDuration duration) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimer.builder()
                .timer(
                        context.system().scheduler().scheduleOnce(
                                duration,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .build();
    }

    public WakeupTimer intervalWakeup(final FiniteDuration interval) {
        return intervalWakeup(interval, interval);
    }

    public WakeupTimer intervalWakeup(final FiniteDuration delay, final FiniteDuration interval) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimer.builder()
                .timer(
                        context.system().scheduler().schedule(
                                delay,
                                interval,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .build();
    }

    public <T> WakeupTimerWithPayload<T> oneShotWakeup(final FiniteDuration duration, final T payload) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimerWithPayload.<T> builder()
                .timer(
                        context.system().scheduler().scheduleOnce(
                                duration,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .payload(payload)
                .build();
    }

    public <T> WakeupTimerWithPayload<T> intervalWakeup(final FiniteDuration interval, final T payload) {
        return intervalWakeup(interval, interval, payload);
    }

    public <T> WakeupTimerWithPayload<T> intervalWakeup(
            final FiniteDuration delay,
            final FiniteDuration interval,
            final T payload) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimerWithPayload.<T> builder()
                .timer(
                        context.system().scheduler().schedule(
                                delay,
                                interval,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .payload(payload)
                .build();
    }
    // ---

    public <T> WakeupTimerWithClosure<T> oneShotWakeup(
            final FiniteDuration duration,
            final T argument,
            final Consumer<T> consumer) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimerWithClosure.<T> builder()
                .timer(
                        context.system().scheduler().scheduleOnce(
                                duration,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .argument(argument)
                .consumer(consumer)
                .build();
    }

    public <T> WakeupTimerWithClosure<T> intervalWakeup(
            final FiniteDuration interval,
            final T argument,
            final Consumer<T> consumer) {
        return intervalWakeup(interval, interval, argument, consumer);
    }

    public <T> WakeupTimerWithClosure<T> intervalWakeup(
            final FiniteDuration delay,
            final FiniteDuration interval,
            final T argument,
            final Consumer<T> consumer) {
        final UUID uuid = UUID.randomUUID();

        return WakeupTimerWithClosure.<T> builder()
                .timer(
                        context.system().scheduler().schedule(
                                delay,
                                interval,
                                targetActor,
                                WakeupTimerFired.builder().uuid(uuid).build(),
                                context.dispatcher(),
                                ActorRef.noSender()))
                .uuid(uuid)
                .argument(argument)
                .consumer(consumer)
                .build();
    }

}
