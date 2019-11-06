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

    public void auth(ChromeDriver driver, String login, String password) {
        driver.findElementByCssSelector(A_AUTH).click();
        driver.findElementByCssSelector(INPUT_LOGIN).sendKeys(login);
        driver.findElementByCssSelector(BUTTON_SUBMIT).click();
        driver.findElementByCssSelector(INPUT_PASSWORD).sendKeys(password);
        driver.findElementByCssSelector(BUTTON_SUBMIT).click();
    }

    @Test
    @Parameters({"email", "password", "userName"})
    public void authorization(String email, String password, String userName) {
        try {
            auth(driver, email, password);

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
