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

    @Feature("Cart Operations")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test cart operations and validations")
    public void testCartOperations() throws InterruptedException {
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
            Thread.sleep(2000); // Wait for modal to close

            homePage.goToHome();
        }

        // Go to cart
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        // Verify multiple items
        int initialItemCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialItemCount >= 2, "Cart should contain multiple items");

        // Test delete functionality
        cartPage.deleteFirstItem();
        cartPage.waitForCartToLoad();

        Thread.sleep(2000);

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
}