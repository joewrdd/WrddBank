package com.example.wrddbanksystem.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//----- Configuration Manager for the Application -----
public class AppConfig {

    //----- Singleton Instance -----
    private static AppConfig instance;
    
    //----- Properties -----
    private final Properties properties;
    
    //----- Default Values for Common Settings -----
    private static final String DEFAULT_API_URL = "http://localhost:8080/api";
    private static final String DEFAULT_CONNECT_TIMEOUT = "10";
    private static final String DEFAULT_READ_TIMEOUT = "30";
    private static final String DEFAULT_MAX_RETRIES = "3";
    
    //----- Private Constructor to Enforce Singleton Pattern -----
    private AppConfig() {
        properties = new Properties();
        loadDefaults();
        loadFromFile();
    }
    
    //----- Get the Singleton Instance of the Configuration Manager -----
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    //----- Set Default Property Values -----
    private void loadDefaults() {
        properties.setProperty("api.url", DEFAULT_API_URL);
        properties.setProperty("api.timeout.connect", DEFAULT_CONNECT_TIMEOUT);
        properties.setProperty("api.timeout.read", DEFAULT_READ_TIMEOUT);
        properties.setProperty("api.retries.max", DEFAULT_MAX_RETRIES);
        properties.setProperty("logging.enabled", "true");
    }
    
    //----- Load Properties from the Configuration File -----
    private void loadFromFile() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    //----- Get a Property Value as a String -----
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    //----- Get a Property Value as a String with a Default Value -----
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    //----- Get a Property Value as an Integer -----
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid integer property: " + key + " = " + value);
            }
        }
        
        return defaultValue;
    }
    
    //----- Get a Property Value as a Boolean -----
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        
        return defaultValue;
    }
    
    //----- Set a Property Value -----
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    //----- Get the API Base URL -----
    public String getApiUrl() {
        return getProperty("api.url", DEFAULT_API_URL);
    }
    
    //----- Get the Connection Timeout -----
    public int getConnectTimeout() {
        return getIntProperty("api.timeout.connect", Integer.parseInt(DEFAULT_CONNECT_TIMEOUT));
    }
    
    //----- Get the Read Timeout -----
    public int getReadTimeout() {
        return getIntProperty("api.timeout.read", Integer.parseInt(DEFAULT_READ_TIMEOUT));
    }
    
    //----- Get the Maximum Number of Retries -----
    public int getMaxRetries() {
        return getIntProperty("api.retries.max", Integer.parseInt(DEFAULT_MAX_RETRIES));
    }
    
    //----- Check if Logging is Enabled -----
    public boolean isLoggingEnabled() {
        return getBooleanProperty("logging.enabled", true);
    }
} 