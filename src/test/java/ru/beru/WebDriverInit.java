package ru.beru;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public class WebDriverInit {
    private final String PROPERTY_NAME_DRIVER = "webdriver.chrome.driver";
    private final String PATH_TO_DRIVER = "/chromedriver.exe";
    private final String SITE_URL = "https://beru.ru/";

    protected ChromeDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty(PROPERTY_NAME_DRIVER, System.getProperty("user.dir") + PATH_TO_DRIVER);
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(SITE_URL);
    }

    @AfterClass
    public void SetDown() {
        driver.quit();
    }
}
