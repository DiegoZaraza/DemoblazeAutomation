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

import java.util.regex.Pattern;

@Epic("Purchase Flow")
public class PurchaseTest extends BaseTest {

    @Feature("Complete Purchase")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Complete purchase flow - add product to cart and checkout")
    public void testCompletePurchaseFlow() {
        logger.info("Starting complete purchase flow test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Navigate to Phones category
        homePage.clickCategory(TestData.Categories.PHONES);

        // Select and add Samsung Galaxy S6 to cart
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);

        // Verify product page loads and add to cart
        Assert.assertTrue(productPage.isProductDisplayed(), "Product page should be displayed");
        boolean addedToCart = productPage.addToCart();
        Assert.assertTrue(addedToCart, "Product should be successfully added to cart");

        // Navigate to cart
        homePage.goToCart();

        // Verify cart has items
        cartPage.waitForCartToLoad();
        Assert.assertTrue(cartPage.hasItems(), "Cart should contain items");

        // Verify total calculation is correct
        Assert.assertTrue(cartPage.validateTotal(), "Cart total should match sum of item prices");

        // Complete purchase
        PurchaseData purchaseData = TestData.getDefaultPurchaseData();
        String confirmationMessage = cartPage.completePurchase(purchaseData);

        // Verify purchase confirmation contains order ID
        Assert.assertNotNull(confirmationMessage, "Confirmation message should not be null");
        Pattern orderIdPattern = Pattern.compile(TestData.Messages.ORDER_CONFIRMATION_PATTERN);
        Assert.assertTrue(orderIdPattern.matcher(confirmationMessage).find(),
                "Confirmation message should contain order ID");

        logger.info("Complete purchase flow test completed successfully");
        logger.info("Order confirmation: {}", confirmationMessage);
    }

    @Feature("Multiple Products Purchase")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Add multiple products to cart and verify total")
    public void testMultipleProductsPurchase() {
        logger.info("Starting multiple products purchase test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Add first product (Samsung Galaxy S6)
        homePage.clickCategory(TestData.Categories.PHONES);
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);
        Assert.assertTrue(productPage.addToCart(), "First product should be added to cart");

        // Navigate back and add second product
        driver.navigate().back();
        homePage.clickProduct(TestData.Products.NEXUS_6);
        Assert.assertTrue(productPage.addToCart(), "Second product should be added to cart");

        // Go to cart and verify
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Cart should contain at least 2 items");

        // Verify total calculation
        Assert.assertTrue(cartPage.validateTotal(),
                "Cart total should correctly sum all item prices");

        logger.info("Multiple products purchase test completed successfully");
    }

    @Feature("Product Details Consistency")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify product details are maintained through purchase flow")
    public void testProductDetailsConsistency() {
        logger.info("Starting product details consistency test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Navigate to product
        homePage.clickCategory(TestData.Categories.PHONES);
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);

        // Get product details
        String productName = productPage.getProductName();
        String productPrice = productPage.getProductPrice();

        Assert.assertNotNull(productName, "Product name should not be null");
        Assert.assertNotNull(productPrice, "Product price should not be null");
        Assert.assertFalse(productName.trim().isEmpty(), "Product name should not be empty");
        Assert.assertFalse(productPrice.trim().isEmpty(), "Product price should not be empty");

        // Add to cart
        Assert.assertTrue(productPage.addToCart(), "Product should be added to cart");

        // Go to cart and verify details are maintained
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        Assert.assertTrue(cartPage.hasItems(), "Cart should have items");
        Assert.assertTrue(cartPage.validateTotal(), "Total should be calculated correctly");

        logger.info("Product details consistency test completed successfully");
        logger.info("Product: {}, Price: {}", productName, productPrice);
    }

    @Feature("Mixed Category Purchase")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Test purchase with different product categories")
    public void testPurchaseFromDifferentCategories() {
        logger.info("Starting purchase from different categories test");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Add product from Phones
        homePage.clickCategory(TestData.Categories.PHONES);
        homePage.clickProduct(TestData.Products.SAMSUNG_GALAXY_S6);
        Assert.assertTrue(productPage.addToCart(), "Phone product should be added to cart");

        // Add product from Laptops
        homePage.clickCategory(TestData.Categories.LAPTOPS);
        homePage.clickProduct(TestData.Products.SONY_VAIO_I5);
        Assert.assertTrue(productPage.addToCart(), "Laptop product should be added to cart");

        // Go to cart and verify mixed category products
        homePage.goToCart();
        cartPage.waitForCartToLoad();

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Cart should contain products from different categories");
        Assert.assertTrue(cartPage.validateTotal(), "Total should be correct for mixed category products");

        // Complete purchase
        PurchaseData purchaseData = TestData.getDefaultPurchaseData();
        String confirmation = cartPage.completePurchase(purchaseData);
        Assert.assertNotNull(confirmation, "Purchase should be confirmed");

        logger.info("Purchase from different categories test completed successfully");
    }
}