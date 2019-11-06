package ru.beru;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Basket extends WebDriverInit {
    private final String INPUT_SEARCH = "[data-apiary-widget-name='@marketplace/SearchForm'] input[type='search']";
    private final String BUTTON_SEARCH = "[data-apiary-widget-name='@marketplace/SearchForm'] button[type='submit']";
    private final String BREADCRUMBS = "[data-apiary-widget-name='@marketplace/Breadcrumbs'], [data-widget-name='Breadcrumbs']";
    private final String A_LAST_BREADCRUMBS = "div:last-child>div>a";

    private final String INPUTS_FILTER = "[data-apiary-widget-name='@marketplace/SearchFilters'] input, [data-zone-name='filters'] input";

    private final String PRELOADER_MASK = "[data-apiary-widget-name='@marketplace/SearchSerp'] div>div>div>div>div>div>div>div>div>span, .preloadable__preloader>.spin2";

    private final String BUTTON_NEXT_PAGE = "[data-auto='pagination-next'], .n-pager__button-next";

    private final String SPAN_PRICE = "[data-zone-name='SearchSerp'] span>span:first-child>span:first-child, .n-snippet-list span>span:first-child>span:first-child";

    private final String SPAN_ITEM_PRICE = "[data-apiary-widget-name='@marketplace/SkuCart'] span>span:first-child>span:first-child";

    private final String BUTTON_ADD_TO_BASKET_IN_LIST = "[data-zone-name='SearchSerp'] button, .n-snippet-list button";
    private final String BUTTON_ADD_TO_BASKET_IN_CARD = "[data-zone-name='SearchSerp'] a>button>span, .n-snippet-list a>button>span";
    private final String A_BASKET = "[data-apiary-widget-name='@marketplace/desktop/Header']>div>div:last-child>div:first-child>div>div:last-child>a";

    private final String BUTTON_ADD_TO_BASKET = "[data-apiary-widget-name='@marketplace/SkuCart'] button";
    private final String BUTTOUN_GO_TO_BASKET = "[data-apiary-widget-name='@marketplace/SkuCartUpsale'] [data-auto='modal'] a button";

    private final String FREE_DELIVERY_STATUS_PRICE_UP = "[data-apiary-widget-name='@marketplace/CartDeliveryThreshold'] span>span";
    private final String FREE_DELIVERY_ACCEPTED = "[data-apiary-widget-name='@marketplace/CartDeliveryThreshold'] span>b";
    private final String B_FREE_DELIVERY_ACCEPTED_MESSAGE = "бесплатную доставку";
    private final Integer FREE_DELIVERY = 2499;

    private final String STORE_STATUS_MESSAGE = "[data-apiary-widget-name='@marketplace/CartList']>div>div>div>div>div>div>div>div>div>div>div>div>div>div>div:first-child>div>div>span>span";
    private final String STORE_STATUS_MESSAGE_NO_LIMIT = "В наличии на складе";

    private final String SPAN_TOTAL_PRICE = "[data-apiary-widget-name='@marketplace/CartTotalPrice']>div>div:last-child>span:last-child";

    private final String BUTTON_PLUS = "[data-apiary-widget-name='@marketplace/CartList'] button:last-child";
    private final String BASKET_PRELOADER_MASK = "[data-apiary-widget-name='@marketplace/CartTotalInformation']>div>div>div>div>div>div:last-child>div:not([data-apiary-widget-name='@marketplace/CartTotalPrice'])>div>div>span";
    private final String INPUT_COUNT_ITEM_BASKET = "[data-apiary-widget-name='@marketplace/CartList'] input";

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

    public void waitToPreloader(String selector) {
        try {
            WebElement preloader = (new WebDriverWait(driver, 30,50))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(selector)));

            (new WebDriverWait(driver, 30, 1500))
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

            waitToPreloader(PRELOADER_MASK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkPrice(Actions actions, String price_From, String price_To, List<WebElement> prices) {
        Integer price_a = Integer.parseInt(price_From);
        Integer price_b = Integer.parseInt(price_To);

        for (Integer i = 0; i < prices.size(); i++) {
            actions.moveToElement(prices.get(i));
            actions.perform();

            Integer p = Integer.parseInt(prices.get(i).getText().replaceAll(" ", ""));
            Assert.assertTrue(price_a <= p);
            Assert.assertTrue(p <= price_b);
        }
    }

    public void checkItemsPrice(ChromeDriver driver, String price_From, String price_To) {
        try {
            Actions actions = new Actions(driver);

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<WebElement> buttonNextPage = driver.findElementsByCssSelector(BUTTON_NEXT_PAGE);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            do {
                List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);
                checkPrice(actions, price_From, price_To, itemsPrices);

                if (buttonNextPage.size() != 0) {
                    actions.moveToElement(buttonNextPage.get(0));
                    actions.perform();

                    buttonNextPage.get(0).click();
                    waitToPreloader(PRELOADER_MASK);

                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    buttonNextPage = driver.findElementsByCssSelector(BUTTON_NEXT_PAGE);
                    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                }
            }
            while (buttonNextPage.size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goItemDetailPage(ChromeDriver driver, Integer pos) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - pos - 1));
        actions.perform();
        itemsPrices.get(itemsPrices.size() - pos - 1).click();
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

    public Integer getPriceOneItemList(ChromeDriver driver, Integer pos) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - pos - 1));
        actions.perform();

        return Integer.parseInt(itemsPrices.get(itemsPrices.size() - pos - 1).getText().replaceAll(" ", ""));
    }

    public void addItemToBasketGoBasket(ChromeDriver driver, Integer pos) {
        Actions actions = new Actions(driver);
        List<WebElement> itemsPrices = driver.findElementsByCssSelector(BUTTON_ADD_TO_BASKET_IN_LIST);

        actions.moveToElement(itemsPrices.get(itemsPrices.size() - pos - 1));
        actions.perform();
        itemsPrices.get(itemsPrices.size() - pos - 1).click();

        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BUTTON_ADD_TO_BASKET_IN_CARD))).click();

        /*
        WebElement basket = driver.findElementByCssSelector(A_BASKET);
        actions.moveToElement(basket);
        actions.perform();
        basket.click();
        */
    }

    public void checkDelivery(ChromeDriver driver, Integer summ) {
        if(summ < FREE_DELIVERY) {
            Integer price_up = Integer.parseInt(driver.findElementByCssSelector(FREE_DELIVERY_STATUS_PRICE_UP).getText().replaceAll(" ","").replaceAll("\u20BD",""));
            Assert.assertEquals(Integer.valueOf(FREE_DELIVERY - summ), Integer.valueOf(price_up));
        } else {
            String deliveryStatus = driver.findElementByCssSelector(FREE_DELIVERY_ACCEPTED).getText();
            Assert.assertEquals(deliveryStatus, B_FREE_DELIVERY_ACCEPTED_MESSAGE);
        }
    }

    public void checkStore(ChromeDriver driver) {
        String storeMessage = driver.findElementByCssSelector(STORE_STATUS_MESSAGE).getText();
        Assert.assertEquals(STORE_STATUS_MESSAGE_NO_LIMIT, storeMessage);
    }

    public void checkTotalPrice(ChromeDriver driver, Integer summ) {
        Integer totalPrice = Integer.parseInt(driver.findElementByCssSelector(SPAN_TOTAL_PRICE).getText().replaceAll(" ", "").replaceAll("\u20BD", ""));
        Assert.assertEquals(summ, totalPrice);
    }

    public void addMoreItem(ChromeDriver driver, Integer price, Integer minSumm) {
        try {
            waitToPreloader(BASKET_PRELOADER_MASK);
            Integer count = 1;

            checkDelivery(driver, price * count);

            while (price * count < minSumm) {
                driver.findElementByCssSelector(BUTTON_PLUS).click();
                waitToPreloader(BASKET_PRELOADER_MASK);
                checkStore(driver);

                count = Integer.parseInt(driver.findElementByCssSelector(INPUT_COUNT_ITEM_BASKET).getAttribute("value"));
                checkDelivery(driver, price * count);
                checkTotalPrice(driver, price * count);
            }
            Thread.sleep(5000);
            checkStore(driver);
            checkDelivery(driver, price * count);
            checkTotalPrice(driver, price * count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Parameters({"catalogSection", "priceFrom", "priceTo", "positionFromEnd", "minSum"})
    public void CheckBasket(String catalogSection, String priceFrom, String priceTo, String positionFromEnd, String minSum) {
        Integer priceOneItem;
        try {
            goCatalogSection(driver, catalogSection);
            setFilter(driver, priceFrom, priceTo);
            checkItemsPrice(driver, priceFrom, priceTo);

            //goItemDetailPage(driver, Integer.parseInt(positionFromEnd));
            //priceOneItemDetail = getPriceOneItem(driver);
            //addToBasket(driver);

            priceOneItem = getPriceOneItemList(driver, Integer.parseInt(positionFromEnd));
            addItemToBasketGoBasket(driver, Integer.parseInt(positionFromEnd));
            addMoreItem(driver, priceOneItem, Integer.parseInt(minSum));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
