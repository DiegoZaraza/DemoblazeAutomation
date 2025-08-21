package com.demoblaze.listeners;

import com.demoblaze.utils.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ScreenshotListener.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Get the current driver instance
            WebDriver driver = DriverManager.getDriver(System.getProperty("browser", "chrome"));

            if (driver != null) {
                // Take screenshot
                TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
                File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

                // Generate unique filename
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String fileName = String.format("%s_%s_%s.png",
                        className.substring(className.lastIndexOf('.') + 1),
                        testName,
                        timestamp);

                File destFile = new File(SCREENSHOT_DIR + fileName);

                // Copy screenshot to destination
                FileUtils.copyFile(sourceFile, destFile);

                // Set system property for ExtentReportListener
                System.setProperty("screenshot.path", destFile.getAbsolutePath());

                logger.info("Screenshot captured for failed test: {}", fileName);
            } else {
                logger.warn("No driver instance available for screenshot");
            }

        } catch (Exception e) {
            logger.error("Failed to capture screenshot for test failure: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Clear any previous screenshot path
        System.clearProperty("screenshot.path");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Clear any previous screenshot path
        System.clearProperty("screenshot.path");
    }
}