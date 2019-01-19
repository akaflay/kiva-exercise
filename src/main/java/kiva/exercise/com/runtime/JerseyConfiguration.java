package kiva.exercise.com.runtime;

import java.util.HashSet;
import java.util.Set;

import kiva.exercise.com.exception.GenericExceptionMapper;
import kiva.exercise.com.resource.Loan;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;


@Component
public class JerseyConfiguration extends ResourceConfig {

	public JerseyConfiguration() {
		Set<Class<?>> classes = new HashSet<>();
        classes.add(Loan.class);
        classes.add(GenericExceptionMapper.class);
        registerClasses(classes);
	}

}
