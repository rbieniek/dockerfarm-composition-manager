package org.dofacoma.monitoring.prometheus.components;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.dofacoma.monitoring.MonitoringPointsEmitter;
import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.dofacoma.common.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = ANNOTATION, classes = TestConfiguration.class))
@Slf4j
public class PrometheusMonitoringComponentsConfiguration {
    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public HealthCheckRegistry healthCheckRegistry() {
        return new HealthCheckRegistry();
    }

    @Bean
    @Autowired
    public DropwizardMetricServices dropwizardMetricServices(final MetricRegistry metricRegistry) {
        return new DropwizardMetricServices(metricRegistry);
    }

    @Bean
    @Autowired
    public DropwizardExports dropwizardsExport(final MetricRegistry metricRegistry) {
        return new DropwizardExports(metricRegistry);
    }

    @Bean
    @Autowired
    public CollectorRegistry collectorRegistry(final DropwizardExports dropwizardsExport) {
        final CollectorRegistry registry = new CollectorRegistry();

        registry.register(dropwizardsExport);

        return registry;
    }

    @Bean
    @Autowired
    @Primary
    public MonitoringPointsEmitter prometheusMonitoringPointsEmitter(final PrometheusMonitoringProperties prometheusMonitoringProperties,
                                                                     final CollectorRegistry collectorRegistry) {
        return new PrometheusMonitoringPointsEmitter(prometheusMonitoringProperties, collectorRegistry);
    }

    @Configuration
    @ConditionalOnProperty(prefix = PrometheusMonitoringProperties.PREFIX + ".prometheus", name = "push-gateway-uri")
    public static class PrometheusPushComponentConfiuguration {
        @Bean
        public PushGatewayDriver prometheusPushGatewayDriver(final CollectorRegistry registry,
                                                             final PushGateway pushGateway, final PrometheusMonitoringProperties properties) {
            log.info("Enabling metric push to prometheus push gateway");

            return new PushGatewayDriver(registry, pushGateway, properties.getPrometheus());
        }

        @Bean
        public PushGateway prometheusPushGateway(final PrometheusMonitoringProperties properties) {
            final PushGateway gw = new PushGateway(properties.getPrometheus().getPushGatewayUri());

            return gw;
        }
    }

}
