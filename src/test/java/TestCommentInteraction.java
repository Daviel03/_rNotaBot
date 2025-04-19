/**
 * TestCommentInteraction
 * This test verifies the functionality of Reddit comment interaction.
 * @author Tajae
 * @version 1.0
 * @since 2025-04-18
 */
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestCommentInteraction {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private Actions actions;

    @BeforeClass
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.get("https://www.reddit.com/");
        login();
    }

    private void login() throws InterruptedException {
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
        Thread.sleep(3000);
    }

    @Test// Helper method to navigate to the test post
    private void navigateToTestPost() throws InterruptedException {
        // Open the user options menu.
        Thread.sleep(3000);
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"expand-user-drawer-button\"]/span/span/div/faceplate-partial/span/span")));
        profileMenu.click();
        Thread.sleep(1500);

        // Click on the user profile link.
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"user-drawer-content\"]/ul[1]/faceplate-tracker[1]/li/a")));
        profileLink.click();
        Thread.sleep(2500);


        String postCommentString = "return document.querySelector(\"#t3_1k01ezv\").shadowRoot.querySelector" +
                "(\"div.shreddit-post-container.flex.gap-sm.flex-row.items-center.flex-nowrap.justify-start > a\")";
        WebElement postComment = (WebElement) js.executeScript(postCommentString);
        postComment.click();
        Thread.sleep(2500);
    }

    @Test(priority = 1)
    public void postComment() throws InterruptedException {
        navigateToTestPost();
        WebElement commentBoxClick = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"main-content\"]/shreddit-async-loader/comment-body-header" +
                        "/shreddit-async-loader/comment-composer-host/faceplate-tracker[1]")));
        commentBoxClick.click();

        WebElement commentText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"main-content\"]/shreddit-async-loader/comment-body-header/shreddit-async-" +
                        "loader/comment-composer-host/faceplate-form/shreddit-composer/div[1]/p")));
        commentText.sendKeys("Now writing a comment to post for testing :>");

        WebElement commentButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"main-content\"]/shreddit-async-loader/comment-body-header/shreddit-a" +
                        "sync-loader/comment-composer-host/faceplate-form/shreddit-composer/button[2]")));
        commentButton.click();

        Thread.sleep(4500);
    }

    @Test(priority = 2)
    public void editComment() throws InterruptedException {
        navigateToTestPost();
        Thread.sleep(2000);

        String threeDotsString = "return document.querySelector(\"#comment-tree > shreddit-comment:nth-child(2) > shreddit-comment-" +
                "action-row > shreddit-overflow-menu\").shadowRoot.querySelector(\"faceplate-dropdown-menu\")";
        WebElement threeDots = (WebElement) js.executeScript(threeDotsString);
        threeDots.click();

        String editButton = "return document.querySelector(\"#comment-tree > shreddit-comment:nth-child(2) > shreddit-comment-" +
                "action-row > shreddit-overflow-menu\").shadowRoot.querySelector(\"faceplate-dropdown-menu > faceplate-" +
                "menu > faceplate-tracker:nth-child(1)\")";
        WebElement editComment = (WebElement) js.executeScript(editButton);
        editComment.click();
        Thread.sleep(2500);

        WebElement editCommentText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"comment-tree\"]/shreddit-comment[1]/div[4]/shreddit-async-loader/" +
                        "comment-composer-host/faceplate-form/shreddit-composer/div[1]/p")));
        editCommentText.sendKeys("  Now I am editing the comment :<");

        WebElement saveEditButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"comment-tree\"]/shreddit-comment[1]/div[4]/shreddit-async-loader/" +
                        "comment-composer-host/faceplate-form/shreddit-composer/button[2]")));
        saveEditButton.click();
        Thread.sleep(2500);
    }

    @Test(priority = 3)
    public void upvoteComment() throws InterruptedException {
        navigateToTestPost();
        actions = new Actions(driver);

        // Find the first comment in the tree
        WebElement commentContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#comment-tree > shreddit-comment:nth-child(2)")));

        // Get shadow root of the comment action row
        SearchContext shadowRoot = ((WebElement) commentContainer.findElement(
                By.cssSelector("shreddit-comment-action-row"))).getShadowRoot();

        // Find and click the upvote button using CSS selector
        WebElement upvoteButton = shadowRoot.findElement(By.cssSelector(
                "div > div > div.flex.items-center.max-h-2xl.shrink > span > button:first-child"));
        upvoteButton.click();

        Thread.sleep(2000);
    }
    @Test(priority = 4)
    public void downvoteComment() throws InterruptedException {
        navigateToTestPost();
        // Wait for the page to fully load
        Thread.sleep(2000);

        // Find all comment action rows
        List<WebElement> actionRows = driver.findElements(By.cssSelector("shreddit-comment-action-row"));
        // Find the first visible one
        for (WebElement actionRow : actionRows) {
            SearchContext shadow = actionRow.getShadowRoot();

            // Try to find the downvote button using various possible selectors
            try {
                WebElement downvoteButton = shadow.findElement(By.cssSelector("button[aria-label*='downvote']"));
                downvoteButton.click();
                break;
            } catch (Exception e1) {
                WebElement downvoteButton = shadow.findElement(By.cssSelector("span > button:nth-of-type(2)"));
                downvoteButton.click();
                break;
            }
        }

        Thread.sleep(2500);
    }
    @Test (priority = 3)
    void replytoComment() throws InterruptedException {
        WebElement profileMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='expand-user-drawer-button']/span/span/div/faceplate-partial/span/span")));
        profileMenu.click();

        // Click on the user profile link.
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='user-drawer-content']/ul[1]/faceplate-tracker[1]/li/a")));
        profileLink.click();
        Thread.sleep(3500);

        WebElement postComment = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='t3_1k01ezv']")));
        postComment.click();
        Thread.sleep(2500);

        WebElement replyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='comment-tree']/shreddit-comment[3]/shreddit-comment-action-row/faceplate-tracker/button")));
        replyButton.click();
        Thread.sleep(2500);
        WebElement replyCommentText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='comment-tree']/shreddit-comment[3]/shreddit-comment-action-row/" +
                        "shreddit-async-loader/comment-composer-host/faceplate-form/shreddit-composer")
        ));
        replyCommentText.sendKeys("Now I am replying the comment :<");
        Thread.sleep(2500);
        WebElement saveReplyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='comment-tree']/shreddit-comment[3]/shreddit-comment-action-row" +
                        "/shreddit-async-loader/comment-composer-host/faceplate-form/shreddit-composer/button[2]/span")
        ));
        saveReplyButton.click();
        Thread.sleep(2500);
    }

    @AfterClass
    void closeBrowser() throws InterruptedException {
        Thread.sleep(3500);
        if (driver != null) {
            driver.quit();
        }
    }
}