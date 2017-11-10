package org.dofacoma.monitoring;

import org.springframework.http.HttpStatus;

public interface MonitoringPointsEmitter {
    void emitRestEndpointExecution(String path, HttpStatus status, long executionTime);

    void emitSubsystemExecution(String subsystem, String action, boolean error, long executionTime);
}
