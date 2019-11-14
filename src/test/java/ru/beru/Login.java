package ru.beru;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Login extends WebDriverInit {
    private final String A_AUTH = "[data-apiary-widget-name='@marketplace/Auth'] a";
    private final String SPAN_AUTH = "[data-apiary-widget-name='@marketplace/Auth'] button span";

    private final String INPUT_LOGIN = "input[name='login']";
    private final String INPUT_PASSWORD = "input[name='passwd']";
    private final String BUTTON_SUBMIT = "button[type='submit']";

    private final String USER_MENU = "[data-apiary-widget-name='@marketplace/desktop/UserMenu']";
    private final String USER_NAME = "[data-apiary-widget-name='@marketplace/desktop/UserMenu'] span";

    @Test(testName = "auth", description = "Authorization user.", priority = 0)
    @Parameters({"email", "password"})
    private void auth(String email, String password) throws Exception {
        try {
            //driver.findElementByCssSelector(A_AUTH).click();
            //driver.findElementByCssSelector(INPUT_LOGIN).sendKeys(email);
            //driver.findElementByCssSelector(BUTTON_SUBMIT).click();
            //driver.findElementByCssSelector(INPUT_PASSWORD).sendKeys(password);
            //driver.findElementByCssSelector(BUTTON_SUBMIT).click();

            click(driver.findElementByCssSelector(A_AUTH));
            sendKeys(driver.findElementByCssSelector(INPUT_LOGIN), email);
            click(driver.findElementByCssSelector(BUTTON_SUBMIT));
            sendKeys(driver.findElementByCssSelector(INPUT_PASSWORD), password);
            click(driver.findElementByCssSelector(BUTTON_SUBMIT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outerInitAuth(String email, String password, ChromeDriver driver) throws Exception {
        try {
            this.driver = driver;
            auth(email, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Check change name button and set user name.", priority = 1, dependsOnMethods = "auth")
    @Parameters({"userName"})
    private void checkUserName(String userName) throws Exception {
        try {
            WebElement button = driver.findElementByCssSelector(SPAN_AUTH);

            //Assert.assertEquals(button.getText(), "Мой профиль");
            //button.click();

            Assert.assertEquals(getText(button), "Мой профиль");
            click(button);

            (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(USER_MENU)));

            WebElement userNameElement = driver.findElementByCssSelector(USER_NAME);

            //Assert.assertEquals(userNameElement.getText(), userName);
            Assert.assertEquals(getText(userNameElement), userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
