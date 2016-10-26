package org.dofacoma.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(excludeFilters=@Filter(type=FilterType.ANNOTATION, classes=TestConfiguration.class))
public class CommonConfig {

}
