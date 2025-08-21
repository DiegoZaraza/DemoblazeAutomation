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

    @Feature("Partial Profile Update")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test partial profile updates")
    public void testPartialProfileUpdate() {
        logger.info("Starting partial profile update test");

        ProfilePage profilePage = new ProfilePage(driver);

        // Set initial profile
        profilePage.setProfile(
                TestData.ProfileData.INITIAL_FULLNAME,
                TestData.ProfileData.INITIAL_EMAIL
        );

        // Update only fullname
        profilePage.partialUpdate("fullname", TestData.ProfileData.UPDATED_FULLNAME);

        // Verify fullname is updated but email remains the same
        Assert.assertEquals(profilePage.getFullname(), TestData.ProfileData.UPDATED_FULLNAME,
                "Fullname should be updated");
        Assert.assertEquals(profilePage.getEmail(), TestData.ProfileData.INITIAL_EMAIL,
                "Email should remain unchanged");

        // Update only email
        profilePage.partialUpdate("email", TestData.ProfileData.UPDATED_EMAIL);

        // Verify email is updated and fullname remains the updated value
        Assert.assertEquals(profilePage.getFullname(), TestData.ProfileData.UPDATED_FULLNAME,
                "Fullname should remain updated");
        Assert.assertEquals(profilePage.getEmail(), TestData.ProfileData.UPDATED_EMAIL,
                "Email should be updated");

        logger.info("Partial profile update test completed successfully");
    }

    @Feature("Session Persistence")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test profile data persistence across browser sessions (simulated)")
    public void testProfileSessionPersistence() {
        logger.info("Starting profile session persistence test");

        ProfilePage profilePage = new ProfilePage(driver);

        // Clear any existing profile data
        profilePage.clearProfile();
        Assert.assertFalse(profilePage.profileExists(), "Profile should not exist initially");

        // Set profile data
        profilePage.setProfile(
                TestData.ProfileData.INITIAL_FULLNAME,
                TestData.ProfileData.INITIAL_EMAIL
        );

        Assert.assertTrue(profilePage.profileExists(), "Profile should exist after setting");

        // Simulate session persistence by reloading multiple times
        for (int i = 0; i < 3; i++) {
            profilePage.reloadPage();
            Assert.assertTrue(profilePage.verifyProfile(
                            TestData.ProfileData.INITIAL_FULLNAME,
                            TestData.ProfileData.INITIAL_EMAIL),
                    "Profile should persist through reload " + (i + 1)
            );
        }

        logger.info("Profile session persistence test completed successfully");
    }

    @Feature("Profile Data Validation")
    @Severity(SeverityLevel.MINOR)
    @Test(description = "Test profile data validation and error handling")
    public void testProfileDataValidation() {
        logger.info("Starting profile data validation test");

        ProfilePage profilePage = new ProfilePage(driver);

        // Test with empty values
        profilePage.setProfile("", "");
        Assert.assertEquals(profilePage.getFullname(), "", "Empty fullname should be handled");
        Assert.assertEquals(profilePage.getEmail(), "", "Empty email should be handled");

        // Test with special characters
        String specialName = "João O'Connor-Smith";
        String specialEmail = "test+tag@example-domain.com";

        profilePage.setProfile(specialName, specialEmail);
        Assert.assertEquals(profilePage.getFullname(), specialName,
                "Special characters in fullname should be handled");
        Assert.assertEquals(profilePage.getEmail(), specialEmail,
                "Special characters in email should be handled");

        // Test profile clearing
        profilePage.clearProfile();
        Assert.assertFalse(profilePage.profileExists(), "Profile should be cleared");
        Assert.assertEquals(profilePage.getFullname(), "", "Fullname should be empty after clear");
        Assert.assertEquals(profilePage.getEmail(), "", "Email should be empty after clear");

        logger.info("Profile data validation test completed successfully");
    }

    @Feature("Profile Lifecycle")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test complete profile lifecycle")
    public void testProfileLifecycle() {
        logger.info("Starting complete profile lifecycle test");

        ProfilePage profilePage = new ProfilePage(driver);

        // 1. Initial state - no profile
        profilePage.clearProfile();
        Assert.assertFalse(profilePage.profileExists(), "Initially no profile should exist");

        // 2. Create profile
        profilePage.setProfile(
                TestData.ProfileData.INITIAL_FULLNAME,
                TestData.ProfileData.INITIAL_EMAIL
        );
        Assert.assertTrue(profilePage.profileExists(), "Profile should exist after creation");

        // 3. Read profile
        String retrievedName = profilePage.getFullname();
        String retrievedEmail = profilePage.getEmail();
        Assert.assertEquals(retrievedName, TestData.ProfileData.INITIAL_FULLNAME);
        Assert.assertEquals(retrievedEmail, TestData.ProfileData.INITIAL_EMAIL);

        // 4. Update profile
        profilePage.updateProfile(
                TestData.ProfileData.UPDATED_FULLNAME,
                TestData.ProfileData.UPDATED_EMAIL
        );

        // 5. Verify update
        Assert.assertTrue(profilePage.verifyProfile(
                TestData.ProfileData.UPDATED_FULLNAME,
                TestData.ProfileData.UPDATED_EMAIL
        ));

        // 6. Test persistence
        profilePage.reloadPage();
        Assert.assertTrue(profilePage.verifyProfile(
                TestData.ProfileData.UPDATED_FULLNAME,
                TestData.ProfileData.UPDATED_EMAIL
        ));

        // 7. Delete profile
        profilePage.clearProfile();
        Assert.assertFalse(profilePage.profileExists(), "Profile should not exist after deletion");

        logger.info("Complete profile lifecycle test completed successfully");
    }
}