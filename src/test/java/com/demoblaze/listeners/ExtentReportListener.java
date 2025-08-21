package com.demoblaze.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportListener implements ITestListener {
    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportName = "DemoblazeTestReport_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/" + reportName);

        // Configure the report
        sparkReporter.config().setDocumentTitle("Demoblaze Automation Test Report");
        sparkReporter.config().setReportName("End-to-End Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Set system information
        extent.setSystemInfo("Application", "Demoblaze");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Browser", System.getProperty("browser", "Chrome"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Test Suite", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest extentTest = extent.createTest(testName, description != null ? description : testName);

        // Add categories/tags
        String className = result.getTestClass().getName();
        String category = className.substring(className.lastIndexOf('.') + 1).replace("Test", "");
        extentTest.assignCategory(category);

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test failed");
        test.get().log(Status.FAIL, result.getThrowable());

        // Add screenshot if available
        String screenshotPath = System.getProperty("screenshot.path");
        if (screenshotPath != null) {
            try {
                test.get().addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
            } catch (Exception e) {
                test.get().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test skipped");
        test.get().log(Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    public static ExtentTest getCurrentTest() {
        return test.get();
    }
}