package com.ufl.uexplore.util;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationHelper {

	private static PropertiesConfiguration propertiesConfiguration;

	static {
		try {
			Configurations configuration = new Configurations();
			propertiesConfiguration = configuration.properties("application.properties");
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static PropertiesConfiguration getPropertiesConfiguration() {
		return propertiesConfiguration;
	}
	
	

}
