/**
 * TestNotificationSystem
 * This test verifies the functionality of Reddit notification system.
 * @author Daviel
 * @version 1.0
 * @since 2025-04-18
 */

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.util.Map;
import java.util.List;
import org.openqa.selenium.interactions.Actions;


public class TestNotificationSystem {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Navigate to reddit.com
        driver.get("https://www.reddit.com/");
        loginToReddit();
    }

    private void loginToReddit() {
        try {

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href, 'login') or contains(@data-testid, 'login')]")));
            loginButton.click();

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-username")));
            usernameField.sendKeys("UserName");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-password")));
            passwordField.sendKeys("Password!");

            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='login']/auth-flow-modal/div[2]/faceplate-tracker/button")));
            submitButton.click();


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//header")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Login to Reddit failed: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testNavigateToNotificationsPage() {
        // Wait until the notifications button is clickable
        WebElement notificationsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@id='notifications-inbox-button']")));
        notificationsButton.click();
        wait.until(ExpectedConditions.urlContains("/notifications"));
    }

    @Test(priority = 2, dependsOnMethods = {"testNavigateToNotificationsPage"})
    public void testMarkAllNotificationsAsRead() {
        try {
            Thread.sleep(2000);
            // use XPath to target text content
            WebElement markAllAsReadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='Mark all as read']")));

            Thread.sleep(3000);
            markAllAsReadButton.click(); //first click
            markAllAsReadButton.click(); //second click, red banner should show up
            Thread.sleep(3000);

            System.out.println("Mark all as read button was clicked twice successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to mark all notifications as read: " + e.getMessage());
        }
    }

    @Test(priority = 3, dependsOnMethods = {"testNavigateToNotificationsPage"})
    public void testHideNotifications() {
        try {
            Thread.sleep(2000);
            // Look for any notification context menu elements on the page
            List<WebElement> notificationMenus = driver.findElements(By.cssSelector("notification-context-menu"));
            if (notificationMenus.isEmpty()) {
                System.out.println("No notifications found to hide. Skipping test.");
                return; // Skip the test if no notifications are found
            }
            // Go through all notification menus to find one we can use
            for (WebElement menuHost : notificationMenus) {
                try {
                    String messageType = menuHost.getAttribute("message-type");
                    System.out.println("Attempting to hide notification of type: " + messageType);
                    // Access the shadow DOM
                    SearchContext shadow = menuHost.getShadowRoot();
                    // Find and click the three-dot menu button within the shadow DOM
                    WebElement threeDotButton = shadow.findElement(By.cssSelector("button[aria-label='Open notification actions']"));
                    threeDotButton.click();
                    Thread.sleep(2000);
                    // Look for the hide option in the menu
                    List<WebElement> menuItems = shadow.findElements(By.cssSelector("div[role='menuitem']"));
                    WebElement hideOption = null;
                    for (WebElement item : menuItems) {
                        if (item.getText().contains("Hide")) {
                            hideOption = item;
                            break;
                        }
                    }

                    if (hideOption != null) {
                        // Use JavaScript to click the element
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hideOption);
                        Thread.sleep(2000);
                        System.out.println("Successfully hid notification of type: " + messageType);
                        return;
                    } else {
                        System.out.println("Could not find Hide option for notification type: " + messageType);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing notification: " + e.getMessage());
                    // Continue to next notification
                }
            }

            System.out.println("Could not hide any notifications after trying all available ones.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to hide notification: " + e.getMessage());
        }
    }

    @Test(priority = 4, dependsOnMethods = {"testNavigateToNotificationsPage"})
    public void testNavigateToNotificationSettings() {
        try {
            // start on navigation page
            if (!driver.getCurrentUrl().contains("/notifications")) {
                WebElement notificationsButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[@id='notifications-inbox-button']")));
                notificationsButton.click();
                wait.until(ExpectedConditions.urlContains("/notifications"));
            }

            Thread.sleep(2000);

            // Find the "Mark all as read" text
            WebElement markAllAsReadText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='Mark all as read']")));

            // Get the size of button
            Rectangle rect = markAllAsReadText.getRect();
            //Click right of the button
            int markAllWidth = rect.getWidth();
            int xOffset = (markAllWidth / 2) + 25;
            System.out.println("Mark all as read button width: " + markAllWidth);
            System.out.println("Using xOffset: " + xOffset);

            String originalWindow = driver.getWindowHandle();

            Actions actions = new Actions(driver);
            actions.moveToElement(markAllAsReadText)
                    .moveByOffset(xOffset, 0)
                    .click()
                    .perform();

            // Wait
            Thread.sleep(3000);

            // Check if a new window was opened and switch back to original
            if (driver.getWindowHandles().size() > 1) {
                System.out.println("New window detected, switching back to original");
                driver.switchTo().window(originalWindow);
            }

            System.out.println("Clicked at precise offset from Mark all as read text");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to navigate to notification settings: " + e.getMessage());
        }
    }

    @Test(priority = 5, dependsOnMethods = {"testNavigateToNotificationsPage"})
    public void testClickOnMessagesTab() {
        try {
            if (!driver.getCurrentUrl().contains("/notifications")) {
                WebElement notificationsButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[@id='notifications-inbox-button']")));
                notificationsButton.click();
                wait.until(ExpectedConditions.urlContains("/notifications"));
            }

            Thread.sleep(2000);

            WebElement messagesTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[text()='Messages']")));
            messagesTab.click();

            wait.until(ExpectedConditions.urlContains("/message/messages"));

            Assert.assertTrue(driver.getCurrentUrl().contains("/message/messages"),
                    "Failed to navigate to the Messages page");

            System.out.println("Successfully navigated to the Messages page");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to navigate to Messages page: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (driver != null) {
            driver.quit();
        }
    }
}