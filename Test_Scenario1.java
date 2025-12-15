package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class SimpleFormDemoTest {

    private WebDriver driver;
    private String buildName = "LT-Gitpod-Build-" + System.currentTimeMillis();

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws Exception {
        // Read LambdaTest credentials from environment variables
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY environment variables.");
        }

        // LT options
        MutableCapabilities ltOptions = new MutableCapabilities();
        ltOptions.setCapability("username", username);
        ltOptions.setCapability("accessKey", accessKey);
        ltOptions.setCapability("build", buildName);
        ltOptions.setCapability("name", method.getName());
        ltOptions.setCapability("w3c", true);
        ltOptions.setCapability("video", true);
        ltOptions.setCapability("network", true);
        ltOptions.setCapability("console", true);
        ltOptions.setCapability("visual", true);

        // Top-level capabilities (W3C)
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "120.0");
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("LT:Options", ltOptions);

        // LambdaTest hub URL (basic auth)
        String hubUrl = String.format("https://%s:%s@hub.lambdatest.com/wd/hub", username, accessKey);

        this.driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Print the session id (LambdaTest Test ID)
        if (this.driver instanceof RemoteWebDriver) {
            SessionId session = ((RemoteWebDriver) this.driver).getSessionId();
            System.out.println("Session ID for test '" + method.getName() + "' : " + (session != null ? session.toString() : "null"));
        }
    }

    @Test
    public void testSimpleFormDemo() {
        // 1. Open Selenium Playground
        driver.get("https://www.lambdatest.com/selenium-playground");

        // 2. Click "Simple Form Demo"
        // Use link text (page uses a link labelled "Simple Form Demo")
        driver.findElement(By.linkText("Simple Form Demo")).click();

        // 3. Validate URL contains "simple-form-demo"
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.toLowerCase().contains("simple-form-demo"),
                "URL should contain 'simple-form-demo' but was: " + currentUrl);

        // 4. Create a variable for the message
        String message = "Welcome to LambdaTest";

        // 5. Enter value in "Enter Message" text box (id="user-message")
        driver.findElement(By.id("user-message")).clear();
        driver.findElement(By.id("user-message")).sendKeys(message);

        // 6. Click "Get Checked Value" (id="showInput")
        driver.findElement(By.id("showInput")).click();

        // 7. Validate the same message is displayed under "Your Message:" (id="message")
        String displayed = driver.findElement(By.id("message")).getText().trim();
        Assert.assertEquals(displayed, message, "Displayed message should match the entered message");

        // Mark test as passed in LambdaTest (best-effort)
        try {
            ((RemoteWebDriver) driver).executeScript("lambda-status=passed");
        } catch (Exception ignored) {}
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}