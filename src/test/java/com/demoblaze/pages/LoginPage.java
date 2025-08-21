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

public class LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "logInModal")
    private WebElement loginModal;

    @FindBy(id = "loginusername")
    private WebElement usernameField;

    @FindBy(id = "loginpassword")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Log in') and @onclick='logIn()']")
    private WebElement loginSubmitButton;

    @FindBy(xpath = "//button[@class='btn btn-secondary' and @data-dismiss='modal']")
    private WebElement closeButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    public void waitForModalToBeVisible() {
        logger.info("Waiting for login modal to be visible");
        wait.until(ExpectedConditions.visibilityOf(loginModal));
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        logger.info("Clicking login submit button");
        wait.until(ExpectedConditions.elementToBeClickable(loginSubmitButton)).click();
    }

    public String handleAlert() {
        try {
            logger.info("Waiting for alert to be present");
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            logger.info("Alert text: {}", alertText);
            alert.accept();
            return alertText;
        } catch (Exception e) {
            logger.info("No alert present");
            return null;
        }
    }

    public boolean login(String username, String password) {
        try {
            waitForModalToBeVisible();
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();

            // Check if alert appears (login failed)
            String alertText = handleAlert();
            if (alertText != null) {
                logger.warn("Login failed with alert: {}", alertText);
                return false;
            }

            // If no alert, login was successful
            logger.info("Login successful for user: {}", username);
            return true;
        } catch (Exception e) {
            logger.error("Error during login process: {}", e.getMessage());
            return false;
        }
    }

    public void closeModal() {
        logger.info("Closing login modal");
        try {
            if (closeButton.isDisplayed()) {
                closeButton.click();
            }
        } catch (Exception e) {
            logger.warn("Could not close modal: {}", e.getMessage());
        }
    }
}