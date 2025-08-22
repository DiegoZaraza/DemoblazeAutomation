package com.demoblaze.tests;

import com.demoblaze.base.BaseTest;
import com.demoblaze.data.TestData;
import com.demoblaze.pages.HomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Epic("Category Management")
public class CategoryTest extends BaseTest {

    @Feature("Category Existence")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify that required categories exist")
    public void testCategoriesExist() {
        logger.info("Starting categories existence test");

        HomePage homePage = new HomePage(driver);

        // Verify each expected category is displayed
        for (String category : TestData.Categories.EXPECTED_CATEGORIES) {
            boolean categoryDisplayed = homePage.isCategoryDisplayed(category);
            Assert.assertTrue(categoryDisplayed,
                    "Category '" + category + "' should be displayed on the home page");
            logger.info("Category '{}' is displayed", category);
        }

        logger.info("Categories existence test completed successfully");
    }

    @Feature("Category Product Display")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify each category shows correct products")
    public void testCategoryProductsDisplay() {
        logger.info("Starting category products display test");

        HomePage homePage = new HomePage(driver);

        for (String category : TestData.Categories.EXPECTED_CATEGORIES) {
            logger.info("Testing category: {}", category);

            // Click on category
            homePage.clickCategory(category);

            // Wait and verify products are displayed
            int productCount = homePage.getProductCount();
            Assert.assertTrue(productCount > 0,
                    "Category '" + category + "' should display at least one product");

            logger.info("Category '{}' displays {} products", category, productCount);
        }

        logger.info("Category products display test completed successfully");
    }
}