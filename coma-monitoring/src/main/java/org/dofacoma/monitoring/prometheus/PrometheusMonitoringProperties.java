package org.dofacoma.monitoring.prometheus;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ConfigurationProperties(prefix = PrometheusMonitoringProperties.PREFIX)
@Getter
@Setter
public class PrometheusMonitoringProperties {

    public static final String PREFIX = "hermes.monitoring";

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z_:][a-zA-Z0-9_:]*")
    private String metricName;

    private DropwizardsProperties dropwizard;
    private PrometheusProperties prometheus;


    @Getter
    @Setter
    public static class DropwizardsProperties {
        private DropwizardServletProperties servlet;

    }

    @Getter
    @Setter
    public static class DropwizardServletProperties {
        private String metricsPath;
        private String healthcheckPath;
    }

    @Getter
    @Setter
    public static class PrometheusProperties {
        private String servletPath;
        private String pushGatewayUri;
        private int pushInterval = 15;
    }
}
