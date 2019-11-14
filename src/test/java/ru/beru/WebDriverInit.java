package ru.beru;

import io.qameta.allure.Attachment;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public class WebDriverInit extends TestListenerAdapter {
    private final String PROPERTY_NAME_DRIVER = "webdriver.chrome.driver";
    private final String PATH_TO_DRIVER = "/chromedriver.exe";
    private final String SITE_URL = "https://beru.ru/";

    private final String SCRIPT_GET_ELEMENT_BORDER =
            "var elem = arguments[0];" +
            "var style = document.defaultView.getComputedStyle(elem);\n" +
            "var border = style.getPropertyValue('border-top-width')\n" +
            "+ ' ' + style.getPropertyValue('border-top-style')\n" +
            "+ ' ' + style.getPropertyValue('border-top-color')\n" +
            "+ ';' + style.getPropertyValue('border-right-width')\n" +
            "+ ' ' + style.getPropertyValue('border-right-style')\n" +
            "+ ' ' + style.getPropertyValue('border-right-color')\n" +
            "+ ';' + style.getPropertyValue('border-bottom-width')\n" +
            "+ ' ' + style.getPropertyValue('border-bottom-style')\n" +
            "+ ' ' + style.getPropertyValue('border-bottom-color')\n" +
            "+ ';' + style.getPropertyValue('border-left-width')\n" +
            "+ ' ' + style.getPropertyValue('border-left-style')\n" +
            "+ ' ' + style.getPropertyValue('border-left-color');" +
            "elem.style.border = '2px solid red';\n" +
            "return border;\n";
    private final String SCRIPT_UNHIGHLIGHT_ELEMENT =
            "var elem = arguments[0];\n" +
            "var borders = arguments[1].split(';');\n" +
            "elem.style.borderTop = borders[0];\n" +
            "elem.style.borderRight = borders[1];\n" +
            "elem.style.borderBottom = borders[2];\n" +
            "elem.style.borderLeft = borders[3];\n";

    protected ChromeDriver driver;

    @BeforeClass(description = "Starting browser.")
    public void init() {
        System.setProperty(PROPERTY_NAME_DRIVER, System.getProperty("user.dir") + PATH_TO_DRIVER);
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(SITE_URL);
    }

    @AfterClass(description = "Close browser.")
    public void exit() {
        driver.close();
        driver.quit();
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] saveScreenshot(byte[] screenshot){
        return screenshot;
    }

    private void makeScreenShoot() {
        byte[] srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        saveScreenshot(srcFile);
    }

    private void highLightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String lastBorder = (String)(js.executeScript(SCRIPT_GET_ELEMENT_BORDER, element));
        makeScreenShoot();
        js.executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, element, lastBorder);
    }

    public void click(WebElement element) {
        highLightElement(element);
        System.out.println("click");
        element.click();
    }

    public String getText(WebElement element) {
        highLightElement(element);
        System.out.println("getText");
        return element.getText();
    }

    public void sendKeys(WebElement element, String keys) {
        highLightElement(element);
        System.out.println("sendKeys");
        element.sendKeys(keys);
    }
}
