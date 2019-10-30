package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delivery extends Login {
    @Test
    public void ChangeCityToDelivery()
    {
        try
        {
            driver.findElementByCssSelector("span[data-auto='region-form-opener']").click();
            WebElement inputCity = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/RegionSelect'] input");
            inputCity.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            inputCity.sendKeys(dotenv.get("city"));

            driver.findElementByCssSelector("#react-autowhatever-region li:first-child").click();

            // for close modal if not delivery enter city
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            while(driver.findElementsByCssSelector("[data-auto='region-popup']").size() > 0)
            {
                List<WebElement> buttons = driver.findElementsByCssSelector("[data-auto='region-popup'] button");
                buttons.get(buttons.size() - 1).click();
            }
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


            WebElement city = driver.findElementByCssSelector("span[data-auto='region-form-opener'] span[data-auto='region-form-opener']");
            Assert.assertEquals(city.getAttribute("innerHTML"), dotenv.get("city"));

            auth();

            WebElement button = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/Auth'] span");
            button.click();
            Thread.sleep(1000); //for show menu

            driver.findElementsByCssSelector("[data-apiary-widget-name='@marketplace/desktop/UserMenu'] ul").get(2).findElement(By.cssSelector("li:last-child a")).click();

            city = driver.findElementByCssSelector("span[data-auto='region-form-opener'] span[data-auto='region-form-opener']");
            WebElement region = driver.findElementByCssSelector("#region [data-auto='region']");
            Assert.assertEquals(city.getAttribute("innerHTML"), region.getAttribute("innerHTML"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
