package com.demoblaze.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Alert;

import java.time.Duration;

public class ProductPage {
    private static final Logger logger = LogManager.getLogger(ProductPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//a[contains(@onclick,'addToCart')]")
    private WebElement addToCartButton;

    @FindBy(css = ".name")
    private WebElement productName;

    @FindBy(css = ".price-container")
    private WebElement productPrice;

    @FindBy(css = "#more-information")
    private WebElement productDescription;

    @FindBy(css = ".item img")
    private WebElement productImage;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("ProductPage initialized");
    }

    public void waitForPageToLoad() {
        logger.info("Waiting for product page to load");
        wait.until(ExpectedConditions.visibilityOf(productName));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
    }

    public String getProductName() {
        waitForPageToLoad();
        String name = productName.getText().trim();
        logger.info("Product name: {}", name);
        return name;
    }

    public String getProductPrice() {
        String price = productPrice.getText().trim();
        logger.info("Product price: {}", price);
        return price;
    }

    public boolean addToCart() {
        try {
            logger.info("Adding product to cart");
            waitForPageToLoad();
            addToCartButton.click();

            // Handle the "Product added" alert
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            logger.info("Alert text after adding to cart: {}", alertText);
            alert.accept();

            // Verify the alert contains expected message
            boolean success = alertText.contains("Product added");
            if (success) {
                logger.info("Product successfully added to cart");
            } else {
                logger.warn("Unexpected alert message when adding to cart: {}", alertText);
            }

            return success;
        } catch (Exception e) {
            logger.error("Error adding product to cart: {}", e.getMessage());
            return false;
        }
    }

    public boolean isProductDisplayed() {
        try {
            waitForPageToLoad();
            boolean displayed = productName.isDisplayed() &&
                    addToCartButton.isDisplayed() &&
                    productPrice.isDisplayed();
            logger.info("Product page fully displayed: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Error checking if product is displayed: {}", e.getMessage());
            return false;
        }
    }

    public String getProductDescription() {
        try {
            String description = productDescription.getText().trim();
            logger.info("Product description length: {}", description.length());
            return description;
        } catch (Exception e) {
            logger.warn("Could not get product description: {}", e.getMessage());
            return "";
        }
    }
}