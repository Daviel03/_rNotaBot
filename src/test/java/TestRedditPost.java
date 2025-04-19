/**
 * TestRedditPost
 * This test verifies the functionality of Reddit home page post interactions.
 * @author Daviel/Angel
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
import org.openqa.selenium.interactions.Actions;

public class TestRedditPost {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
            passwordField.sendKeys("Password");

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
    public void testUpvoteFirstPost() {
        try {
            Thread.sleep(3000);

            try {
                java.util.List<WebElement> posts = driver.findElements(
                        By.cssSelector("div[data-testid='post'], shreddit-post"));

                if (!posts.isEmpty()) {
                    WebElement post = posts.get(0);
                    String postId = post.getAttribute("id");

                    if (postId != null && !postId.isEmpty()) {
                        SearchContext shadow = post.getShadowRoot();
                        WebElement upvoteButton = shadow.findElement(
                                By.cssSelector("svg[fill='currentColor'][icon-name='upvote-outline']"));
                        upvoteButton.click();
                        System.out.println("Upvoted using shadow DOM approach!");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Approach failed: " + e.getMessage());
            }

            WebElement visibleUpvote = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'icon-upvote') or @data-test-id='post-upvote-button']")));
            visibleUpvote.click();
            System.out.println("Upvoted using shadow DOM approach!");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to upvote any post: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void testDownvotePost() {
        try {
            Thread.sleep(3000);
            java.util.List<WebElement> posts = driver.findElements(
                    By.cssSelector("div[data-testid='post'], shreddit-post"));

            if (posts.isEmpty()) {
                Assert.fail("No posts found on the page");
                return;
            }

            boolean success = false;
            for (WebElement post : posts) {
                try {
                    SearchContext shadow = post.getShadowRoot();
                    WebElement downvoteButton = shadow.findElement(
                            By.cssSelector("svg[fill='currentColor'][icon-name='downvote-outline']"));
                    downvoteButton.click();
                    System.out.println("Successfully downvoted post!");
                    success = true;
                    break;
                } catch (Exception e) {
                    continue;
                }
            }

            if (!success) {
                Assert.fail("Could not find any posts with downvote buttons in shadow DOM");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to downvote post: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void testShareAndCopyLink() {
        try {
            Thread.sleep(3000);
            java.util.List<WebElement> posts = driver.findElements(
                    By.cssSelector("div[data-testid='post'], shreddit-post"));

            if (posts.isEmpty()) {
                Assert.fail("No posts found on the page");
                return;
            }

            boolean shareClicked = false;
            WebElement clickedShareButton = null;
            Point shareButtonLocation = null;

            for (WebElement post : posts) {
                try {
                    SearchContext shadow0 = post.getShadowRoot();
                    WebElement shareButtonHost = shadow0.findElement(
                            By.cssSelector("shreddit-post-share-button[data-post-click-location='share']"));

                    shareButtonLocation = shareButtonHost.getLocation();
                    Dimension shareButtonSize = shareButtonHost.getSize();

                    try {
                        SearchContext shadow1 = shareButtonHost.getShadowRoot();
                        WebElement shareButton = shadow1.findElement(By.cssSelector("button[type='button']"));
                        shareButton.click();
                        clickedShareButton = shareButton;
                    } catch (Exception e1) {
                        shareButtonHost.click();
                        clickedShareButton = shareButtonHost;
                    }

                    System.out.println("Clicked share button using shadow DOM approach!");
                    shareClicked = true;
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            if (!shareClicked) {
                try {
                    WebElement visibleShareButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(., 'Share') or contains(@data-click-id, 'share')]")));

                    shareButtonLocation = visibleShareButton.getLocation();
                    Dimension shareButtonSize = visibleShareButton.getSize();

                    visibleShareButton.click();
                    clickedShareButton = visibleShareButton;
                    System.out.println("Clicked share button using fallback approach!");
                    shareClicked = true;
                } catch (Exception e) {
                    System.out.println("Clicking share button failed");
                    Assert.fail("Clicking share button failed");
                }
            }

            Thread.sleep(1000);
            if (shareClicked && shareButtonLocation != null) {
                try {
                    Actions actions = new Actions(driver);
                    int targetX = shareButtonLocation.getX() + 80;
                    int targetY = shareButtonLocation.getY() + 80;
                    actions.moveByOffset(targetX, targetY).click().perform();
                    System.out.println("Clicked at position X:" + targetX + ", Y:" + targetY + " to select Copy link!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Assert.fail("Failed to click 'Copy link' using coordinates: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to share and copy link: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void testClickCommentsButton() {
        try {
            Thread.sleep(3000);
            try {
                java.util.List<WebElement> posts = driver.findElements(
                        By.cssSelector("div[data-testid='post'], shreddit-post"));

                for (WebElement post : posts) {
                    try {
                        SearchContext shadow = post.getShadowRoot();
                        WebElement commentsButton = null;

                        try {
                            commentsButton = shadow.findElement(
                                    By.cssSelector("a[name='comments-action-button']"));
                        } catch (Exception e1) {
                            try {
                                commentsButton = shadow.findElement(
                                        By.cssSelector("a[data-click-id='comments']"));
                            } catch (Exception e2) {
                                commentsButton = shadow.findElement(
                                        By.cssSelector("a[href*='/comments/']"));
                            }
                        }

                        if (commentsButton != null) {
                            commentsButton.click();
                            System.out.println("Clicked comments button using shadow DOM approach!");
                            return;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                System.out.println("Could not find comments button in any post's shadow DOM");
            } catch (Exception e) {
                System.out.println("Approach: " + e.getMessage());
            }
            Assert.fail("Failed to click comments button");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to click comments button: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void testClickFirstPost() throws InterruptedException {
        Thread.sleep(3000);
        driver.get("https://www.reddit.com/");
        try {
            Thread.sleep(3000);
            java.util.List<WebElement> posts = driver.findElements(
                    By.cssSelector("div[data-testid='post'], shreddit-post"));

            try {
                ((JavascriptExecutor) driver).executeScript(
                        "document.querySelector('a.absolute.inset-0, a[data-click-id=\"body\"]').click()");
                System.out.println("Clicked on first post using JavaScript approach!");
                return;
            } catch (Exception e) {
                System.out.println("JavaScript approach failed: " + e.getMessage());
            }
            Assert.fail("Failed to click on first post");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to click on first post: " + e.getMessage());
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