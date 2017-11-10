package org.dofacoma.engine.common;

import org.dofacoma.common.TestConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@EnableConfigurationProperties
@ComponentScan(excludeFilters = @Filter(type = ANNOTATION, classes = TestConfiguration.class))
public class EngineCommonConfiguration {
}
