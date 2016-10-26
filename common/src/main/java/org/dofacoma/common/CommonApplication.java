package org.dofacoma.common;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public class CommonApplication extends SpringApplication {

	    public CommonApplication(final Class<?> source) {
	        super(source);
	    }

	    @Override
	    @SuppressWarnings("PMD.UseVarargs")
	    protected void configureProfiles(final ConfigurableEnvironment env, final String[] args) {
	        super.configureProfiles(env, args);
	    }


}
