package org.dofacoma.monitoring.prometheus.components;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class PrometheusMonitoringPointsEmitter implements MonitoringPointsEmitter, InitializingBean {
    private final PrometheusMonitoringProperties prometheusMonitoringProperties;
    private final CollectorRegistry collectorRegistry;

    private Histogram restHistogram;
    private Histogram subsystemHistogram;
    private Counter restCounter;
    private Counter subsystemCounter;

    @Override
    public void emitRestEndpointExecution(String path, HttpStatus status, long executionTime) {
        restHistogram.labels(path, status.series().name()).observe((double)executionTime);
        restCounter.labels(path, status.series().name()).inc();
    }

    @Override
    public void emitSubsystemExecution(String subsystem, String action, boolean error, long executionTime) {
        subsystemHistogram.labels(subsystem, action, error ? "failure" : "success").observe((double)executionTime);
        subsystemCounter.labels(subsystem, action, error ? "failure" : "success").inc();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        restHistogram = Histogram.build()
                .namespace("timing")
                .subsystem("rest")
                .name(prometheusMonitoringProperties.getMetricName())
                .labelNames("path", "statusSeries")
                .help("REST interface timing histogram")
                .register(collectorRegistry);
        restCounter = Counter.build()
                .namespace("counter")
                .subsystem("rest")
                .name(prometheusMonitoringProperties.getMetricName())
                .labelNames("path", "statusSeries")
                .help("REST interface usage counter")
                .register(collectorRegistry);
        subsystemHistogram = Histogram.build()
                .namespace("timing")
                .subsystem("components")
                .name(prometheusMonitoringProperties.getMetricName())
                .labelNames("subsystem", "action", "error")
                .help("Subsystem timing histogram")
                .register(collectorRegistry);
        subsystemCounter = Counter.build()
                .namespace("counter")
                .subsystem("components")
                .name(prometheusMonitoringProperties.getMetricName())
                .labelNames("subsystem", "action", "error")
                .help("Subsystem usage counter")
                .register(collectorRegistry);
    }
}
