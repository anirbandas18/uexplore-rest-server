package com.ufl.uexplore.factory;

import javax.enterprise.inject.Produces;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ServiceFactory {

	@Produces
	public PropertiesConfiguration createConfigurationService() {
		Configurations configurations = new Configurations();
		try {
			String configFileLocation = "src/main/resources/application.properties";
			PropertiesConfiguration properties = configurations.properties(configFileLocation);
			return properties;
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

}
