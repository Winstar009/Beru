package ru.beru;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public class WebDriverInit {
    protected ChromeDriver driver;
    protected Dotenv dotenv;

    @BeforeClass
    public void setUp()
    {
        dotenv = Dotenv.load();

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://beru.ru/");
    }

    @AfterClass
    public void SetDown()
    {
        driver.quit();
    }
}
