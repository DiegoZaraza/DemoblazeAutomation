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

    @Feature("Category Uniqueness")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify categories show different products")
    public void testCategoriesShowUniqueProducts() {
        logger.info("Starting unique products per category test");

        HomePage homePage = new HomePage(driver);

        // Collect products from each category
        Set<String> phonesProducts = new HashSet<>();
        Set<String> laptopsProducts = new HashSet<>();
        Set<String> monitorsProducts = new HashSet<>();

        // Get products from Phones category
        homePage.clickCategory(TestData.Categories.PHONES);
        List<String> phones = homePage.getProductNames();
        phonesProducts.addAll(phones);
        logger.info("Phones category has {} products", phones.size());

        // Get products from Laptops category
        homePage.clickCategory(TestData.Categories.LAPTOPS);
        List<String> laptops = homePage.getProductNames();
        laptopsProducts.addAll(laptops);
        logger.info("Laptops category has {} products", laptops.size());

        // Get products from Monitors category
        homePage.clickCategory(TestData.Categories.MONITORS);
        List<String> monitors = homePage.getProductNames();
        monitorsProducts.addAll(monitors);
        logger.info("Monitors category has {} products", monitors.size());

        // Verify no overlap between categories
        Set<String> phonesLaptopsIntersection = new HashSet<>(phonesProducts);
        phonesLaptopsIntersection.retainAll(laptopsProducts);
        Assert.assertTrue(phonesLaptopsIntersection.isEmpty(),
                "Phones and Laptops categories should not have common products");

        Set<String> phonesMonitorsIntersection = new HashSet<>(phonesProducts);
        phonesMonitorsIntersection.retainAll(monitorsProducts);
        Assert.assertTrue(phonesMonitorsIntersection.isEmpty(),
                "Phones and Monitors categories should not have common products");

        Set<String> laptopsMonitorsIntersection = new HashSet<>(laptopsProducts);
        laptopsMonitorsIntersection.retainAll(monitorsProducts);
        Assert.assertTrue(laptopsMonitorsIntersection.isEmpty(),
                "Laptops and Monitors categories should not have common products");

        logger.info("Unique products per category test completed successfully");
    }

    @Feature("Category Navigation")
    @Severity(SeverityLevel.MINOR)
    @Test(description = "Verify category navigation and product count consistency")
    public void testCategoryNavigationConsistency() {
        logger.info("Starting category navigation consistency test");

        HomePage homePage = new HomePage(driver);

        for (String category : TestData.Categories.EXPECTED_CATEGORIES) {
            logger.info("Testing navigation consistency for category: {}", category);

            // Navigate to category multiple times and verify consistent product count
            homePage.clickCategory(category);
            int firstCount = homePage.getProductCount();

            // Navigate away and back
            homePage.clickCategory(TestData.Categories.PHONES); // Navigate to different category
            homePage.clickCategory(category); // Navigate back
            int secondCount = homePage.getProductCount();

            Assert.assertEquals(secondCount, firstCount,
                    "Product count should be consistent for category: " + category);

            logger.info("Category '{}' shows consistent product count: {}", category, firstCount);
        }

        logger.info("Category navigation consistency test completed successfully");
    }

    @Feature("Minimum Product Requirement")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify all categories have minimum expected products")
    public void testMinimumProductRequirement() {
        logger.info("Starting minimum product requirement test");

        HomePage homePage = new HomePage(driver);
        final int MINIMUM_PRODUCTS = 1;

        for (String category : TestData.Categories.EXPECTED_CATEGORIES) {
            homePage.clickCategory(category);
            int productCount = homePage.getProductCount();

            Assert.assertTrue(productCount >= MINIMUM_PRODUCTS,
                    String.format("Category '%s' should have at least %d product(s), but found %d",
                            category, MINIMUM_PRODUCTS, productCount));

            logger.info("Category '{}' meets minimum requirement with {} products",
                    category, productCount);
        }

        logger.info("Minimum product requirement test completed successfully");
    }
}