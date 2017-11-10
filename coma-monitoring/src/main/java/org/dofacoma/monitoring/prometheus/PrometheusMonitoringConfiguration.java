package org.dofacoma.monitoring.prometheus;

import org.dofacoma.monitoring.prometheus.components.PrometheusMonitoringComponentsConfiguration;
import org.dofacoma.monitoring.prometheus.servlets.PrometheusMonitoringServletsConfiguration;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties
public class PrometheusMonitoringConfiguration {

    @Configuration
    @Conditional(PrometheusEnabled.class)
    @Import({PrometheusMonitoringComponentsConfiguration.class, PrometheusMonitoringServletsConfiguration.class})
    public static class ConditionalPrometheusMonitoringConfiguration {
        @Bean
        public PrometheusMonitoringProperties prometheusMonitoringProperties() {
            return new PrometheusMonitoringProperties();
        }
    }

    static class PrometheusEnabled extends AnyNestedCondition {

        PrometheusEnabled() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = PrometheusMonitoringProperties.PREFIX + ".prometheus", name = "servlet-path")
        static class PrometheusServletEndpointEnabled {

        }

        @ConditionalOnProperty(prefix = PrometheusMonitoringProperties.PREFIX + ".prometheus", name = "push-gateway-uri")
        static class PrometheusPushGatewayEnabled {

        }

    }
}
