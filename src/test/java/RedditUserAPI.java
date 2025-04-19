/**
 * RedditUserAPI
 * This test verifies the functionality of Reddit user interactions.
 * @author Hector
 * @version 1.0
 * @since 2025-04-18
 */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class RedditUserAPI {
    private WebDriver driver;
    private WebDriverWait wait;
    private boolean isLoggedIn = false;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.reddit.com/");
        // Log in once at the beginning
        login();
    }
    @Test
    private void login() {
        if (isLoggedIn) {
            return;
        }
        // Click the login button.
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, 'login') or contains(@data-testid, 'login')]")));
        loginButton.click();

        // Enter username.
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-username")));
        usernameField.sendKeys("UserName");

        // Enter password.
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-password")));
        passwordField.sendKeys("Password");

        // Click the submit button.
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='login']/auth-flow-modal/div[2]/faceplate-tracker/button")));
        submitButton.click();

        try {
            Thread.sleep(3000); // waits for 3000 milliseconds, i.e., 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isLoggedIn = true;
    }

    @Test
    public void testLogout() {
        // Open the user options menu.
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"expand-user-drawer-button\"]/span/span/div/faceplate-partial/span/span/img")));
        profileMenu.click();

        // Click the logout option.
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='logout-list-item']/div")));
        logoutButton.click();

        // Validate logout by verifying the login button is visible again.
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href, 'login') or contains(@data-testid, 'login')]")));
        assert loginButton.isDisplayed();

        // Since we logged out, we need to log back in for other tests
        isLoggedIn = false;
        login();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserProfile() {
        // Open the user options menu.
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"expand-user-drawer-button\"]/span/span/div/faceplate-partial/span/span/img")));
        // Optionally click the profile menu if needed:
        profileMenu.click();

        // Click on the user profile link.
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"user-drawer-content\"]/ul[1]/faceplate-tracker[1]/li/a/span[1]/span[2]/span[1]")));
        profileLink.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBlockUser() {
        // Navigate to the profile of the user to block.
        driver.get("https://www.reddit.com/user/BlockMembers");

        // Click the three buttons for drop down.
        WebElement blockButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"right-sidebar-contents\"]/aside[2]/faceplate-tracker/div/div[1]/div/faceplate-dropdown-menu/faceplate-tracker/button")));
        blockButton.click();

        // Block account.
        WebElement confirmBlock = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"right-sidebar-contents\"]/aside[2]/faceplate-tracker/div/div[1]/div/faceplate-dropdown-menu/faceplate-menu/faceplate-tracker[3]/shreddit-async-loader/user-block-wrapper/user-blocking/li/div/span[1]/span[2]/span[1]")));
        confirmBlock.click();

        // Wait 3 seconds to show that the block action has been performed.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open the user options menu.
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='expand-user-drawer-button']/span/span/div/faceplate-partial/span/span/img")));
        profileMenu.click();

        // Click on the Settings link.
        WebElement settingsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='user-drawer-content']//a[contains(@href, 'settings')]")));
        settingsLink.click();

        // Click on the Privacy tab.
        WebElement privacyTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='settings-tabgroup']/a[3]")));
        privacyTab.click();

        // Click on the Blocked Accounts section button/arrow.
        WebElement blockedAccountsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-content\"]/div/settings-privacy-section")));
        blockedAccountsButton.click();

        // Locate the Blockme entry and remove it.
        WebElement removeBlockedUser = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(., 'Blockmesh')]//button[contains(text(),'Remove')]")));
        removeBlockedUser.click();

        // Click on the Save button to confirm the unblock.
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Save')]")));
        saveButton.click();

        // Wait a bit to observe the unblocking action before finishing.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFollowUser() {
        // Navigate to the target user's profile
        driver.get("https://www.reddit.com/user/Followme");

        // Click the follow button.
        WebElement followButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"right-sidebar-contents\"]/aside[2]/faceplate-tracker/div/div[2]/div/follow-button/div[2]/faceplate-tracker/button")));
        followButton.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // "Unfollow" button to reset follow.
        WebElement unfollowButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(" //*[@id=\"right-sidebar-contents\"]/aside[2]/faceplate-tracker/div/div[2]/div/follow-button/div[1]/faceplate-tracker/button/span")));
        unfollowButton.click();
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}