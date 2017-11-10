package org.dofacoma.monitoring.support;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class RestPoint {
    private final MonitoringPointsEmitter emitter;
    private final String path;

    private long markTiumestap;

    public void mark() {
        markTiumestap = System.currentTimeMillis();
    }

    public <T> ResponseEntity<T> emit(ResponseEntity<T> response) {
        final long emitTimestamp = System.currentTimeMillis();

        emitter.emitRestEndpointExecution(path, response.getStatusCode(), emitTimestamp - markTiumestap);

        return response;
    }

    public void error() {
        final long emitTimestamp = System.currentTimeMillis();

        emitter.emitRestEndpointExecution(path, HttpStatus.INTERNAL_SERVER_ERROR, emitTimestamp - markTiumestap);
    }
}
