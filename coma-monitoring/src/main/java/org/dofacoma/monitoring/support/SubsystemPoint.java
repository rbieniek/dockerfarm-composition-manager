package org.dofacoma.monitoring.support;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SubsystemPoint {
    private final MonitoringPointsEmitter emitter;
    private final String subsystem;
    private final String action;

    @SneakyThrows
    public <T> T measure(Supplier<T> supplier) throws Throwable{
        final long markStamp = System.currentTimeMillis();
        boolean error = false;

        try {
            return supplier.get();
        } catch(Throwable t) {
            error = true;

            throw t;
        } finally {
            final long emitStamp = System.currentTimeMillis();

            emitter.emitSubsystemExecution(subsystem, action, error, emitStamp-markStamp);
        }
    }
}
