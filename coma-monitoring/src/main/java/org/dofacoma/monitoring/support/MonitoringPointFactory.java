package org.dofacoma.monitoring.support;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MonitoringPointFactory {
    private final MonitoringPointsEmitter emitter;

    public RestPoint restPoint(final String pathName) {
        final RestPoint point = new RestPoint(emitter, pathName);

        point.mark();

        return point;
    }

    public SubsystemPoint subsystemPoint(final String subsystem, final String action) {
        return new SubsystemPoint(emitter, subsystem, action);
    }
}
