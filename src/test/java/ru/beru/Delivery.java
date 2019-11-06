package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Delivery extends WebDriverInit {
    private final String SPAN_REGION_MODAL_OPENER = "span[data-auto='region-form-opener']";
    private final String INPUT_REGION_NAME = "[data-apiary-widget-name='@marketplace/RegionSelect'] input";
    private final String LIST_REGION = "#react-autowhatever-region";
    private final String LI_FIRST_ELEMENT_REGION = "li:first-child";
    private final String REGION_MODAL = "[data-auto='region-popup']";
    private final String BUTTONS_MODAL = "button:not([data-auto='select-button'])";
    private final String BUTTON_LAST_MODAL = "div:last-child > button";

    private final String SPAN_REGION_NAME = "span[data-auto='region-form-opener'] span[data-auto='region-form-opener']";
    private final String SPAN_AUTH = "[data-apiary-widget-name='@marketplace/Auth'] span";
    private final String USER_MENU = "[data-apiary-widget-name='@marketplace/desktop/UserMenu']";
    private final String LI_LAST_A = "li:last-child a";
    private final String USER_SETTING_REGION_NAME = "#region [data-auto='region']";

    public void ChangeRegion(String regionName) {
        try {
            driver.findElementByCssSelector(SPAN_REGION_MODAL_OPENER).click();
            WebElement inputCity = driver.findElementByCssSelector(INPUT_REGION_NAME);
            inputCity.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            inputCity.sendKeys(regionName);

            WebElement listBoxRegion = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(LIST_REGION)));
            listBoxRegion.findElement(By.cssSelector(LI_FIRST_ELEMENT_REGION)).click();

            WebElement modal = driver.findElementByCssSelector(REGION_MODAL);
            if (modal.findElements(By.cssSelector(BUTTONS_MODAL)).size() > 1) {
                modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();
            } else {
                modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();
                modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();
            }

            (new WebDriverWait(driver, 30)).until(ExpectedConditions.invisibilityOf(modal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Parameters({"region", "email", "password"})
    public void EqualsRegionAfterChange(String region, String email, String password) {
        try {
            ChangeRegion(region);

            WebElement city = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(SPAN_REGION_NAME)));
            Assert.assertEquals(city.getText(), region);

            Login login = new Login();
            login.auth(driver, email, password);

            WebElement button = driver.findElementByCssSelector(SPAN_AUTH);
            button.click();

            WebElement userMenu = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(USER_MENU)));

            userMenu.findElements(By.cssSelector("ul")).get(2).findElement(By.cssSelector(LI_LAST_A)).click();

            city = driver.findElementByCssSelector(SPAN_REGION_NAME);
            WebElement citySetting = driver.findElementByCssSelector(USER_SETTING_REGION_NAME);
            Assert.assertEquals(city.getText(), citySetting.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
