package com.demoblaze.pages;

import com.demoblaze.model.PurchaseData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class CartPage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "#tbodyid .success")
    private List<WebElement> cartItems;

    @FindBy(css = "#tbodyid .success td:nth-child(3)")
    private List<WebElement> itemPrices;

    @FindBy(id = "totalp")
    private WebElement totalPrice;

    @FindBy(xpath = "//button[contains(text(),'Place Order')]")
    private WebElement placeOrderButton;

    @FindBy(css = "a:contains('Delete')")
    private List<WebElement> deleteButtons;

    // Order Modal Elements
    @FindBy(id = "orderModal")
    private WebElement orderModal;

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "country")
    private WebElement countryField;

    @FindBy(id = "city")
    private WebElement cityField;

    @FindBy(id = "card")
    private WebElement cardField;

    @FindBy(id = "month")
    private WebElement monthField;

    @FindBy(id = "year")
    private WebElement yearField;

    @FindBy(xpath = "//button[contains(text(),'Purchase')]")
    private WebElement purchaseButton;

    @FindBy(css = ".sweet-alert.showSweetAlert.visible")
    private WebElement confirmationAlert;

    @FindBy(css = ".sweet-alert .lead")
    private WebElement confirmationText;

    @FindBy(xpath = "//button[contains(text(),'OK')]")
    private WebElement okButton;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("CartPage initialized");
    }

    public void waitForCartToLoad() {
        logger.info("Waiting for cart page to load");
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
    }

    public int getCartItemCount() {
        waitForCartToLoad();
        int count = cartItems.size();
        logger.info("Cart contains {} items", count);
        return count;
    }

    public List<Double> getItemPrices() {
        List<Double> prices = new ArrayList<>();
        logger.info("Getting item prices from cart");

        for (WebElement priceElement : itemPrices) {
            try {
                String priceText = priceElement.getText().trim();
                // Remove any currency symbols and convert to double
                String cleanPrice = priceText.replaceAll("[^0-9.]", "");
                double price = Double.parseDouble(cleanPrice);
                prices.add(price);
                logger.debug("Item price: {}", price);
            } catch (NumberFormatException e) {
                logger.error("Could not parse price: {}", priceElement.getText().trim());
            }
        }

        logger.info("Retrieved {} item prices", prices.size());
        return prices;
    }

    public double getTotalPrice() {
        try {
            String totalText = totalPrice.getText().trim();
            String cleanTotal = totalText.replaceAll("[^0-9.]", "");
            double total = Double.parseDouble(cleanTotal);
            logger.info("Cart total: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Could not get total price: {}", e.getMessage());
            return 0.0;
        }
    }

    public boolean hasItems() {
        waitForCartToLoad();
        boolean hasItems = getCartItemCount() > 0;
        logger.info("Cart has items: {}", hasItems);
        return hasItems;
    }

    public void deleteFirstItem() {
        if (!deleteButtons.isEmpty()) {
            logger.info("Deleting first item from cart");
            deleteButtons.get(0).click();
            // Wait a moment for the item to be removed
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            logger.warn("No items to delete from cart");
        }
    }

    public void clickPlaceOrder() {
        logger.info("Clicking Place Order button");
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
    }

    public boolean isOrderModalVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderModal));
            logger.info("Order modal is visible");
            return true;
        } catch (Exception e) {
            logger.warn("Order modal is not visible: {}", e.getMessage());
            return false;
        }
    }

    public void fillOrderForm(PurchaseData purchaseData) {
        logger.info("Filling order form with purchase data");

        wait.until(ExpectedConditions.elementToBeClickable(nameField));
        nameField.clear();
        nameField.sendKeys(purchaseData.getName());

        countryField.clear();
        countryField.sendKeys(purchaseData.getCountry());

        cityField.clear();
        cityField.sendKeys(purchaseData.getCity());

        cardField.clear();
        cardField.sendKeys(purchaseData.getCard());

        monthField.clear();
        monthField.sendKeys(purchaseData.getMonth());

        yearField.clear();
        yearField.sendKeys(purchaseData.getYear());

        logger.info("Order form filled successfully");
    }

    public void clickPurchase() {
        logger.info("Clicking Purchase button");
        wait.until(ExpectedConditions.elementToBeClickable(purchaseButton)).click();
    }

    public String getConfirmationMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmationAlert));
            String message = confirmationText.getText().trim();
            logger.info("Order confirmation message: {}", message);
            return message;
        } catch (Exception e) {
            logger.error("Could not get confirmation message: {}", e.getMessage());
            return null;
        }
    }

    public void clickOK() {
        logger.info("Clicking OK button on confirmation");
        wait.until(ExpectedConditions.elementToBeClickable(okButton)).click();
    }

    public String completePurchase(PurchaseData purchaseData) {
        try {
            clickPlaceOrder();

            if (!isOrderModalVisible()) {
                throw new RuntimeException("Order modal did not appear - cart might be empty");
            }

            fillOrderForm(purchaseData);
            clickPurchase();

            String confirmationMessage = getConfirmationMessage();
            if (confirmationMessage == null) {
                throw new RuntimeException("Purchase confirmation did not appear");
            }

            clickOK();
            logger.info("Purchase completed successfully");
            return confirmationMessage;

        } catch (Exception e) {
            logger.error("Error completing purchase: {}", e.getMessage());
            throw new RuntimeException("Purchase failed: " + e.getMessage());
        }
    }

    public double calculateExpectedTotal() {
        List<Double> prices = getItemPrices();
        double expectedTotal = prices.stream().mapToDouble(Double::doubleValue).sum();
        logger.info("Calculated expected total: {}", expectedTotal);
        return expectedTotal;
    }

    public boolean validateTotal() {
        double actualTotal = getTotalPrice();
        double expectedTotal = calculateExpectedTotal();
        boolean isValid = Math.abs(actualTotal - expectedTotal) < 0.01; // Account for floating point precision

        logger.info("Total validation - Expected: {}, Actual: {}, Valid: {}",
                expectedTotal, actualTotal, isValid);
        return isValid;
    }
}