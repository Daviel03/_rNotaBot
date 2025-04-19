/**
 * TestRedditChatExample
 * This test verifies the functionality of Reddit chat system.
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

public class TestRedditChatExample {

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

    @Test
    public void openStartNewChatUsingXPath() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Wait for and click the Chat button.
        WebElement chatButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@id='header-action-item-chat-button']")));
        chatButton.click();
        System.out.println("Chat button clicked successfully!");

    }
    @Test(dependsOnMethods = {"openStartNewChatUsingXPath"})
    public void typeMessageAndClickSend() {
        try {
            // Wait for the chat interface to fully load
            Thread.sleep(3000);

            // Go through the nested Shadow DOMs using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Using JavaScript we navigate shadow DOM and type in the textarea
            js.executeScript(
                    "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const host4 = shadow3.querySelector('rs-message-composer');" +
                            "const shadow4 = host4.shadowRoot;" +
                            "const textarea = shadow4.querySelector('textarea[name=\"message\"]');" +
                            "textarea.value = 'anything';" +
                            "textarea.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "console.log('Message typed successfully!');"
            );

            System.out.println("Message typed successfully!");

            Thread.sleep(1000);

            // Click the send button
            js.executeScript(
                    "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const host4 = shadow3.querySelector('rs-message-composer');" +
                            "const shadow4 = host4.shadowRoot;" +
                            "const sendButton = shadow4.querySelector('button[aria-label=\"Send message\"]');" +
                            "sendButton.click();" +
                            "console.log('Send button clicked successfully!');"
            );

            System.out.println("Send button clicked successfully!");
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to type message and click send: " + e.getMessage());
        }
    }

    @Test(dependsOnMethods = {"typeMessageAndClickSend"})
    public void reactToMessage() {
        try {
            Thread.sleep(2000);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            //Find and hover over the last message
            js.executeScript(
                    "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "if (!host1 || !host1.shadowRoot) return false;" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "if (!host2 || !host2.shadowRoot) return false;" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "if (!host3 || !host3.shadowRoot) return false;" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "if (!timeline || !timeline.shadowRoot) return false;" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "if (!virtualScroll || !virtualScroll.shadowRoot) return false;" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return false;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "lastMessage.dispatchEvent(new MouseEvent('mouseenter', {bubbles: true}));" +
                            "console.log('Hovered over message');"
            );

            System.out.println("Triggered message hover");
            Thread.sleep(1500);  // Wait after hover

            // Get reaction button coordinates
            Object reactionCoords = js.executeScript(
                    "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "if (!host1 || !host1.shadowRoot) return null;" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "if (!host2 || !host2.shadowRoot) return null;" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "if (!host3 || !host3.shadowRoot) return null;" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "if (!timeline || !timeline.shadowRoot) return null;" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "if (!virtualScroll || !virtualScroll.shadowRoot) return null;" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return null;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "if (!lastMessage.shadowRoot) return null;" +
                            "const messageShadow = lastMessage.shadowRoot;" +
                            "const menuElement = messageShadow.querySelector('rs-timeline-event-menu');" +
                            "if (!menuElement || !menuElement.shadowRoot) return null;" +
                            "const menuShadow = menuElement.shadowRoot;" +
                            "const reactionAction = menuShadow.querySelector('rs-timeline-event-reaction-picker-action');" +
                            "if (!reactionAction) return null;" +
                            "const rect = reactionAction.getBoundingClientRect();" +
                            "return {x: Math.round(rect.left + rect.width/2), y: Math.round(rect.top + rect.height/2)};"
            );

            if (reactionCoords == null) {
                System.out.println("Could not get coordinates for reaction button");
                Assert.fail("Could not get coordinates for reaction button");
                return;
            }

            Map<String, Object> coords = (Map<String, Object>) reactionCoords;
            int x = ((Number) coords.get("x")).intValue();
            int y = ((Number) coords.get("y")).intValue();

            System.out.println("Reaction button coordinates: x=" + x + ", y=" + y);

            // Click at the reaction button coordinates
            Actions actions = new Actions(driver);
            actions.moveToLocation(x, y).click().perform();

            System.out.println("Clicked at reaction button coordinates");
            Thread.sleep(1500);  // Wait for emoji picker to appear

            // Step 4: Click at the emoji position
            int emojiX = x;
            int emojiY = y - 30;

            System.out.println("Clicking emoji at: x=" + emojiX + ", y=" + emojiY);
            actions.moveToLocation(emojiX, emojiY).click().perform();
            Thread.sleep(2000);

            System.out.println("Reaction test completed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to react to message: " + e.getMessage());
        }
    }
    @Test(dependsOnMethods = {"reactToMessage"})
    public void copyMessageLink() {
        try {
            Thread.sleep(2000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            //find and hover over the last message
            Boolean messageHovered = (Boolean) js.executeScript(
                    "try {" +
                            "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "if (!host1 || !host1.shadowRoot) return false;" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "if (!host2 || !host2.shadowRoot) return false;" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "if (!host3 || !host3.shadowRoot) return false;" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "if (!timeline || !timeline.shadowRoot) return false;" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "if (!virtualScroll || !virtualScroll.shadowRoot) return false;" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return false;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "lastMessage.dispatchEvent(new MouseEvent('mouseenter', {bubbles: true}));" +
                            "console.log('Hovered over message');" +
                            "return true;" +
                            "} catch (e) {" +
                            "console.error('Error hovering over message:', e);" +
                            "return false;" +
                            "}"
            );

            System.out.println("Message hovered: " + messageHovered);
            Thread.sleep(1500);  // Wait for hover effect

            //getting the reaction button coordinates
            Object reactionCoords = js.executeScript(
                    "try {" +
                            "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "if (!host1 || !host1.shadowRoot) return null;" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "if (!host2 || !host2.shadowRoot) return null;" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "if (!host3 || !host3.shadowRoot) return null;" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "if (!timeline || !timeline.shadowRoot) return null;" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "if (!virtualScroll || !virtualScroll.shadowRoot) return null;" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return null;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "if (!lastMessage.shadowRoot) return null;" +
                            "const messageShadow = lastMessage.shadowRoot;" +
                            "const menuElement = messageShadow.querySelector('rs-timeline-event-menu');" +
                            "if (!menuElement || !menuElement.shadowRoot) return null;" +
                            "const menuShadow = menuElement.shadowRoot;" +
                            "const reactionAction = menuShadow.querySelector('rs-timeline-event-reaction-picker-action');" +
                            "if (!reactionAction) return null;" +
                            "const rect = reactionAction.getBoundingClientRect();" +
                            "return {x: Math.round(rect.left + rect.width/2), y: Math.round(rect.top + rect.height/2)};" +
                            "} catch (e) {" +
                            "console.error('Error getting reaction button coordinates:', e);" +
                            "return null;" +
                            "}"
            );

            if (reactionCoords == null) {
                System.out.println("Could not get coordinates for reaction button");
                Assert.fail("Could not get coordinates for reaction button");
                return;
            }

            //Get coordinates from the reaction button
            Map<String, Object> rCoords = (Map<String, Object>) reactionCoords;
            int rX = ((Number) rCoords.get("x")).intValue();
            int rY = ((Number) rCoords.get("y")).intValue();

            System.out.println("Reaction button coordinates: x=" + rX + ", y=" + rY);

            // Get the delete button coordinates
            Object deleteCoords = js.executeScript(
                    "try {" +
                            "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "if (!host1 || !host1.shadowRoot) return null;" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "if (!host2 || !host2.shadowRoot) return null;" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "if (!host3 || !host3.shadowRoot) return null;" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "if (!timeline || !timeline.shadowRoot) return null;" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "if (!virtualScroll || !virtualScroll.shadowRoot) return null;" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return null;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "if (!lastMessage.shadowRoot) return null;" +
                            "const messageShadow = lastMessage.shadowRoot;" +
                            "const menuElement = messageShadow.querySelector('rs-timeline-event-menu');" +
                            "if (!menuElement || !menuElement.shadowRoot) return null;" +
                            "const menuShadow = menuElement.shadowRoot;" +
                            "const deleteAction = menuShadow.querySelector('rs-timeline-event-delete-action');" +
                            "if (!deleteAction) return null;" +
                            "const rect = deleteAction.getBoundingClientRect();" +
                            "return {x: Math.round(rect.left + rect.width/2), y: Math.round(rect.top + rect.height/2)};" +
                            "} catch (e) {" +
                            "console.error('Error getting delete button coordinates:', e);" +
                            "return null;" +
                            "}"
            );

            // Find the position of the share button
            int shareX, shareY;
            if (deleteCoords != null) {
                // Find the position between them
                Map<String, Object> dCoords = (Map<String, Object>) deleteCoords;
                int dX = ((Number) dCoords.get("x")).intValue();
                int dY = ((Number) dCoords.get("y")).intValue();

                System.out.println("Delete button coordinates: x=" + dX + ", y=" + dY);
                shareX = rX + ((dX - rX) / 2);
                shareY = rY;
            } else {
                // we just go and click to the right of reaction button
                shareX = rX + 30;
                shareY = rY;
            }
            System.out.println("Share button estimated coordinates: x=" + shareX + ", y=" + shareY);

            //click at the calculated share button position
            Actions actions = new Actions(driver);
            actions.moveToLocation(shareX, shareY).click().perform();
            System.out.println("Clicked at estimated share button position");
            Thread.sleep(2000);
            System.out.println("Share operation completed");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to share message: " + e.getMessage());
        }
    }
    @Test(dependsOnMethods = {"reactToMessage"})
    public void deleteMessage() {
        try {
            Thread.sleep(2000);

            // Find and hover over the last message
            JavascriptExecutor js = (JavascriptExecutor) driver;

            Boolean messageHovered = (Boolean) js.executeScript(
                    "try {" +
                            "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "if (allMessages.length === 0) return false;" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "lastMessage.dispatchEvent(new MouseEvent('mouseenter', {bubbles: true}));" +
                            "console.log('Hovered over message');" +
                            "return true;" +
                            "} catch (e) {" +
                            "console.error('Error hovering over message:', e);" +
                            "return false;" +
                            "}"
            );

            System.out.println("Message hovered: " + messageHovered);
            Thread.sleep(1000);  // Wait for hover effect

            //Find and click the delete button
            Object deleteButtonCoords = js.executeScript(
                    "try {" +
                            "const host1 = document.querySelector('body > shreddit-app:nth-child(3) > div:nth-child(30)');" +
                            "const shadow1 = host1.shadowRoot;" +
                            "const host2 = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                            "const shadow2 = host2.shadowRoot;" +
                            "const host3 = shadow2.querySelector('rs-room');" +
                            "const shadow3 = host3.shadowRoot;" +
                            "const timeline = shadow3.querySelector('rs-timeline');" +
                            "const timelineShadow = timeline.shadowRoot;" +
                            "const virtualScroll = timelineShadow.querySelector('rs-virtual-scroll-dynamic');" +
                            "const virtualScrollShadow = virtualScroll.shadowRoot;" +
                            "const allMessages = virtualScrollShadow.querySelectorAll('rs-timeline-event');" +
                            "const lastMessage = allMessages[allMessages.length - 1];" +
                            "const messageShadow = lastMessage.shadowRoot;" +
                            "const menuElement = messageShadow.querySelector('rs-timeline-event-menu');" +
                            "if (!menuElement || !menuElement.shadowRoot) return null;" +
                            "const menuShadow = menuElement.shadowRoot;" +
                            "const deleteAction = menuShadow.querySelector('rs-timeline-event-delete-action');" +
                            "if (!deleteAction) return null;" +
                            "const rect = deleteAction.getBoundingClientRect();" +
                            "return {x: Math.round(rect.left + rect.width/2), y: Math.round(rect.top + rect.height/2)};" +
                            "} catch (e) {" +
                            "console.error('Error getting delete button coordinates:', e);" +
                            "return null;" +
                            "}"
            );

            if (deleteButtonCoords == null) {
                System.out.println("Could not get coordinates for delete button, trying alternative approach");

                // Try a more dynamic approach to find the button
                Boolean deleteButtonClicked = (Boolean) js.executeScript(
                        "try {" +
                                "// Find all shreddit-app divs and try each one until we find what we need" +
                                "const divs = document.querySelectorAll('body > shreddit-app > div');" +
                                "for (const div of divs) {" +
                                "  try {" +
                                "    const shadow1 = div.shadowRoot;" +
                                "    if (!shadow1) continue;" +
                                "    const app = shadow1.querySelector('rs-app[class=\"w-full h-full\"]');" +
                                "    if (!app) continue;" +
                                "    const shadow2 = app.shadowRoot;" +
                                "    const room = shadow2.querySelector('rs-room');" +
                                "    if (!room) continue;" +
                                "    const shadow3 = room.shadowRoot;" +
                                "    const timeline = shadow3.querySelector('rs-timeline');" +
                                "    if (!timeline) continue;" +
                                "    const shadow4 = timeline.shadowRoot;" +
                                "    const virtualScroll = shadow4.querySelector('rs-virtual-scroll-dynamic');" +
                                "    if (!virtualScroll) continue;" +
                                "    const shadow5 = virtualScroll.shadowRoot;" +
                                "    const events = shadow5.querySelectorAll('rs-timeline-event');" +
                                "    if (events.length === 0) continue;" +
                                "    const lastEvent = events[events.length - 1];" +
                                "    lastEvent.dispatchEvent(new MouseEvent('mouseenter', {bubbles: true}));" +
                                "    console.log('Hovered over message in fallback');" +
                                "    const shadow6 = lastEvent.shadowRoot;" +
                                "    const menu = shadow6.querySelector('rs-timeline-event-menu');" +
                                "    if (!menu) continue;" +
                                "    const shadow7 = menu.shadowRoot;" +
                                "    const deleteAction = shadow7.querySelector('rs-timeline-event-delete-action');" +
                                "    if (!deleteAction) continue;" +
                                "    deleteAction.click();" +
                                "    console.log('Delete action clicked directly');" +
                                "    return true;" +
                                "  } catch (innerError) {" +
                                "    console.error('Error in this div:', innerError);" +
                                "    continue;" +
                                "  }" +
                                "}" +
                                "return false;" +
                                "} catch (e) {" +
                                "console.error('Error in fallback delete approach:', e);" +
                                "return false;" +
                                "}"
                );

                if (deleteButtonClicked) {
                    System.out.println("Delete button clicked via alternative approach");
                    // Handle the confirmation dialog
                    handleDeleteConfirmDialog();
                    return;
                }

                Assert.fail("Could not locate delete button with any approach");
                return;
            }


            Map<String, Object> coords = (Map<String, Object>) deleteButtonCoords;
            int x = ((Number) coords.get("x")).intValue();
            int y = ((Number) coords.get("y")).intValue();

            System.out.println("Delete button coordinates: x=" + x + ", y=" + y);

            // Click at the delete button coordinates
            Actions actions = new Actions(driver);
            actions.moveToLocation(x, y).click().perform();

            System.out.println("Clicked at delete button coordinates");
            handleDeleteConfirmDialog();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to delete message: " + e.getMessage());
        }
    }

    private void handleDeleteConfirmDialog() {
        try {
            Thread.sleep(2000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            try {
                System.out.println("Keyboard navigation with two Tab presses");
                Actions actions = new Actions(driver);
                actions.sendKeys(Keys.TAB).perform();
                Thread.sleep(500);
                actions.sendKeys(Keys.ENTER).perform();
                System.out.println("Sent two Tab presses and Enter");
                Thread.sleep(1000);
                return;
            } catch (Exception e) {
                System.out.println("Error in tab sequence: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error in handleDeleteConfirmDialog: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @AfterClass
    public void tearDown() {
        try {
            // Wait 10 seconds so you can observe the browser state before it closes.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
