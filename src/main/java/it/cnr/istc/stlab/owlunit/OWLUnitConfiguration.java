package it.cnr.istc.stlab.owlunit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OWLUnitConfiguration {

	private static Logger logger = LoggerFactory.getLogger(OWLUnitConfiguration.class);
	private static OWLUnitConfiguration instance;
	private int serverPort;

	private static String CONFIGURATION_FILE = "config.properties";

	private OWLUnitConfiguration() {
		try {
			Configurations configs = new Configurations();
			Configuration config = configs.properties("config.properties");
			this.serverPort = config.getInt("serverPort");

			logger.info("Configuration file " + CONFIGURATION_FILE);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void setConfigFile(String file) {
		CONFIGURATION_FILE = file;
	}

	public static OWLUnitConfiguration getPSSConfiguration() {
		if (instance == null) {
			instance = new OWLUnitConfiguration();
		}
		return instance;
	}

	public int getServerPort() {
		return serverPort;
	}

}
