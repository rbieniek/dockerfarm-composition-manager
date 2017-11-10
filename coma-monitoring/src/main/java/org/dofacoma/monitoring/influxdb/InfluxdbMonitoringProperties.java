package org.dofacoma.monitoring.influxdb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = InfluxdbMonitoringProperties.PREFIX, name = {"uri", "database"})
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = InfluxdbMonitoringProperties.PREFIX)
public class InfluxdbMonitoringProperties {
    public static final String PREFIX="hermes.monitoring.influxdb";

    private String uri;
    private String user;
    private String password;
    private String database;
    private String retentionPolicy;

    private BatchingProperties bacthing;

    @Getter
    @Setter
    @ToString
    public static class BatchingProperties {
        private int everyPoints;
        private int everyMilliseconds;
    }
}
