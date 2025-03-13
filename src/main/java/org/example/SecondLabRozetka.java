package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SecondLabRozetka {
    private WebDriver firefoxDriver;
    private static final String baseUrl = "https://rozetka.com.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-fullscreen");
        firefoxDriver = new FirefoxDriver(options);
    }

    @BeforeMethod
    public void preconditions() {
        firefoxDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        firefoxDriver.quit();
    }

    @Test
    public void testHeaderExists() {
        WebElement header = firefoxDriver.findElement(By.id("header"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnAboutUs() {
        WebElement forAboutUsButton = firefoxDriver.findElement(By.xpath("/html/body/rz-app-root/div/div[1]/rz-main-page/div/aside/rz-main-page-sidebar/div[6]/rz-service-links/div[1]/ul/li[1]/a"));

        Assert.assertNotNull(forAboutUsButton);
        forAboutUsButton.click();

        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnAboutUsPage() {
        String aboutUsPageUrl = "pages/about/";
        firefoxDriver.get(baseUrl + aboutUsPageUrl);

        WebElement searchField = firefoxDriver.findElement(By.tagName("input"));

        Assert.assertNotNull(searchField);

        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchField.getLocation().getX(), searchField.getLocation().getY()) +
                String.format("\nSize: %d x %d", searchField.getSize().getWidth(), searchField.getSize().getHeight())
        );

        String inputValue = "I need info";
        searchField.sendKeys(inputValue);

        Assert.assertEquals(searchField.getText(), inputValue);

        searchField.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), aboutUsPageUrl);
    }

    @Test
    public void testSlider() {
        WebElement nextButton = firefoxDriver.findElement(By.className("right-btn"));

        WebElement nextButtonByCss = firefoxDriver.findElement(By.cssSelector("button.right-btn"));

        Assert.assertEquals(nextButton, nextButtonByCss);


        WebElement prevButton = firefoxDriver.findElement(By.className("left-btn"));

        for (int i = 0; i < 20; i++) {
            if(nextButton.getAttribute("class").contains("disabled")) {
                prevButton.click();
                Assert.assertTrue(prevButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            }
            else {
                nextButton.click();
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(prevButton.getAttribute("class").contains("disabled"));
            }
        }
    }
}