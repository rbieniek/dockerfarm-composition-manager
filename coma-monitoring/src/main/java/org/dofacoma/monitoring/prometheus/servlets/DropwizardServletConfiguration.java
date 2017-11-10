package org.dofacoma.monitoring.prometheus.servlets;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = PrometheusMonitoringProperties.PREFIX + ".dropwizard.servlet", name = "metrics-path")
public class DropwizardServletConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DropwizardServletConfiguration.class);

    @Bean
    @Autowired
    public ServletRegistrationBean dropwizardMetricsServlet(final PrometheusMonitoringProperties properties) {
        log.info("Enabling servlet-based dropwizard metrics export");

        final ServletRegistrationBean registration = new ServletRegistrationBean(new MetricsServlet(),
                properties.getDropwizard().getServlet().getMetricsPath());

        registration.setName("DropwizardMetricsServlet");

        return registration;

    }

    @Bean
    @Autowired
    public ServletRegistrationBean dropwizardHealthCheckServlet(final PrometheusMonitoringProperties properties) {
        log.info("Enabling Dropwizard healthcheck export");

        return new ServletRegistrationBean(new HealthCheckServlet(),
                properties.getDropwizard().getServlet().getHealthcheckPath());
    }

    @Bean
    @Autowired
    public ServletListenerRegistrationBean<HealthCheckServlet.ContextListener>
    healthcheckContextListener(final HealthCheckRegistry registry) {
        return new ServletListenerRegistrationBean<>(new HealthCheckServlet.ContextListener() {

            @Override
            protected HealthCheckRegistry getHealthCheckRegistry() {
                return registry;
            }
        });
    }

    @Bean
    @Autowired
    public ServletListenerRegistrationBean<MetricsServlet.ContextListener>
    metricsContextListener(final MetricRegistry registry) {
        return new ServletListenerRegistrationBean<>(new MetricsServlet.ContextListener() {

            @Override
            protected MetricRegistry getMetricRegistry() {
                return registry;
            }
        });
    }
}