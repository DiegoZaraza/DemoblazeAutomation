package com.demoblaze.tests;

import com.demoblaze.base.BaseTest;
import com.demoblaze.data.TestData;
import com.demoblaze.pages.ProfilePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("User Profile Management")
public class ProfileTest extends BaseTest {

    @Feature("Profile Persistence")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify profile changes persist between page reloads")
    public void testProfilePersistence() {
        logger.info("Starting profile persistence test");

        ProfilePage profilePage = new ProfilePage(driver);

        // Set initial profile data
        profilePage.setProfile(
                TestData.ProfileData.INITIAL_FULLNAME,
                TestData.ProfileData.INITIAL_EMAIL
        );

        // Verify initial data is set
        Assert.assertTrue(profilePage.verifyProfile(
                        TestData.ProfileData.INITIAL_FULLNAME,
                        TestData.ProfileData.INITIAL_EMAIL),
                "Initial profile data should be set correctly"
        );

        // Update profile data
        profilePage.updateProfile(
                TestData.ProfileData.UPDATED_FULLNAME,
                TestData.ProfileData.UPDATED_EMAIL
        );

        // Verify updated data
        Assert.assertTrue(profilePage.verifyProfile(
                        TestData.ProfileData.UPDATED_FULLNAME,
                        TestData.ProfileData.UPDATED_EMAIL),
                "Updated profile data should be correct"
        );

        // Reload page to test persistence
        profilePage.reloadPage();

        // Verify data persists after reload
        Assert.assertTrue(profilePage.verifyProfile(
                        TestData.ProfileData.UPDATED_FULLNAME,
                        TestData.ProfileData.UPDATED_EMAIL),
                "Profile data should persist after page reload"
        );

        logger.info("Profile persistence test completed successfully");
    }
}