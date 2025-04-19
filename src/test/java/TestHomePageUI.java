/**
 * TestHomePageUI
 * This test verifies the functionality of Reddit home page.
 * @author Angel
 * @version 1.0
 * @since 2025-04-18
 */
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class TestHomePageUI {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
        driver.get("https://www.reddit.com/");
        loginToReddit();
    }

    private void loginToReddit() {
        try {
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href,'login') or contains(@data-testid,'login')]")));
            loginButton.click();

            WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("login-username")));
            user.sendKeys("UserName");

            WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("login-password")));
            pass.sendKeys("Password");

            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='login']/auth-flow-modal/div[2]/faceplate-tracker/button")));
            submit.click();

            // wait for header to confirm login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//header")));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Login to Reddit failed: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void testSortDropdown() throws InterruptedException {
        Thread.sleep(3000);
        WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("shreddit-sort-dropdown")));
        js.executeScript(
                "arguments[0].shadowRoot.querySelector(\"button[aria-label^='Sort by:']\").click()",
                host);

        WebElement hot = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/hot/') and .//span[text()='Hot']]")));
        hot.click();
        wait.until(ExpectedConditions.urlContains("/hot/"));

        WebElement host2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("shreddit-sort-dropdown")));
        String label = (String) js.executeScript(
                "return arguments[0].shadowRoot.querySelector(\"button[aria-label^='Sort by:']\").textContent",
                host2);
        Assert.assertTrue(label.contains("Hot"),
                "Expected Sort‑by dropdown to show 'Hot', but was: " + label);
    }

    @Test(priority = 1)
    public void testViewDropdown() throws InterruptedException {
        Thread.sleep(3000);
        WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("shreddit-sort-dropdown[class*='nd:block']")));
        js.executeScript(
                "arguments[0].shadowRoot.querySelector(\"button[aria-label^='View:']\").click()",
                host);

        WebElement compact = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'feedViewType=compactView') and .//span[text()='Compact']]")));
        compact.click();
        wait.until(ExpectedConditions.urlContains("compactView"));
    }

    @Test(priority = 3)
    public void scrollHomeFeed() {
        String home = "https://www.reddit.com/?feed=home";
        if (!driver.getCurrentUrl().contains("feed=home")) {
            driver.get(home);
            wait.until(ExpectedConditions.urlToBe(home));
        }
        js.executeScript(
                "const d=5000, step=5, start=Date.now();" +
                        "(function s(){ window.scrollBy(0,step);" +
                        " if(Date.now()-start<d) requestAnimationFrame(s);" +
                        "})();"
        );
        try { Thread.sleep(5200); } catch (InterruptedException ignored) {}
    }

    @Test(priority = 4)
    public void testLoadTrendingPosts() {
        WebElement sidebar = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("left-nav-top-section")));
        js.executeScript(
                "arguments[0].shadowRoot.querySelector(\"a[href*='/r/popular']\").click()",
                sidebar);
        wait.until(ExpectedConditions.urlContains("/popular"));
    }

    @Test(priority = 5)
    public void testScrollFeed() {
        js.executeScript("window.scrollBy(0,1000)");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        js.executeScript("window.scrollTo(0, 0)");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @Test(priority = 6)
    public void testSearchSubreddits() {
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(
                        "/html/body/shreddit-app/reddit-header-large/" +
                                "reddit-header-action-items/header/nav/div[2]/div/div/" +
                                "search-dynamic-id-cache-controller/reddit-search-large")));
        search.click();
        search.sendKeys("FGCU", Keys.ENTER);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        WebElement result = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"right-sidebar-contents\"]/div/div/section[1]/search-telemetry-tracker[1]")));
        result.click();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    @Test(priority = 7)
    public void testViewPostDetails() {
        WebElement first = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-content\"]/div[2]/shreddit-feed/article[1]")));
        first.click();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    @Test(priority = 8)
    public void testSidebarVisibility() {
        js.executeScript("document.body.style.zoom='125%'");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        js.executeScript("document.body.style.zoom='100%'");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
