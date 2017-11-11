package org.dofacoma.spring.akka;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dofacoma.common.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStreamReader;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@ComponentScan(
        basePackageClasses = AkkaConfiguration.class,
        excludeFilters = @Filter(type = ANNOTATION, classes = TestConfiguration.class))
@EnableConfigurationProperties(AkkaConfigurationProperties.class)
@Slf4j
public class AkkaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Autowired
    public ActorSystem actorSystem(
            final AkkaConfigurationProperties properties,
            final ResourceLoader resourceLoader,
            final Environment environment) throws Exception {
        final String usedConfigFile = StringUtils.isNotBlank(properties.getConfigurationLocation())
                ? properties.getConfigurationLocation()
                : "classpath:akka.conf";

        log.info("Initializing Akka using config location {}", usedConfigFile);

        final ActorSystem system = ActorSystem.create("dofacoma", ConfigFactory.load(
                ConfigFactory.systemProperties().withFallback(
                        ConfigFactory.parseReader(
                                new InputStreamReader(resourceLoader.getResource(usedConfigFile).getInputStream(), "UTF-8")))));

        AkkaSpringExtension.SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);

        return system;
    }
}
