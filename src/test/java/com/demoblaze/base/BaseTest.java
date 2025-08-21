package com.demoblaze.base;

import com.demoblaze.utils.ConfigReader;
import com.demoblaze.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigReader config;

    @BeforeClass
    @Parameters({"browser", "baseUrl"})
    public void setUp(@Optional("chrome") String browser, @Optional("https://www.demoblaze.com/") String baseUrl) {
        logger.info("Setting up test environment");
        logger.info("Browser: " + browser);
        logger.info("Base URL: " + baseUrl);

        config = new ConfigReader();
        driver = DriverManager.getDriver(browser);
        driver.manage().window().maximize();
        driver.get(baseUrl);

        logger.info("Test setup completed successfully");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down test environment");
        if (driver != null) {
            DriverManager.quitDriver();
            logger.info("Driver closed successfully");
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        logger.info("Navigating to home page before test method");
        driver.get("https://www.demoblaze.com/");
    }
}