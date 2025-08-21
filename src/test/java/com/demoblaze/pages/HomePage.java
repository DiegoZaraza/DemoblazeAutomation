package com.demoblaze.pages;

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

public class HomePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "login2")
    private WebElement loginButton;

    @FindBy(id = "nameofuser")
    private WebElement welcomeUser;

    @FindBy(id = "cartur")
    private WebElement cartLink;

    @FindBy(css = "#itemc")
    private List<WebElement> categoryButtons;

    @FindBy(linkText = "Phones")
    private WebElement phonesCategory;

    @FindBy(linkText = "Laptops")
    private WebElement laptopsCategory;

    @FindBy(linkText = "Monitors")
    private WebElement monitorsCategory;

    @FindBy(css = ".card")
    private List<WebElement> productCards;

    @FindBy(css = ".card-title a")
    private List<WebElement> productLinks;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }

    public void clickLogin() {
        logger.info("Clicking login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isUserLoggedIn(String username) {
        try {
            String expectedText = "Welcome " + username;
            wait.until(ExpectedConditions.textToBePresentInElement(welcomeUser, expectedText));
            logger.info("User {} is logged in", username);
            return true;
        } catch (Exception e) {
            logger.warn("User {} is not logged in", username);
            return false;
        }
    }

    public void goToCart() {
        logger.info("Navigating to cart");
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    public void clickCategory(String categoryName) {
        logger.info("Clicking on category: {}", categoryName);
        WebElement category;

        switch (categoryName.toLowerCase()) {
            case "phones":
                category = phonesCategory;
                break;
            case "laptops":
                category = laptopsCategory;
                break;
            case "monitors":
                category = monitorsCategory;
                break;
            default:
                throw new IllegalArgumentException("Category not supported: " + categoryName);
        }

        wait.until(ExpectedConditions.elementToBeClickable(category)).click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                org.openqa.selenium.By.cssSelector(".card")));
    }

    public boolean isCategoryDisplayed(String categoryName) {
        try {
            for (WebElement category : categoryButtons) {
                if (category.getText().trim().equalsIgnoreCase(categoryName)) {
                    logger.info("Category {} is displayed", categoryName);
                    return true;
                }
            }
            logger.warn("Category {} is not displayed", categoryName);
            return false;
        } catch (Exception e) {
            logger.error("Error checking category display: {}", e.getMessage());
            return false;
        }
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                org.openqa.selenium.By.cssSelector(".card")));
        int count = productCards.size();
        logger.info("Found {} products on the page", count);
        return count;
    }

    public void clickProduct(String productName) {
        logger.info("Clicking on product: {}", productName);
        for (WebElement productLink : productLinks) {
            if (productLink.getText().trim().contains(productName)) {
                wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    public List<String> getProductNames() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                org.openqa.selenium.By.cssSelector(".card-title a")));

        return productLinks.stream()
                .map(element -> element.getText().trim())
                .collect(java.util.stream.Collectors.toList());
    }
}