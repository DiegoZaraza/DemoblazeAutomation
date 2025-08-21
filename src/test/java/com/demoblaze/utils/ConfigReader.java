package com.demoblaze.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading configuration properties from config.properties file
 * Provides convenient methods to access common test configuration values
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    // Private constructor for singleton pattern
    public ConfigReader() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReader
     * @return ConfigReader instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Load properties from config file
     */
    private void loadProperties() {
        properties = new Properties();

        // Try to load from classpath first (for resources folder)
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded successfully from classpath");
                return;
            }
        } catch (IOException e) {
            logger.warn("Could not load config from classpath: {}", e.getMessage());
        }

        // Fallback to file system path
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
            logger.info("Configuration loaded successfully from file system: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration file from both classpath and file system. " +
                    "Please ensure config.properties exists in src/test/resources/", e);
            throw new RuntimeException("Configuration file not found", e);
        }
    }

    /**
     * Get property value by key
     * @param key property key
     * @return property value or null if not found
     */
    public String getProperty(String key) {
        if (properties == null) {
            logger.error("Properties not loaded");
            return null;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }

    /**
     * Get property value with default fallback
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get boolean property value
     * @param key property key
     * @return boolean value
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Get boolean property with default value
     * @param key property key
     * @param defaultValue default boolean value
     * @return boolean property value or default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get integer property value
     * @param key property key
     * @return integer value or 0 if parsing fails
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse integer property '{}' with value '{}', returning 0", key, value);
            return 0;
        }
    }

    /**
     * Get integer property with default value
     * @param key property key
     * @param defaultValue default integer value
     * @return integer property value or default
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse integer property '{}' with value '{}', returning default {}",
                    key, value, defaultValue);
            return defaultValue;
        }
    }

    // Convenience methods for common properties

    /**
     * Get base URL for the application
     * @return base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url", "https://www.demoblaze.com/");
    }

    /**
     * Get browser type for test execution
     * @return browser name (chrome, firefox, edge, etc.)
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }

    /**
     * Check if browser should run in headless mode
     * @return true if headless mode enabled
     */
    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    /**
     * Get explicit wait timeout in seconds
     * @return timeout in seconds
     */
    public int getTimeout() {
        return getIntProperty("timeout", 10);
    }

    /**
     * Get implicit wait timeout in seconds
     * @return implicit wait timeout
     */
    public int getImplicitWait() {
        return getIntProperty("implicit.wait", 5);
    }

    /**
     * Get page load timeout in seconds
     * @return page load timeout
     */
    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout", 30);
    }

    // Additional convenience methods for Demoblaze specific configuration

    /**
     * Get valid login username
     * @return username for login tests
     */
    public String getValidUsername() {
        return getProperty("login.valid.username", "admin");
    }

    /**
     * Get valid login password
     * @return password for login tests
     */
    public String getValidPassword() {
        return getProperty("login.valid.password", "admin");
    }

    /**
     * Get invalid login username for negative testing
     * @return invalid username
     */
    public String getInvalidUsername() {
        return getProperty("login.invalid.username", "invaliduser");
    }

    /**
     * Get invalid login password for negative testing
     * @return invalid password
     */
    public String getInvalidPassword() {
        return getProperty("login.invalid.password", "wrongpassword");
    }

    /**
     * Get screenshot directory path
     * @return path for storing screenshots
     */
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "test-output/screenshots/");
    }

    /**
     * Get reports directory path
     * @return path for storing test reports
     */
    public String getReportsPath() {
        return getProperty("reports.path", "test-output/reports/");
    }

    /**
     * Check if screenshot capture is enabled on failure
     * @return true if screenshots should be captured on failure
     */
    public boolean isCaptureScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    /**
     * Get retry count for failed tests
     * @return number of retries for failed tests
     */
    public int getRetryCount() {
        return getIntProperty("retry.count", 0);
    }

    /**
     * Check if test execution should continue on failure
     * @return true if tests should continue after failure
     */
    public boolean isContinueOnFailure() {
        return getBooleanProperty("continue.on.failure", false);
    }

    /**
     * Get maximum number of parallel threads for test execution
     * @return thread count for parallel execution
     */
    public int getThreadCount() {
        return getIntProperty("thread.count", 1);
    }

    /**
     * Get test data file path
     * @return path to test data file
     */
    public String getTestDataPath() {
        return getProperty("testdata.path", "src/test/resources/testdata/");
    }
}