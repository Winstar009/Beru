package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delivery extends WebDriverInit {
    public void ChangeRegion(String regionName) {
        try {
            driver.findElementByCssSelector("span[data-auto='region-form-opener']").click();
            WebElement inputCity = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/RegionSelect'] input");
            inputCity.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            inputCity.sendKeys(regionName);

            WebElement listBoxRegion = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("#react-autowhatever-region")));
            listBoxRegion.findElement(By.cssSelector("li:first-child")).click();

            WebElement modal = driver.findElementByCssSelector("[data-auto='region-popup']");
            if (modal.findElements(By.cssSelector("button:not([data-auto='select-button'])")).size() > 1) {
                modal.findElement(By.cssSelector("div:last-child > button")).click();
            } else {
                modal.findElement(By.cssSelector("div:last-child > button")).click();
                modal.findElement(By.cssSelector("div:last-child > button")).click();
            }

            (new WebDriverWait(driver, 30)).until(ExpectedConditions.invisibilityOf(modal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void EqualsRegionAfterChange() {
        try {
            ChangeRegion(dotenv.get("city"));

            WebElement city = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("span[data-auto='region-form-opener'] span[data-auto='region-form-opener']")));
            Assert.assertEquals(city.getText(), dotenv.get("city"));

            Login login = new Login();
            login.auth(driver, dotenv);

            WebElement button = driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/Auth'] span");
            button.click();

            WebElement userMenu = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/desktop/UserMenu']")));

            userMenu.findElements(By.cssSelector("ul")).get(2).findElement(By.cssSelector("li:last-child a")).click();

            city = driver.findElementByCssSelector("span[data-auto='region-form-opener'] span[data-auto='region-form-opener']");
            WebElement citySetting = driver.findElementByCssSelector("#region [data-auto='region']");
            Assert.assertEquals(city.getText(), citySetting.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
