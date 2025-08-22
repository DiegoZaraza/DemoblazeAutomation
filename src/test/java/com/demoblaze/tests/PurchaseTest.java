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
}