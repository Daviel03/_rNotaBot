/**
 * RedditPostAPI
 * This test verifies the functionality of Reddit post creation
 * @author Hector
 * @version 1.0
 * @since 2025-04-18
 */
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.openqa.selenium.interactions.Actions;

public class RedditPostAPI {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static boolean isLoggedIn = false;

    @BeforeClass
    public void setUp() {
        // Set up the Chrome driver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        // Navigate to Reddit homepage
        driver.get("https://www.reddit.com/");
        login();
    }

    public void login() {
        if (!isLoggedIn) {
            // Click the login button.
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href, 'login') or contains(@data-testid, 'login')]")));
            loginButton.click();

            // Enter username.
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-username")));
            usernameField.sendKeys("UserName");

            // Enter password.
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-password")));
            passwordField.sendKeys("PassWord");

            // Click the submit button.
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='login']/auth-flow-modal/div[2]/faceplate-tracker/button")));
            submitButton.click();

            try {
                Thread.sleep(3000); // waits for 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isLoggedIn = true;
        }
    }

    @Test
    public void subredditSubscription() {
        // Navigate directly to FGCU subreddit URL.
        driver.get("https://www.reddit.com/r/FGCU/");

        // Click the "Join" button.
        WebElement joinButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"subgrid-container\"]/div[1]/section/div/div[2]/shreddit-subreddit-header-buttons")));
        joinButton.click();

        // Pause briefly for visual verification
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Click the "Joined" button to Unsubscribe.
        WebElement joinedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"subgrid-container\"]/div[1]/section/div/div[2]/shreddit-subreddit-header-buttons")));
        joinedButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void themePreference() {
        driver.get("https://www.reddit.com/user/yaimaburner/");

        // Open the user options menu
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("expand-user-drawer-button")));
        profileMenu.click();

        // Wait for the dark‑mode toggle to be clickable
        WebElement switchMode = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("darkmode-list-item")));

        // Click the dark‑mode toggle
        switchMode.click();

        try {
            Thread.sleep(3000); // waits for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createPost() {
        // Navigate directly to user profile
        try {
            Thread.sleep(3000); // waits for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get("https://www.reddit.com/user/yaimaburner/");

        // Click the "Create Post" button.
        WebElement createPostButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-content\"]/div[3]/div/shreddit-async-loader/div/create-post-entry-point-wrapper/faceplate-tracker/a/span")));
        createPostButton.click();

        // Locate the title field on the create post page.
        WebElement titleField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='post-composer__title']/faceplate-textarea-input")));
        titleField.sendKeys("Test Post Title");

        // Locate the body field on the create post page.
        WebElement bodyField = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='post-composer_bodytext']/div")));
        bodyField.sendKeys("This is a test post created during automation testing.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Submit the post.
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"submit-post-button\"]")));
        submitButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postInsight() throws InterruptedException {
        driver.get("https://www.reddit.com/user/yaimaburner/");

        // 1) Click the "Posts" tab
        WebElement postsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='profile-feed-tabgroup']/a[2]")));
        postsTab.click();

        // 2) Open the overflow menu on the first post
        WebElement postOpen = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"t3_1k0xo5k\"]/a[1]")));
        postOpen.click();
        Thread.sleep(1000); // wait for menu to render

        WebElement moreInsight = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"t3_1k0xo5k\"]/div[5]/div/post-stats-coachmark/a")));
        moreInsight.click();
        Thread.sleep(1000); // wait for menu to render

        // scroll down by 300 pixels
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollBy(0, 300);");
        Thread.sleep(4000);
    }

    @Test
    public void sortPost() throws InterruptedException {
        driver.get("https://www.reddit.com/user/yaimaburner/");

        // 1) Click the "Posts" tab
        WebElement postsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='profile-feed-tabgroup']/a[2]")));
        postsTab.click();

        WebElement sortBy = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-content\"]/div[3]/div/shreddit-async-loader/div/div/shreddit-sort-dropdown")));
        sortBy.click();
        Thread.sleep(1000); // wait for menu to render

        WebElement clickHot = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-content\"]/div[3]/div/shreddit-async-loader/div/div/shreddit-sort-dropdown/div[3]/li[1]/a/span[1]/span/span[1]")));
        clickHot.click();
        Thread.sleep(4000); // wait for menu to render
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}