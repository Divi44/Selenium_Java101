package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class InputFormSubmitTest {

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
    public void testInputFormSubmitValidationAndSuccess() {
        driver.get("https://www.lambdatest.com/selenium-playground");

        // 1. Click "Input Form Submit"
        driver.findElement(By.linkText("Input Form Submit")).click();

        // 2. Click Submit without filling any info
        WebElement submitBtn = driver.findElement(By.xpath("//button[contains(text(),'Submit')]"));
        submitBtn.click();

        // 3. Assert "Please fill out this field." browser validation appears
        // This is an HTML5 validation; Selenium cannot directly read browser native popup.
        // Instead, check the 'required' attribute on first required field and that it's invalid.
        WebElement nameField = driver.findElement(By.id("name"));
        // If form is empty, nameField should be invalid when submitting - check using JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", nameField);
        Assert.assertFalse(isValid, "Expected the name field to be invalid when empty");

        // Alternatively, try to retrieve the validation message via JS (may vary by browser locale)
        String validationMessage = (String) js.executeScript("return arguments[0].validationMessage;", nameField);
        Assert.assertTrue(validationMessage != null && validationMessage.trim().length() > 0,
                "Expected a validation message like 'Please fill out this field.' but got: " + validationMessage);

        // 4. Fill in Name, Email, and other fields
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("johndoe@example.com");
        driver.findElement(By.id("password")).sendKeys("Password123!");
        driver.findElement(By.id("company")).sendKeys("Example Inc");
        driver.findElement(By.id("website")).sendKeys("https://example.com");

        // 5. Select "United States" from country dropdown using visible text
        Select countrySelect = new Select(driver.findElement(By.id("country")));
        countrySelect.selectByVisibleText("United States");

        driver.findElement(By.id("city")).sendKeys("New York");
        driver.findElement(By.id("address1")).sendKeys("123 Example St");
        driver.findElement(By.id("address2")).sendKeys("Suite 100");
        driver.findElement(By.id("state")).sendKeys("NY");
        driver.findElement(By.id("zip")).sendKeys("10001");

        // 6. Click Submit
        submitBtn.click();

        // 7. Validate success message is displayed
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Boolean successVisible = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//*[contains(text(),'Thanks for contacting us, we will get back to you shortly.')]"),
                "Thanks for contacting us, we will get back to you shortly."
        ));
        Assert.assertTrue(successVisible, "Expected the success message after submission");

        // Mark test passed
        try {
            ((RemoteWebDriver) driver).executeScript("lambda-status=passed");
        } catch (Exception ignored) {}
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}