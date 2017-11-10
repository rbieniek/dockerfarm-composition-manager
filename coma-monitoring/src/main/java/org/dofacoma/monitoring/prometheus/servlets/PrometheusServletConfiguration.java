package org.dofacoma.monitoring.prometheus.servlets;

import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = PrometheusMonitoringProperties.PREFIX + ".prometheus", name = "servlet-path")
public class PrometheusServletConfiguration {
    private static final Logger log = LoggerFactory.getLogger(PrometheusServletConfiguration.class);

    @Bean
    @Autowired
    public ServletRegistrationBean prometheusServletRegistration(final CollectorRegistry collectorRegistry,
                                                                 final PrometheusMonitoringProperties properties) {
        log.info("Enabling servlet-based prometheus metrics export");

        final ServletRegistrationBean registration = new ServletRegistrationBean(new MetricsServlet(collectorRegistry),
                properties.getPrometheus().getServletPath());

        registration.setName("PrometheusMetricsServlet");

        return registration;
    }
}
