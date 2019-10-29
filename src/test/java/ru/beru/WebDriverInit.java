package ru.beru;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class WebDriverInit {
    protected ChromeDriver driver;
    protected Dotenv dotenv;

    protected WebElement getWebElementByCssSelector(String selector)
    {
        try
        {
            return driver.findElementByCssSelector(selector);
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }

    @BeforeClass
    public void setUp()
    {
        dotenv = Dotenv.load();

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://beru.ru/");
    }

    @AfterClass
    public void SetDown()
    {
        driver.quit();
    }
}
