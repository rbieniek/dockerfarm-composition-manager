package org.dofacoma.composition.manager;

import org.dofacoma.engine.undertow.EngineUndertowConfiguration;
import org.dofacoma.monitoring.MonitoringConfiguration;
import org.dofacoma.common.CommonApplication;
import org.dofacoma.common.CommonConfig;
import org.dofacoma.common.TestConfiguration;
import org.dofacoma.persistence.PersistenceConfiguration;
import org.dofacoma.spring.akka.AkkaConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters=@Filter(type=FilterType.ANNOTATION, classes=TestConfiguration.class))
@Import({CommonConfig.class, MonitoringConfiguration.class, EngineUndertowConfiguration.class, AkkaConfiguration.class, PersistenceConfiguration.class})
public class CompositionManagerMain {

	public static void main(final String[] args) {
		new CommonApplication(CompositionManagerMain.class).run(args);
	}
}
