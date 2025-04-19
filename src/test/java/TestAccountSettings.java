/**
 * TestAccountSettings
 * This test verifies the functionality of Reddit account settings.
 * @author Tajae
 * @version 1.0
 * @since 2025-04-18
 */
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestAccountSettings {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void setUp() throws Exception {
        // Initialize the Chrome driver.
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver; // This line was missing in your code

        driver.manage().window().maximize();
        driver.get("https://www.reddit.com/");

        // Wait for the login button to be clickable and click it.
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, 'login') or contains(@data-testid, 'login')]")));
        loginButton.click();

        // Wait for the username input field to become visible and enter your username.
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-username")));
        usernameField.sendKeys("UserName");

        // Wait for the password input field to become visible and enter your password.
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-password")));
        passwordField.sendKeys("Password");

        // Wait for the submit button to be clickable and then click it.
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"login\"]/auth-flow-modal/div[2]/faceplate-tracker/button")));
        submitButton.click();
        Thread.sleep(3000); // Increased wait time for login to complete
    }

    // Helper method to navigate to settings page
    private void navigateToSettings() throws InterruptedException {
        // First navigate back to Reddit homepage to ensure consistent starting point
        driver.get("https://www.reddit.com/");
        Thread.sleep(2000);

        WebElement top_right_profile = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"expand-user-drawer-button\"]/span/span/div/faceplate-partial/span/span")));
        top_right_profile.click();
        Thread.sleep(2000);

        WebElement settings_button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"user-drawer-content\"]/ul[3]/faceplate-tracker/li")
        ));
        settings_button.click();
        Thread.sleep(2000);
    }

    @Test(priority = 1)
    public void settingsTab() throws InterruptedException {
        navigateToSettings();

        WebElement profile_button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[2]")));
        profile_button.click();
        Thread.sleep(2500);

        WebElement privacy_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[3]")));
        privacy_tab.click();
        Thread.sleep(2500);

        WebElement preferences_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[4]")));
        preferences_tab.click();
        Thread.sleep(2500);

        WebElement notifications_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[5]")));
        notifications_tab.click();
        Thread.sleep(2500);

        WebElement email_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[6]")));
        email_tab.click();
        Thread.sleep(2500);
    }

    @Test(priority = 2)
    public void underEmailTab() throws InterruptedException {
        navigateToSettings();

        WebElement email_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[6]")));
        email_tab.click();
        Thread.sleep(2500);

        String pm_button_string = "return document.querySelector(\"#main-content > div > settings-emails\").shadowRoot." +
                "querySelector(\"div:nth-child(1) > label:nth-child(2) > div > span.flex.items-center.shrink-0 > span > " +
                "faceplate-switch-input\")";
        WebElement pm_button = (WebElement) js.executeScript(pm_button_string);
        pm_button.click();
        Thread.sleep(2500);

        String chatreq_string = "return document.querySelector(\"#main-content > div > settings-emails\")" +
                ".shadowRoot.querySelector(\"div:nth-child(1) > label:nth-child(3) > div > span.flex.items-center." +
                "shrink-0 > span > faceplate-switch-input\").shadowRoot.querySelector(\"span\")";
        WebElement chatreq = (WebElement) js.executeScript(chatreq_string);
        chatreq.click();
        Thread.sleep(2500);

        String newuserwel_string = "return document.querySelector(\"#main-content > div > settings-emails\")." +
                "shadowRoot.querySelector(\"div:nth-child(2) > label:nth-child(2) > div > span.flex.items-center.shrink" +
                "-0 > span > faceplate-switch-input\").shadowRoot.querySelector(\"span\")";

        WebElement newuserwel = (WebElement) js.executeScript(newuserwel_string);
        newuserwel.click();
        Thread.sleep(2500);
    }

    @Test(priority = 3)
    public void profileTab() throws InterruptedException {
        navigateToSettings();

        WebElement profile_button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[2]")));
        profile_button.click();
        Thread.sleep(2500);

        String displaynab_string = "return document.querySelector(\"#main-content > div > settings-profile-section\").shadowRoot" +
                ".querySelector(\"div > div:nth-child(2) > label > div\")";
        WebElement displaynab = (WebElement) js.executeScript(displaynab_string);
        displaynab.click();
        Thread.sleep(1500);

        String displayna_text_string = "return document.querySelector(\"#display-name > rpl-modal-card > " +
                "faceplate-text-input\").shadowRoot.querySelector(\"label\")";
        WebElement displayna_text = (WebElement) js.executeScript(displayna_text_string);
        displayna_text.sendKeys("intelliJ");
        Thread.sleep(2500);

        WebElement disSubmitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"display-name\"]/rpl-modal-card/button[1]")));
        disSubmitButton.click();
        Thread.sleep(1500);
    }

    @Test(priority = 5)
    public void privacyTab() throws InterruptedException {
        navigateToSettings();

        WebElement privacy_tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"settings-tabgroup\"]/a[3]")));
        privacy_tab.click();
        Thread.sleep(2500);

        String thirdparty_app_string = "return document.querySelector(\"#main-content > div > settings-privacy-section\")" +
                ".shadowRoot.querySelector(\"div > a > div > span.flex.items-center.shrink-0 > span\")";
        WebElement thirdparty_app = (WebElement) js.executeScript(thirdparty_app_string);
        thirdparty_app.click();
        Thread.sleep(1500);
    }
    @Test(priority = 4)
    public void addBotGender() throws InterruptedException {

        driver.get("https://www.reddit.com/user/yaimaburner/");

        // Open the user options menu.
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='expand-user-drawer-button']/span/span/div/faceplate-partial/span/span/img")));
        profileMenu.click();

        WebElement settings = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='user-drawer-content']/ul[3]/faceplate-tracker/li/a/span[1]/span[2]/span[1]")));
        settings.click();

        WebElement gender = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='main-content']/div/settings-account-section/div[4]/label/div/span[2]/span/span")));
        gender.click();
        Thread.sleep(2000); // wait for menu to render


        WebElement referAs = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='gender']/rpl-modal-card/div[2]/faceplate-menu/li[5]/div")));
        referAs.click();
        Thread.sleep(2000); // wait for menu to render


        WebElement botTyped = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='gender']/rpl-modal-card/div[2]/faceplate-text-input")));
        botTyped.click();
        botTyped.sendKeys("Bot");
        Thread.sleep(3000); // wait for menu to render

    }

    @AfterClass
    void closeBrowser(){
        if (driver != null) {
            driver.quit();
        }
    }
}