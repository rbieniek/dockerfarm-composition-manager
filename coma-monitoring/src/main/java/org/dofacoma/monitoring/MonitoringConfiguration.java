package org.dofacoma.monitoring;

import org.dofacoma.monitoring.influxdb.InfluxdbMonitoringConfiguration;
import org.dofacoma.monitoring.prometheus.PrometheusMonitoringConfiguration;
import org.dofacoma.monitoring.support.SupportMonitoringConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({InfluxdbMonitoringConfiguration.class, SupportMonitoringConfiguration.class, PrometheusMonitoringConfiguration.class})
@Slf4j
public class MonitoringConfiguration {

    @Bean
    public MonitoringPointsEmitter noopMonitoringPointsEmitter() {
        log.info("Using no-op monitoring points emitter");

        return new NoopMonitoringPointsEmitter();
    }
}
