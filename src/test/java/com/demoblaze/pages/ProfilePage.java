package com.demoblaze.pages;

import com.demoblaze.data.TestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ProfilePage simulates user profile management using localStorage
 * since Demoblaze doesn't have native profile functionality
 */
public class ProfilePage {
    private static final Logger logger = LogManager.getLogger(ProfilePage.class);
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        logger.info("ProfilePage initialized");
    }

    public void setProfile(String fullname, String email) {
        logger.info("Setting profile data - Fullname: {}, Email: {}", fullname, email);

        String script = String.format(
                "localStorage.setItem('%s', JSON.stringify({fullname: '%s', email: '%s'}));",
                TestData.ProfileData.PROFILE_KEY, fullname, email
        );

        jsExecutor.executeScript(script);
        logger.info("Profile data set in localStorage");
    }

    public void updateProfile(String fullname, String email) {
        logger.info("Updating profile data - Fullname: {}, Email: {}", fullname, email);

        String script = String.format(
                "var current = JSON.parse(localStorage.getItem('%s') || '{}');" +
                        "var updated = Object.assign(current, {fullname: '%s', email: '%s'});" +
                        "localStorage.setItem('%s', JSON.stringify(updated));",
                TestData.ProfileData.PROFILE_KEY, fullname, email, TestData.ProfileData.PROFILE_KEY
        );

        jsExecutor.executeScript(script);
        logger.info("Profile data updated in localStorage");
    }

    public Map<String, String> getProfile() {
        logger.info("Retrieving profile data from localStorage");

        String script = String.format(
                "return localStorage.getItem('%s');",
                TestData.ProfileData.PROFILE_KEY
        );

        String profileJson = (String) jsExecutor.executeScript(script);
        Map<String, String> profile = new HashMap<>();

        if (profileJson != null && !profileJson.isEmpty()) {
            // Parse JSON manually (simple parsing for our use case)
            profileJson = profileJson.replace("{", "").replace("}", "").replace("\"", "");
            String[] pairs = profileJson.split(",");

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    profile.put(key, value);
                }
            }
        }

        logger.info("Retrieved profile data: {}", profile);
        return profile;
    }

    public boolean verifyProfile(String expectedFullname, String expectedEmail) {
        logger.info("Verifying profile data - Expected Fullname: {}, Email: {}",
                expectedFullname, expectedEmail);

        Map<String, String> currentProfile = getProfile();

        boolean fullnameMatch = expectedFullname.equals(currentProfile.get("fullname"));
        boolean emailMatch = expectedEmail.equals(currentProfile.get("email"));
        boolean isValid = fullnameMatch && emailMatch;

        logger.info("Profile verification result: {}", isValid);
        if (!isValid) {
            logger.warn("Profile mismatch - Current: {}, Expected: fullname={}, email={}",
                    currentProfile, expectedFullname, expectedEmail);
        }

        return isValid;
    }

    public void clearProfile() {
        logger.info("Clearing profile data from localStorage");

        String script = String.format(
                "localStorage.removeItem('%s');",
                TestData.ProfileData.PROFILE_KEY
        );

        jsExecutor.executeScript(script);
        logger.info("Profile data cleared");
    }

    public boolean profileExists() {
        Map<String, String> profile = getProfile();
        boolean exists = !profile.isEmpty();
        logger.info("Profile exists: {}", exists);
        return exists;
    }

    public void reloadPage() {
        logger.info("Reloading page to test persistence");
        driver.navigate().refresh();
    }

    public String getFullname() {
        Map<String, String> profile = getProfile();
        String fullname = profile.getOrDefault("fullname", "");
        logger.info("Current fullname: {}", fullname);
        return fullname;
    }

    public String getEmail() {
        Map<String, String> profile = getProfile();
        String email = profile.getOrDefault("email", "");
        logger.info("Current email: {}", email);
        return email;
    }

    public void partialUpdate(String field, String value) {
        logger.info("Partially updating profile - Field: {}, Value: {}", field, value);

        String script = String.format(
                "var current = JSON.parse(localStorage.getItem('%s') || '{}');" +
                        "current['%s'] = '%s';" +
                        "localStorage.setItem('%s', JSON.stringify(current));",
                TestData.ProfileData.PROFILE_KEY, field, value, TestData.ProfileData.PROFILE_KEY
        );

        jsExecutor.executeScript(script);
        logger.info("Profile field updated");
    }
}