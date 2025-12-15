package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class DragAndDropSlidersTest {

    private WebDriver driver;
    private String buildName = "LT-Gitpod-Build-" + System.currentTimeMillis();

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws Exception {
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY environment variables.");
        }

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

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "120.0");
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("LT:Options", ltOptions);

        String hubUrl = String.format("https://%s:%s@hub.lambdatest.com/wd/hub", username, accessKey);
        this.driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (this.driver instanceof RemoteWebDriver) {
            SessionId session = ((RemoteWebDriver) this.driver).getSessionId();
            System.out.println("Session ID for test '" + method.getName() + "' : " + (session != null ? session.toString() : "null"));
        }
    }

    @Test
    public void testDragAndDropSlidersTo95() {
        // 1. Open Selenium Playground
        driver.get("https://www.lambdatest.com/selenium-playground");

        // 2. Click "Drag & Drop Sliders"
        driver.findElement(By.linkText("Drag & Drop Sliders")).click();

        // 3. Locate the "Default value 15" slider input element
        // XPath targets the slider whose wrapper has classes sp__range sp__range-success
        WebElement slider = driver.findElement(By.xpath("//*[@class='sp__range sp__range-success']/input"));

        // 4. Set slider value to 95 via JavaScript and dispatch input/change events
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input'));" +
                "arguments[0].dispatchEvent(new Event('change'));",
                slider, 95);

        // 5. Validate displayed range value shows 95 (element id="rangeSuccess")
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean textUpdated = wait.until(ExpectedConditions.textToBe(By.id("rangeSuccess"), "95"));
        Assert.assertTrue(textUpdated, "Expected range value to be '95'");

        // mark as passed on LambdaTest (best-effort)
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