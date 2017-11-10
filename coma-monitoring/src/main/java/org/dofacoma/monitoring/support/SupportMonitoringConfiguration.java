package org.dofacoma.monitoring.support;

import org.dofacoma.common.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type=ANNOTATION, classes = TestConfiguration.class))
public class SupportMonitoringConfiguration {

}
