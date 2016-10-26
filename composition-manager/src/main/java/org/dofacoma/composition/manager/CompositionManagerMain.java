package org.dofacoma.composition.manager;

import org.dofacoma.common.CommonApplication;
import org.dofacoma.common.CommonConfig;
import org.dofacoma.common.TestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan(excludeFilters=@Filter(type=FilterType.ANNOTATION, classes=TestConfiguration.class))
@Import(CommonConfig.class)
public class CompositionManagerMain {

	public static void main(final String[] args) {
		new CommonApplication(CompositionManagerMain.class).run(args);
	}
}
