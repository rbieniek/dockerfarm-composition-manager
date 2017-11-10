package org.dofacoma.engine.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "coma.server")
@Validated
@Getter
@Setter
public class ComaServerConfigurationProperties {

    private Http2ConfigurationProperties http2;

    @Valid
    private SslConfiurationProperties ssl;

    @Getter
    @Setter
    public static class Http2ConfigurationProperties {
        private boolean enabled = false;
    }

    @Getter
    @Setter
    public static class SslConfiurationProperties {
        @NotNull
        @Min(value=1025)
        private Integer port;

        @NotNull
        @NotEmpty
        private String keyStore;

        @NotNull
        @NotEmpty
        private String keyStorePassword;

        @NotNull
        @NotEmpty
        private String keyPassword;

        @NotNull
        @NotEmpty
        private String keyStoreType;

        @NotNull
        @NotEmpty
        private String protocol;
    }
}
