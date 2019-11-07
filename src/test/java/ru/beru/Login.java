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
    private final String SPAN_AUTH = "[data-apiary-widget-name='@marketplace/Auth'] span";

    private final String INPUT_LOGIN = "input[name='login']";
    private final String INPUT_PASSWORD = "input[name='passwd']";
    private final String BUTTON_SUBMIT = "button[type='submit']";

    private final String USER_MENU = "[data-apiary-widget-name='@marketplace/desktop/UserMenu']";
    private final String USER_NAME = "[data-apiary-widget-name='@marketplace/desktop/UserMenu'] span";

    @Test(testName = "auth", description = "Authorization user.", priority = 0)
    @Parameters({"email", "password"})
    private void auth(String email, String password) {
        driver.findElementByCssSelector(A_AUTH).click();
        driver.findElementByCssSelector(INPUT_LOGIN).sendKeys(email);
        driver.findElementByCssSelector(BUTTON_SUBMIT).click();
        driver.findElementByCssSelector(INPUT_PASSWORD).sendKeys(password);
        driver.findElementByCssSelector(BUTTON_SUBMIT).click();
    }

    public void outerInitAuth(String email, String password, ChromeDriver driver) {
        this.driver = driver;
        auth(email, password);
    }

    @Test(description = "Check change name button and set user name.", priority = 1)
    @Parameters({"userName"})
    private void checkUserName(String userName) throws Exception {
        try {
            WebElement button = driver.findElementByCssSelector(SPAN_AUTH);
            Assert.assertEquals(button.getText(), "Мой профиль");

            button.click();

            (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(USER_MENU)));

            WebElement userNameElement = driver.findElementByCssSelector(USER_NAME);
            Assert.assertEquals(userNameElement.getText(), userName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
