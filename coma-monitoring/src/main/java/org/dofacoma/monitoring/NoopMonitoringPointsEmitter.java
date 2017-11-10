package org.dofacoma.monitoring;

import org.springframework.http.HttpStatus;

public class NoopMonitoringPointsEmitter implements MonitoringPointsEmitter {
    @Override
    public void emitRestEndpointExecution(String path, HttpStatus status, long executionTime) {

    }

    @Override
    public void emitSubsystemExecution(String subsystem, String action, boolean error, long executionTime) {

    }
}
