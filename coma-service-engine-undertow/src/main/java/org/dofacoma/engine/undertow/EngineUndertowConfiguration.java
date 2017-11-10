package org.dofacoma.engine.undertow;

import org.dofacoma.engine.common.EngineCommonConfiguration;
import org.dofacoma.engine.common.EngineConditions;
import org.dofacoma.engine.common.ComaServerConfigurationProperties;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
@Import(EngineCommonConfiguration.class)
@Slf4j
public class EngineUndertowConfiguration {
    @Configuration
    @Conditional(EngineConditions.HttpOnlyCondition.class)
    class HttpOnlyConfiguration {
        @Bean
        public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() {
            return new UndertowEmbeddedServletContainerFactory();
        }
    }

    @Configuration
    @Conditional(EngineConditions.Http2OnlyCondition.class)
    class Http2OnlyConfiguration {
        @Bean
        public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() {
            final UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

            factory.addBuilderCustomizers(undertowHttp2UpgradeCustomizer());

            return factory;
        }
    }

    @Configuration
    @Conditional(EngineConditions.HttpsOnlyCondition.class)
    class HttpsOnlyConfiguration {
        @Bean
        public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory(final ApplicationContext ctx,
                                                                                               final ComaServerConfigurationProperties sslProperties,
                                                                                               final ServerProperties serverProperties) {
            final UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

            factory.addBuilderCustomizers(undertowHttpsServerCustomizer(ctx, sslProperties, serverProperties));

            return factory;
        }
    }

    @Configuration
    @Conditional(EngineConditions.Http2AndHttpsCondition.class)
    class Http2AndHttpsConfiguration {
        @Bean
        public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory(final ApplicationContext ctx,
                                                                                               final ComaServerConfigurationProperties sslProperties,
                                                                                               final ServerProperties serverProperties) {
            final UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

            factory.addBuilderCustomizers(undertowHttp2UpgradeCustomizer(), undertowHttpsServerCustomizer(ctx, sslProperties, serverProperties));

            return factory;
        }
    }

    private UndertowBuilderCustomizer undertowHttp2UpgradeCustomizer() {
        log.info("Enabling HTTP/2 server support");

        return new UndertowBuilderCustomizer() {
            @Override
            public void customize(Undertow.Builder builder) {
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            }
        };
    }


    private UndertowBuilderCustomizer undertowHttpsServerCustomizer(final ApplicationContext ctx,
                                      final ComaServerConfigurationProperties sslProperties,
                                      final ServerProperties serverProperties) {
        log.info("Enabling additional HTTPS server");

        return new UndertowBuilderCustomizer() {
            @Override
            public void customize(Undertow.Builder builder) {
                builder.addHttpsListener(sslProperties.getSsl().getPort(),
                        serverProperties.getAddress() != null ?  serverProperties.getAddress().getHostAddress() : "0.0.0.0",
                        createSSLContext(ctx, sslProperties));
            }
        };

    }

    @SneakyThrows
    private SSLContext createSSLContext(final ApplicationContext ctx, final ComaServerConfigurationProperties properties) {
        final KeyStore keyStore = loadKeystore(ctx, properties);
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(keyStore, properties.getSsl().getKeyPassword().toCharArray());
        trustManagerFactory.init(keyStore);

        final SSLContext sslContext = SSLContext.getInstance(properties.getSsl().getProtocol());

        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext;
    }

    @SneakyThrows
    private KeyStore loadKeystore(final ApplicationContext ctx, final ComaServerConfigurationProperties properties) {
        try(InputStream stream = ctx.getResource(properties.getSsl().getKeyStore()).getInputStream()) {
            final KeyStore keyStore = KeyStore.getInstance(properties.getSsl().getKeyStoreType());

            keyStore.load(stream, properties.getSsl().getKeyStorePassword().toCharArray());

            return keyStore;
        }
    }

}
