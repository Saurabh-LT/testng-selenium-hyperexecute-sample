import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class Test1
{
    RemoteWebDriver driver = null;
    public static String status = "passed";
    public static String username = System.getenv("LT_USERNAME");
    public static String access_key = System.getenv("LT_ACCESS_KEY");
    public static String environment = System.getenv("LT_ENV");

    String accessibilityCompliance = "https://ltqa-frontend.lambdatestinternal.com/accessibility-compliance";
    String partialAccessibilityCompliance = "https://ltqa-frontend.lambdatestinternal.com/partial-accessibility-compliance";
    String accessibilityUnCompliance = "https://ltqa-frontend.lambdatestinternal.com/accessibility-uncompliance";
    
    @BeforeMethod
    @Parameters(value={"browser","version","platform", "resolution"})
    public void testSetUp(String browser, String version, String platform, String resolution) throws Exception
    {
        String platformName = System.getenv("HYPEREXECUTE_PLATFORM") != null ? System.getenv("HYPEREXECUTE_PLATFORM") : platform;
        
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("build", "Testing Accessibility using HyperExecute");
        capabilities.setCapability("name", "A11y on Selenium");
        capabilities.setCapability("platform", platformName);
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("version", version);

        capabilities.setCapability("network",true);
        capabilities.setCapability("console",true);
        capabilities.setCapability("visual",true);
        capabilities.setCapability("selenium_version", "4.24.0");

        // accessibility related capabilities
        capabilities.setCapability("accessibility", true); // Enable accessibility testing
        capabilities.setCapability("accessibility.wcagVersion", "wcag21a"); // Specify WCAG version (e.g., WCAG 2.1 Level A)
        capabilities.setCapability("accessibility.bestPractice", true); // Exclude best practice issues from results
        capabilities.setCapability("accessibility.needsReview", true); // Include issues that need review

        try
        {
            if (environment.equalsIgnoreCase("prod"))
                driver = new RemoteWebDriver(new URL("https://" + username + ":" + access_key + "@hub.lambdatest.com/wd/hub"), capabilities);
            else
                driver = new RemoteWebDriver(new URL("https://" + username + ":" + access_key + "@stage-hub.lambdatestinternal.com/wd/hub"), capabilities);
        }
        catch (MalformedURLException e)
        {
            System.out.println("Invalid grid URL");
        }
        System.out.println("Started session");
    }

    @Test(description="Testing Accessibility using HyperExecute")
    public void test1_accessibility_testing() throws InterruptedException
    {
        driver.get(accessibilityCompliance);
        String pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
        driver.get(partialAccessibilityCompliance);
        pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
        driver.get(accessibilityUnCompliance);
        pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
    }

    @AfterMethod
    public void tearDown()
    {
        if (driver != null)
        {
            ((JavascriptExecutor) driver).executeScript("lambda-status=failed" + status);
            driver.quit();
        }
    }
}
