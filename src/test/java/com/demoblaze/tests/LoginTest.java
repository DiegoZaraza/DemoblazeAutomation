package com.demoblaze.tests;

import com.demoblaze.base.BaseTest;
import com.demoblaze.data.TestData;
import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("User Authentication")
public class LoginTest extends BaseTest {

    @Feature("Valid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        logger.info("Starting valid login test");

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Click login button to open modal
        homePage.clickLogin();

        // Attempt login
        boolean loginSuccess = loginPage.login(
                TestData.Credentials.VALID_USERNAME,
                TestData.Credentials.VALID_PASSWORD
        );

        if (!loginSuccess) {
            // If login failed, it means the environment doesn't accept admin/admin
            logger.warn("Environment does not accept admin/admin credentials - documenting as finding");
            Assert.fail("Environment does not accept admin/admin - documented as site finding");
        } else {
            // Verify successful login
            boolean isLoggedIn = homePage.isUserLoggedIn(TestData.Credentials.VALID_USERNAME);
            Assert.assertTrue(isLoggedIn, "User should be logged in after successful login");
            logger.info("Valid login test completed successfully");
        }
    }

    @Feature("Invalid Login")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify login failure with invalid credentials")
    public void testInvalidLogin() {
        logger.info("Starting invalid login test");

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Click login button to open modal
        homePage.clickLogin();

        // Attempt login with invalid credentials
        boolean loginSuccess = loginPage.login(
                TestData.Credentials.INVALID_USERNAME,
                TestData.Credentials.INVALID_PASSWORD
        );

        // Login should fail
        Assert.assertFalse(loginSuccess, "Login should fail with invalid credentials");

        // Verify user is not logged in
        boolean isLoggedIn = homePage.isUserLoggedIn(TestData.Credentials.INVALID_USERNAME);
        Assert.assertFalse(isLoggedIn, "User should not be logged in with invalid credentials");

        logger.info("Invalid login test completed successfully");
    }
}