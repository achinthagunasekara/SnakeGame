/*
 * 2013
 * Author : Archie Gunasekara
 */

package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
	
	private String file = "bin\\config.properties";
	private Properties properties;
	private static ConfigFileReader instance;
	
	public static ConfigFileReader getConfigFileReaderInstance() {
		
		if(instance == null) {
			
			instance = new ConfigFileReader();
		}
		
		return instance;
	}
	
	private ConfigFileReader() {
		
		properties = new Properties();
		
		try {
			
			properties.load(new FileInputStream(file));
		}
		catch (IOException ioEx) {
		
			System.out.println("Unable to read config file : " + ioEx.toString());
		}
	}
	
	public String getPropertyFor(String s) {
		
		return properties.getProperty(s);
	}
}