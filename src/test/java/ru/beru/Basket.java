package ru.beru;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.List;

public class Basket extends WebDriverInit {
    public void goCatalogSection(ChromeDriver driver, String section){
        try{
            driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/SearchForm'] input[type='search']").sendKeys(section);
            driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/SearchForm'] button[type='submit']").click();

            WebElement breadcrumbs = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/Breadcrumbs'], [data-widget-name='Breadcrumbs']")));
            breadcrumbs.findElement(By.cssSelector("div:last-child>div>a")).click();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFilter(ChromeDriver driver, Dotenv dotenv) {
        List<WebElement> price = driver.findElementsByCssSelector("[data-apiary-widget-name='@marketplace/SearchFilters'] input, [data-zone-name='searchPage'] input");
        price.get(0).sendKeys(dotenv.get("price_from"));
        price.get(1).sendKeys(dotenv.get("price_to"));

        /*
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-auto='tooltip__content']")));
        */
/*
        WebElement preloader = (new WebDriverWait(driver, 10000))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/SearchSerp'] div>span::after," +
                        "spin2::after")));
*/
/*
        WebElement preloader = (new WebDriverWait(driver, 10000))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/SearchSerp'] div>span::after," +
                        ".spin2::after, " +
                        "preloadable__preloader")));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.invisibilityOf(preloader));
*/
        /*
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("[data-apiary-widget-name='@marketplace/SearchSerp']")));
        */
        /*wait to apply filters*/
    }

    public void goSectionLastPage(ChromeDriver driver) {
        try{
            JavascriptExecutor js = (JavascriptExecutor) driver;

            List<WebElement> buttonNextPage = driver.findElementsByCssSelector("[data-auto='pagination-next']");
            do {
                js.executeScript("arguments[0].scrollIntoView();", buttonNextPage);
                buttonNextPage.get(0).click();
                buttonNextPage = (new WebDriverWait(driver, 5))
                        .until(ExpectedConditions.visibilityOfAllElements(driver.findElementsByCssSelector("[data-auto='pagination-next']")));
            }
            while (buttonNextPage.size() != 0);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void CheckBasket() {
        try {
            goCatalogSection(driver, dotenv.get("section"));
            setFilter(driver, dotenv);
            goSectionLastPage(driver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
