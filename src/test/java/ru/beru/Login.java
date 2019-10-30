package ru.beru;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Login extends WebDriverInit {
    public void auth()
    {
        driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/Auth'] a").click();
        driver.findElementByCssSelector("input[name='login']").sendKeys(dotenv.get("email"));
        driver.findElementByCssSelector("button[type='submit']").click();
        driver.findElementByCssSelector("input[name='passwd']").sendKeys(dotenv.get("password"));
        driver.findElementByCssSelector("button[type='submit']").click();

    }

    @Test
    public void authorization()
    {
        try
        {
            auth();

            WebElement button = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/Auth'] span");
            button.click();
            Thread.sleep(1000); //for show menu
            Assert.assertEquals(button.getAttribute("innerHTML"), "Мой профиль");

            WebElement userName = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/desktop/UserMenu'] span");
            Assert.assertEquals(userName.getAttribute("innerHTML"), dotenv.get("user_name"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
