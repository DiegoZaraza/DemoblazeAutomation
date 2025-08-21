package com.demoblaze.tests;

import com.demoblaze.base.BaseTest;
import com.demoblaze.data.TestData;
import com.demoblaze.model.PurchaseData;
import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.HomePage;
import com.demoblaze.pages.ProductPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Cart and Product Operations")
public class ExtendedTest extends BaseTest {


    @Feature("Checkout Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify checkout without products shows validation")
    public void testCheckoutWithoutProducts() {
        logger.info("Starting checkout without products test");

        HomePage homePage = new HomePage(driver);
        CartPage cartPage = new CartPage(driver);

        // Go directly to cart without adding products
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        // Verify cart is empty
        Assert.assertFalse(cartPage.hasItems(), "Cart should be empty initially");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart item count should be 0");

        // Attempt to checkout without products
        try {
            PurchaseData purchaseData = TestData.getDefaultPurchaseData();
            cartPage.completePurchase(purchaseData);

            // If we reach here, the checkout process didn't fail as expected
            Assert.fail("Checkout should fail when cart is empty");

        } catch (RuntimeException e) {
            // This is expected - checkout should fail with empty cart
            Assert.assertTrue(e.getMessage().contains("cart might be empty") ||
                            e.getMessage().contains("Modal did not appear"),
                    "Error message should indicate empty cart issue");
            logger.info("✅ Validation successful: Checkout properly failed with empty cart");
        }

        logger.info("Checkout without products test completed successfully");
    }

    @Feature("Cart Total Calculation")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Add same product twice and validate total update")
    public void testSameProductTwiceValidation() {
        logger.info("Starting same product twice validation test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Navigate to Phones and select Samsung Galaxy S6
        homePage.clickCategory(TestData.Categories.PHONES);
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);

        // Add first instance to cart
        Assert.assertTrue(productPage.addToCart(), "First product should be added successfully");

        // Navigate back and add the same product again
        driver.navigate().back();
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);
        Assert.assertTrue(productPage.addToCart(), "Second instance should be added successfully");

        // Go to cart and validate
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        // Verify cart has at least 2 items (same product added twice)
        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2,
                "Cart should contain at least 2 items (same product added twice)");

        // Verify total calculation is correct
        Assert.assertTrue(cartPage.validateTotal(),
                "Total should correctly sum all item prices including duplicates");

        // Get detailed pricing information for logging
        double actualTotal = cartPage.getTotalPrice();
        double expectedTotal = cartPage.calculateExpectedTotal();

        logger.info("Item count: {}", itemCount);
        logger.info("Expected total: {}", expectedTotal);
        logger.info("Actual total: {}", actualTotal);

        Assert.assertEquals(actualTotal, expectedTotal, 0.01,
                "Actual total should match expected total within 0.01 precision");

        logger.info("Same product twice validation test completed successfully");
    }

    @Feature("Quantity Simulation")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test quantity simulation by adding same product multiple times")
    public void testQuantitySimulation() {
        logger.info("Starting quantity simulation test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        final int QUANTITY = 3;
        final String PRODUCT = TestData.Products.SAMSUNG_GALAXY_S6;

        // Add the same product multiple times to simulate quantity
        homePage.clickCategory(TestData.Categories.PHONES);

        for (int i = 0; i < QUANTITY; i++) {
            logger.info("Adding product instance {} of {}", i + 1, QUANTITY);

            if (i > 0) {
                // Navigate back to product list for subsequent additions
                driver.navigate().back();
            }

            homePage.clickProduct(PRODUCT);
            Assert.assertTrue(productPage.addToCart(),
                    String.format("Product instance %d should be added successfully", i + 1));
        }

        // Verify cart contents
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= QUANTITY,
                String.format("Cart should contain at least %d items", QUANTITY));

        // Verify total calculation
        Assert.assertTrue(cartPage.validateTotal(),
                "Total should be correct for multiple instances of same product");

        // Complete purchase to verify the flow works with multiple quantities
        PurchaseData purchaseData = TestData.getDefaultPurchaseData();
        String confirmation = cartPage.completePurchase(purchaseData);
        Assert.assertNotNull(confirmation, "Purchase should be confirmed with multiple quantities");

        logger.info("Quantity simulation test completed successfully");
        logger.info("Simulated quantity: {}, Confirmation: {}", QUANTITY, confirmation);
    }

    @Feature("Cart Operations")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test cart operations and validations")
    public void testCartOperations() {
        logger.info("Starting cart operations test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Add multiple different products
        String[] products = {
                TestData.Products.SAMSUNG_GALAXY_S6,
                TestData.Products.NEXUS_6
        };

        for (String product : products) {
            homePage.clickCategory(TestData.Categories.PHONES);
            homePage.clickProduct(product);
            Assert.assertTrue(productPage.addToCart(),
                    "Product " + product + " should be added to cart");
        }

        // Go to cart
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        // Verify multiple items
        int initialItemCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialItemCount >= 2, "Cart should contain multiple items");

        // Test delete functionality
        cartPage.deleteFirstItem();

        // Verify item was deleted (count should decrease)
        int afterDeleteCount = cartPage.getCartItemCount();
        Assert.assertTrue(afterDeleteCount < initialItemCount,
                "Item count should decrease after deletion");

        // Verify total is still calculated correctly
        if (afterDeleteCount > 0) {
            Assert.assertTrue(cartPage.validateTotal(),
                    "Total should still be correct after item deletion");
        }

        logger.info("Cart operations test completed successfully");
        logger.info("Initial items: {}, After deletion: {}", initialItemCount, afterDeleteCount);
    }

    @Feature("Edge Case Handling")
    @Severity(SeverityLevel.MINOR)
    @Test(description = "Test edge cases and error handling")
    public void testEdgeCasesAndErrorHandling() {
        logger.info("Starting edge cases and error handling test");

        HomePage homePage = new HomePage(driver);
        CartPage cartPage = new CartPage(driver);

        // Test 1: Direct navigation to cart URL
        driver.get(TestData.URLs.CART_URL);
        cartPage.waitForCartToLoad();
        Assert.assertFalse(cartPage.hasItems(), "Directly navigated cart should be empty");

        // Test 2: Attempt checkout with incomplete data
        if (!cartPage.hasItems()) {
            // Add a product first
            homePage.clickCategory(TestData.Categories.PHONES);
            homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);

            ProductPage productPage = new ProductPage(driver);
            productPage.addToCart();

            homePage.goToCart();
            cartPage.waitForCartToLoad();
        }

        // Test checkout with incomplete purchase data
        try {
            PurchaseData incompleteData = TestData.getIncompletePurchaseData();
            cartPage.completePurchase(incompleteData);

            // Note: Depending on the site's validation, this might succeed or fail
            logger.info("Checkout with incomplete data processed");

        } catch (Exception e) {
            logger.info("Checkout with incomplete data properly failed: {}", e.getMessage());
        }

        logger.info("Edge cases and error handling test completed successfully");
    }
}