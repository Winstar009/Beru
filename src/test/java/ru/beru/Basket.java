package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class Basket extends WebDriverInit {
    private final String INPUT_SEARCH = "[data-apiary-widget-name='@marketplace/SearchForm'] input[type='search']";
    private final String BUTTON_SEARCH = "[data-apiary-widget-name='@marketplace/SearchForm'] button[type='submit']";
    private final String BREADCRUMBS = "[data-apiary-widget-name='@marketplace/Breadcrumbs'], [data-widget-name='Breadcrumbs']";
    private final String A_LAST_BREADCRUMBS = "div:last-child>div>a";

    private final String INPUTS_FILTER = "[data-apiary-widget-name='@marketplace/SearchFilters'] input, [data-zone-name='filters'] input";
    private final String SEARCH_RESULT = "[data-apiary-widget-name='@marketplace/SearchSerp'], .n-filter-applied-results";

    private final String PRELOADER_MASK = "[data-apiary-widget-name='@marketplace/SearchSerp'] div>div>div>div>div>div>div>div>div>span, .preloadable__preloader>.spin2";

    private final String BUTTON_NEXT_PAGE = "[data-auto='pagination-next'], .n-pager__button-next";

    private final String SPAN_PRICE = "[data-zone-name='SearchSerp'] span>span:first-child>span:first-child, .n-snippet-list span>span:first-child>span:first-child";

    private final String SPAN_ITEM_PRICE = "[data-apiary-widget-name='@marketplace/SkuCart'] span>span:first-child>span:first-child";

    private final String BUTTON_ADD_TO_BASKET_IN_LIST = "[data-zone-name='SearchSerp'] button, .n-snippet-list button";
    private final String BUTTON_ADD_TO_BASKET_IN_CARD = "[data-zone-name='SearchSerp'] a>button>span, .n-snippet-list a>button>span";
    private final String A_BASKET = "[data-apiary-widget-name='@marketplace/desktop/Header']>div>div:last-child>div:first-child>div>div:last-child>a";

    private final String BUTTON_ADD_TO_BASKET = "[data-apiary-widget-name='@marketplace/SkuCart'] button";
    private final String BUTTOUN_GO_TO_BASKET = "[data-apiary-widget-name='@marketplace/SkuCartUpsale'] [data-auto='modal'] a button";

    public void goCatalogSection(ChromeDriver driver, String section) {
        try {
            driver.findElementByCssSelector(INPUT_SEARCH).sendKeys(section);
            driver.findElementByCssSelector(BUTTON_SEARCH).click();

            WebElement breadcrumbs = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BREADCRUMBS)));
            breadcrumbs.findElement(By.cssSelector(A_LAST_BREADCRUMBS)).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitToPreloader() {
        try {
            WebElement preloader = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(PRELOADER_MASK)));

            (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.invisibilityOf(preloader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilter(ChromeDriver driver, String price_From, String price_To) {
        try {
            List<WebElement> price = driver.findElementsByCssSelector(INPUTS_FILTER);
            price.get(0).sendKeys(price_From);
            price.get(1).sendKeys(price_To);

            waitToPreloader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPrice(String price_From, String price_To, List<WebElement> prices) {
        Integer price_a = Integer.parseInt(price_From);
        Integer price_b = Integer.parseInt(price_To);

        for (Integer i = 0; i < prices.size(); i++) {
            Integer p = Integer.parseInt(prices.get(i).getText().replaceAll(" ", ""));
            Assert.assertTrue(price_a <= p);
            Assert.assertTrue(p <= price_b);
        }
    }

    public void checkItemsPrice(ChromeDriver driver, String price_From, String price_To) {
        try {
            Actions actions = new Actions(driver);

            List<WebElement> buttonNextPage = driver.findElementsByCssSelector(BUTTON_NEXT_PAGE);
            do {
                List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);
                checkPrice(price_From, price_To, itemsPrices);

                if (buttonNextPage.size() != 0) {
                    actions.moveToElement(buttonNextPage.get(0));
                    actions.perform();

                    buttonNextPage.get(0).click();
                    waitToPreloader();

                    buttonNextPage = driver.findElementsByCssSelector(BUTTON_NEXT_PAGE);
                }
            }
            while (buttonNextPage.size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goItemDetailPage(ChromeDriver driver) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - 2));
        actions.perform();
        itemsPrices.get(itemsPrices.size() - 2).click();
    }

    public Integer priceOneItemDetail(ChromeDriver driver) {
        WebElement itemPrice = driver.findElementByCssSelector(SPAN_ITEM_PRICE);
        return Integer.parseInt(itemPrice.getText().replaceAll(" ", ""));
    }

    public void addToBasket(ChromeDriver driver) {
        driver.findElementByCssSelector(BUTTON_ADD_TO_BASKET).click();
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BUTTOUN_GO_TO_BASKET))).click();
    }

    public Integer getPriceOneItemList(ChromeDriver driver) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - 2));
        actions.perform();

        return Integer.parseInt(itemsPrices.get(itemsPrices.size() - 2).getText().replaceAll(" ", ""));
    }

    public void addItemToBasketGoBasket(ChromeDriver driver) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(BUTTON_ADD_TO_BASKET_IN_LIST);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - 2));
        actions.perform();
        itemsPrices.get(itemsPrices.size() - 2).click();

        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BUTTON_ADD_TO_BASKET_IN_CARD))).click();

        /*
        WebElement basket = driver.findElementByCssSelector(A_BASKET);
        actions.moveToElement(basket);
        actions.perform();
        basket.click();
        */
    }

    public void addMoreItem(ChromeDriver driver, Integer price) {

    }

    @Test
    public void CheckBasket() {
        Integer priceOneItem;
        try {
            goCatalogSection(driver, dotenv.get("section"));
            setFilter(driver, dotenv.get("price_from"), dotenv.get("price_to"));
            checkItemsPrice(driver, dotenv.get("price_from"), dotenv.get("price_to"));
            //goItemDetailPage(driver);
            //priceOneItemDetail = getPriceOneItem(driver);
            //addToBasket(driver);
            priceOneItem = getPriceOneItemList(driver);
            addItemToBasketGoBasket(driver);
            addMoreItem(driver, priceOneItem);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
