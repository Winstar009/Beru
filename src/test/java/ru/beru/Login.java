package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

            WebElement userMenu = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/desktop/UserMenu']")));

            Assert.assertEquals(button.getText(), "Мой профиль");

            WebElement userName = userMenu.findElement(By.cssSelector("span"));
            Assert.assertEquals(userName.getText(), dotenv.get("user_name"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
