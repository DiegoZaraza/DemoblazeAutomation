package com.demoblaze.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver(String browserName) {
        if (driver.get() == null) {
            driver.set(createDriver(browserName));
        }
        return driver.get();
    }

    private static WebDriver createDriver(String browserName) {
        WebDriver webDriver = null;
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        logger.info("Creating {} driver (headless: {})", browserName, headless);

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                webDriver = new EdgeDriver(edgeOptions);
                break;

            default:
                logger.error("Unsupported browser: " + browserName);
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }

        logger.info("Driver created successfully");
        return webDriver;
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Closing driver");
            driver.get().quit();
            driver.remove();
        }
    }
}