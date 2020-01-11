package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

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

    @Test(description = "Change region in modal.", priority = 0)
    @Parameters({"region"})
    private void changeRegion(String region) throws Exception {
        try {
            //driver.findElementByCssSelector(SPAN_REGION_MODAL_OPENER).click();
            click(driver.findElementByCssSelector(SPAN_REGION_MODAL_OPENER));

            WebElement inputCity = driver.findElementByCssSelector(INPUT_REGION_NAME);

            //inputCity.sendKeys(Keys.CONTROL + "a" + Keys.DELETE);
            //inputCity.sendKeys(region);

            sendKeys(inputCity, Keys.CONTROL + "a" + Keys.DELETE);
            sendKeys(inputCity, region);

            WebElement listBoxRegion = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(LIST_REGION)));

            //listBoxRegion.findElement(By.cssSelector(LI_FIRST_ELEMENT_REGION)).click();
            click(listBoxRegion.findElement(By.cssSelector(LI_FIRST_ELEMENT_REGION)));

            WebElement modal = driver.findElementByCssSelector(REGION_MODAL);
            if (modal.findElements(By.cssSelector(BUTTONS_MODAL)).size() > 1) {
                //modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();
                click(modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)));
            } else {
                //modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();
                //modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)).click();

                click(modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)));
                click(modal.findElement(By.cssSelector(BUTTON_LAST_MODAL)));
            }

            (new WebDriverWait(driver, 30)).until(ExpectedConditions.invisibilityOf(modal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Check region in header.", priority = 1, dependsOnMethods = "changeRegion")
    @Parameters({"region"})
    private void checkRegionHeader(String region) throws Exception {
        try {
            WebElement city = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(SPAN_REGION_NAME)));

            //Assert.assertEquals(city.getText(), region);
            Assert.assertEquals(getText(city), region);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Authorization user.", priority = 2, dependsOnMethods = "checkRegionHeader")
    @Parameters({"email", "password"})
    private void auth(String email, String password) throws Exception {
        try {
            Login login = new Login();
            login.outerInitAuth(email, password, driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Go to setting.", priority = 3, dependsOnMethods = "auth")
    private void goSetting() throws Exception {
        try {
            WebElement button = driver.findElementByCssSelector(SPAN_AUTH);
            //button.click();
            click(button);

            WebElement userMenu = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(USER_MENU)));

            //userMenu.findElements(By.cssSelector("ul")).get(2).findElement(By.cssSelector(LI_LAST_A)).click();
            click(userMenu.findElements(By.cssSelector("ul")).get(3).findElement(By.cssSelector(LI_LAST_A)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Equals region in header and in setting.", priority = 4, dependsOnMethods = "goSetting")
    @Parameters({"region"})
    private void checkRegionSetting(String region) throws Exception {
        try {
            WebElement city = driver.findElementByCssSelector(SPAN_REGION_NAME);
            WebElement citySetting = driver.findElementByCssSelector(USER_SETTING_REGION_NAME);

            //Assert.assertEquals(city.getText(), citySetting.getText());
            Assert.assertEquals(getText(city), region);
            Assert.assertEquals(getText(citySetting), region);
            Assert.assertEquals(getText(city), getText(citySetting));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
