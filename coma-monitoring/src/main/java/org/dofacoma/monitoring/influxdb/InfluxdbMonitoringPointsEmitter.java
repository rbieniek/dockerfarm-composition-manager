package org.dofacoma.monitoring.influxdb;

import org.dofacoma.monitoring.MonitoringPointsEmitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class InfluxdbMonitoringPointsEmitter implements MonitoringPointsEmitter, InitializingBean, DisposableBean {
    private final InfluxdbMonitoringProperties monitoringProperties;

    private InfluxDB influxDB;

    @Override
    public void emitRestEndpointExecution(String path, HttpStatus status, long executionTime) {
        influxDB.write(Point.measurement("rest")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("path", path)
                .addField("status", status.value())
                .addField("duration", executionTime)
                .build());
    }

    @Override
    public void emitSubsystemExecution(String subsystem, String action, boolean error, long executionTime) {
        influxDB.write(Point.measurement("subsystem")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("name", subsystem)
                .addField("action", action)
                .addField("sucess", !error)
                .addField("duration", executionTime)
                .build());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing InfluxDB with properties {}", monitoringProperties);

        if(StringUtils.isNoneBlank(monitoringProperties.getUser())) {
            influxDB = InfluxDBFactory.connect(monitoringProperties.getUri(), monitoringProperties.getUser(), monitoringProperties.getPassword());
        } else {
            influxDB = InfluxDBFactory.connect(monitoringProperties.getUri());
        }

        if(!influxDB.databaseExists(monitoringProperties.getDatabase())) {
            log.info("Creating InfluxDB database {}", monitoringProperties.getDatabase());
            influxDB.createDatabase(monitoringProperties.getDatabase());
        }
        influxDB.setDatabase(monitoringProperties.getDatabase());

        if(StringUtils.isNotBlank(monitoringProperties.getRetentionPolicy())) {
            log.info("Setting InfluxDB retention policy to {}", monitoringProperties.getRetentionPolicy());
            influxDB.setRetentionPolicy(monitoringProperties.getRetentionPolicy());
        }

        if(monitoringProperties.getBacthing() != null) {
            log.info("Setting InfluxDB batching parameters to {}", monitoringProperties.getBacthing());

            influxDB.enableBatch(monitoringProperties.getBacthing().getEveryPoints(), monitoringProperties.getBacthing().getEveryMilliseconds(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down InfluxDB");

        influxDB.close();
    }
}
