package org.dofacoma.monitoring.influxdb;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import lombok.extern.slf4j.Slf4j;
import org.dofacoma.common.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(excludeFilters = @Filter(type = ANNOTATION, classes = TestConfiguration.class))
@Slf4j
@EnableConfigurationProperties
public class InfluxdbMonitoringConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = InfluxdbMonitoringProperties.PREFIX, name = {"uri", "database"})
    public static class PropertiesSetConfiguration {
        @Bean
        @Autowired
        @Primary
        public MonitoringPointsEmitter influxdbMonitoringPointsEmitter(final InfluxdbMonitoringProperties monitoringProperties) {
            log.info("Using InfluxDB-based monitoring points emitter");

            return new InfluxdbMonitoringPointsEmitter(monitoringProperties);
        }

    }
}
