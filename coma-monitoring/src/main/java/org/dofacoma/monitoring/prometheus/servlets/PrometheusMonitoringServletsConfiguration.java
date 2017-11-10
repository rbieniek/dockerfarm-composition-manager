package org.dofacoma.monitoring.prometheus.servlets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DropwizardServletConfiguration.class, PrometheusServletConfiguration.class})
@Slf4j
public class PrometheusMonitoringServletsConfiguration {
}
