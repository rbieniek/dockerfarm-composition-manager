package org.dofacoma.monitoring.prometheus.components;

import org.dofacoma.monitoring.prometheus.PrometheusMonitoringProperties;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PushGatewayDriver implements ApplicationContextAware, InitializingBean, DisposableBean {

    private final Logger log = LoggerFactory.getLogger(PushGatewayDriver.class);

    private final PushGateway pushGateway;
    private final PrometheusMonitoringProperties.PrometheusProperties properties;
    private final CollectorRegistry registry;

    private ApplicationContext applicationContext;

    private String appName;
    private SimpleAsyncTaskExecutor executor;
    private DriverRunnable driverRunnable;

    public PushGatewayDriver(final CollectorRegistry registry, final PushGateway pushGateway,
                             final PrometheusMonitoringProperties.PrometheusProperties properties) {
        this.pushGateway = pushGateway;
        this.properties = properties;
        this.registry = registry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        appName = applicationContext.getApplicationName();

        if (StringUtils.isBlank(appName)) {
            appName = applicationContext.getEnvironment().getProperty("spring.application.name");

            if (StringUtils.isBlank(appName)) {
                log.warn("Application name not set, using fallback name 'springboot'. Please assign application name");

                appName = "springboot";
            }
        }

        this.executor = new SimpleAsyncTaskExecutor();
        this.driverRunnable = new DriverRunnable();

        final Thread thread = new Thread(driverRunnable);

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void destroy() throws Exception {
        driverRunnable.stop();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class DriverRunnable implements Runnable {
        private AtomicBoolean runFlag = new AtomicBoolean(true);

        private void stop() {
            runFlag.set(false);
        }

        @Override
        public void run() {
            while (runFlag.get()) {
                try {
                    Thread.sleep(1000L * properties.getPushInterval());
                } catch (final InterruptedException e) {
                    log.info("push driveing thread interrupted in sleep", e);
                }

                log.info("Initiating metric data push to gateway {}", properties.getPushGatewayUri());

                executor.execute(() -> {
                    log.info("Pushing metric data to gateway {}", properties.getPushGatewayUri());

                    try {
                        pushGateway.push(registry, appName);
                    } catch (final IOException e) {
                        log.warn("Failed to push metric data to gateway {}", properties.getPushGatewayUri(), e);
                    }
                });
            }
        }

    }
}
