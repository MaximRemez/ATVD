package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class SecondLab {
    private WebDriver firefoxDriver;
    private static final String baseUrl = "https://nmu.org.ua/";

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
    public void testClickOnForStudent() {
        WebElement forStudentButton = firefoxDriver.findElement(By.xpath("/html/body/div[1]/footer/div[1]/div/div/div[1]/div/div/nav/div/ul/li[1]/a"));

        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();

        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "https://old.nmu.org.ua/ua/content/students/";
        firefoxDriver.get(studentPageUrl);

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

        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageUrl);
    }

    @Test
    public void testSlider() {
        WebElement nextButton = firefoxDriver.findElement(By.className("swiper-button-next"));

        WebElement nextButtonByCss = firefoxDriver.findElement(By.cssSelector("div.swiper-button-next"));

        Assert.assertEquals(nextButton, nextButtonByCss);


        WebElement prevButton = firefoxDriver.findElement(By.className("swiper-button-prev"));

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