package ru.beru;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Login extends WebDriverInit {
    @Test
    public void authorization()
    {
        WebElement widgetAuth_a =  getWebElementByCssSelector("[data-apiary-widget-name='@marketplace/Auth'] a");
        widgetAuth_a.click();

        WebElement captcha = getWebElementByCssSelector("from[action='/checkcaptcha']");
        Assert.assertNotNull(captcha, "met CAPTCHA");

        WebElement input_login = getWebElementByCssSelector("input[name='login']");
        Assert.assertNull(input_login, "missing input login");
        input_login.sendKeys(dotenv.get("email"));

        WebElement button_submit_login = getWebElementByCssSelector("button[type='submit']");
        Assert.assertNull(button_submit_login, "missing button submit");
        button_submit_login.click();

        WebElement input_password = getWebElementByCssSelector("input[name='password']");
        Assert.assertNull(input_password, "missing input password");
        input_password.sendKeys(dotenv.get("password"));

        WebElement button_submit_password = getWebElementByCssSelector("button[type='submit']");
        Assert.assertNull(button_submit_password, "missing button submit");
        button_submit_password.click();

        System.out.println(driver.location().toString());
    }

}
