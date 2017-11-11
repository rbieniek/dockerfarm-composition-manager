package org.dofacoma.spring.akka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "dofacoma.akka")
@Getter
@Setter
public class AkkaConfigurationProperties {

    private String configurationLocation;
}
