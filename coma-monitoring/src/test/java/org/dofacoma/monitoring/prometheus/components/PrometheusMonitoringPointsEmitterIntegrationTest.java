package org.dofacoma.monitoring.prometheus.components;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.dofacoma.monitoring.MonitoringPointsEmitter;
import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import org.dofacoma.common.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static io.prometheus.client.Collector.Type.COUNTER;
import static io.prometheus.client.Collector.Type.HISTOGRAM;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = PrometheusMonitoringPointsEmitterIntegrationTest.PrometheusMonitoringPointsEmitterIntegrationTestConfiguration.class)
public class PrometheusMonitoringPointsEmitterIntegrationTest {

    @Autowired
    private MonitoringPointsEmitter prometheusMonitoringPointsEmitter;

    @Autowired
    private CollectorRegistry collectorRegistry;

    @Test
    public void shouldEmitRestMetric() {
        prometheusMonitoringPointsEmitter.emitRestEndpointExecution("/foo", HttpStatus.OK, 10L);

        final MetricFamilySamples histogramSample = collectorRegistry.filteredMetricFamilySamples(singleton("timing_rest_test_metric_sum")).nextElement();

        assertThat(histogramSample.type).isEqualTo(HISTOGRAM);
        assertThat(histogramSample.samples).containsOnly(new Sample("timing_rest_test_metric_sum",
                Arrays.asList("path", "statusSeries"),
                Arrays.asList("/foo", "SUCCESSFUL"),
                10.0d));

        final MetricFamilySamples counterSample = collectorRegistry.filteredMetricFamilySamples(singleton("counter_rest_test_metric")).nextElement();

        assertThat(counterSample.type).isEqualTo(COUNTER);
        assertThat(counterSample.samples).containsOnly(new Sample("counter_rest_test_metric",
                Arrays.asList("path", "statusSeries"),
                Arrays.asList("/foo", "SUCCESSFUL"),
                1.0d));
    }


    @Test
    public void shouldEmitSubsystemMetric() {
        prometheusMonitoringPointsEmitter.emitSubsystemExecution("persistence", "save", false, 10L);

        final MetricFamilySamples histogramSample = collectorRegistry.filteredMetricFamilySamples(singleton("timing_components_test_metric_sum")).nextElement();

        assertThat(histogramSample.type).isEqualTo(HISTOGRAM);
        assertThat(histogramSample.samples).containsOnly(new Sample("timing_components_test_metric_sum",
                Arrays.asList("subsystem", "action", "error"),
                Arrays.asList("persistence", "save", "success"),
                10.0d));

        final MetricFamilySamples counterSample = collectorRegistry.filteredMetricFamilySamples(singleton("counter_components_test_metric")).nextElement();

        assertThat(counterSample.type).isEqualTo(COUNTER);
        assertThat(counterSample.samples).containsOnly(new Sample("counter_components_test_metric",
                Arrays.asList("subsystem", "action", "error"),
                Arrays.asList("persistence", "save", "success"),
                1.0d));
    }

    @TestConfiguration
    @EnableAutoConfiguration
    public static class PrometheusMonitoringPointsEmitterIntegrationTestConfiguration {
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
        public PrometheusMonitoringProperties prometheusMonitoringProperties() {
            final PrometheusMonitoringProperties properties = new PrometheusMonitoringProperties();

            properties.setMetricName("test_metric");

            return properties;
        }

        @Bean
        @Autowired
        public MonitoringPointsEmitter prometheusMonitoringPointsEmitter(final PrometheusMonitoringProperties prometheusMonitoringProperties,
                                                                         final CollectorRegistry collectorRegistry) {
            return new PrometheusMonitoringPointsEmitter(prometheusMonitoringProperties, collectorRegistry);
        }

    }
}

