package org.dofacoma.engine.common;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.ConfigurationCondition;

public class EngineConditions {
    public static class HttpOnlyCondition extends NoneNestedConditions {
        public HttpOnlyCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }
        @ConditionalOnProperty(prefix = "hermes.server.http2",name= "enabled")
        class EnableHttp2Condition {

        }

        @ConditionalOnProperty(prefix = "hermes.server.ssl", name="port")
        class EnableHttpsCondition {

        }
    }

    public static class Http2OnlyCondition extends AllNestedConditions {
        public Http2OnlyCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "hermes.server.http2",name="enabled")
        class EnableHttp2Condition {

        }

        @Conditional(HttpsDisabledCondition.class)
        class DisableHttpsCondition {

        }
    }

    public static class HttpsOnlyCondition extends AllNestedConditions {
        public HttpsOnlyCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @Conditional(Http2DisabledCondition.class)
        class DisableHttp2Condition {

        }

        @ConditionalOnProperty(prefix = "hermes.server.ssl", name="port")
        class EnableHttpsCondition {

        }
    }

    public static class Http2AndHttpsCondition extends AllNestedConditions {
        public Http2AndHttpsCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "hermes.server.http2",name="enabled")
        class EnableHttp2Condition {

        }

        @ConditionalOnProperty(prefix = "hermes.server.ssl", name="port")
        class EnableHttpsCondition {

        }
    }


    public static class Http2DisabledCondition extends NoneNestedConditions {
        public Http2DisabledCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "hermes.server.http2",name="enabled")
        class EnableHttp2Condition {

        }
    }

    public static class HttpsDisabledCondition extends NoneNestedConditions {
        public HttpsDisabledCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "hermes.server.ssl", name="port")
        class EnableHttpsCondition {

        }
    }
}
