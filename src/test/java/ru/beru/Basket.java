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

    private final String SPAN_PRICE = "[data-zone-name='SearchSerp']:first-child [data-auto='price'] span>span:first-child, [data-zone-name='SearchSerp']:first-child span>span:first-child>span:first-child, .n-snippet-list span>span:first-child>span:first-child";

    private final String SPAN_ITEM_PRICE = "[data-auto='cart-item']:first-child [data-auto='price']>span>span:first-child, [data-apiary-widget-name='@marketplace/CartList'] [data-auto='cart-item']:first-child span>span:first-child>span:first-child";

    private final String BUTTON_ADD_TO_BASKET_IN_LIST = "[data-zone-name='SearchSerp'] button, .n-snippet-list button";
    private final String BUTTON_ADD_TO_BASKET_IN_CARD = "[data-zone-name='SearchSerp'] a>button>span, .n-snippet-list a>button>span";

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

    @Test(description = "Go to catalog section.", priority = 0)
    @Parameters({"catalogSection"})
    private void goCatalogSection(String catalogSection) throws Exception {
        try {
            //driver.findElementByCssSelector(INPUT_SEARCH).sendKeys(catalogSection);
            //driver.findElementByCssSelector(BUTTON_SEARCH).click();

            sendKeys(driver.findElementByCssSelector(INPUT_SEARCH), catalogSection);
            click(driver.findElementByCssSelector(BUTTON_SEARCH));

            WebElement breadcrumbs = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BREADCRUMBS)));

            //breadcrumbs.findElement(By.cssSelector(A_LAST_BREADCRUMBS)).click();
            click(breadcrumbs.findElement(By.cssSelector(A_LAST_BREADCRUMBS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitToPreloader(String selector) throws Exception {
        try {
            WebElement preloader = (new WebDriverWait(driver, 30,50))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(selector)));

            (new WebDriverWait(driver, 30, 1500))
                    .until(ExpectedConditions.invisibilityOf(preloader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Set filter.", priority = 1, dependsOnMethods = "goCatalogSection")
    @Parameters({"priceFrom", "priceTo"})
    private void setFilter(String priceFrom, String priceTo) throws Exception {
        try {
            List<WebElement> price = driver.findElementsByCssSelector(INPUTS_FILTER);

            //price.get(0).sendKeys(priceFrom);
            //price.get(1).sendKeys(priceTo);

            sendKeys(price.get(0), priceFrom);
            sendKeys(price.get(1), priceTo);

            waitToPreloader(PRELOADER_MASK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPrice(Actions actions, String priceFrom, String priceTo, List<WebElement> prices) throws Exception {
        try {
            Integer price_a = Integer.parseInt(priceFrom);
            Integer price_b = Integer.parseInt(priceTo);

            for (Integer i = 0; i < prices.size(); i++) {
                actions.moveToElement(prices.get(i));
                actions.perform();

                //Integer p = Integer.parseInt(prices.get(i).getText().replaceAll(" ", ""));
                Integer p = Integer.parseInt(getText(prices.get(i)).replaceAll(" ", ""));

                Assert.assertTrue(price_a <= p);
                Assert.assertTrue(p <= price_b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Check items price from all page.", priority = 2, dependsOnMethods = "setFilter")
    @Parameters({"priceFrom", "priceTo"})
    private void checkItemsPrice(String priceFrom, String priceTo) throws Exception {
        try {
            Actions actions = new Actions(driver);

            //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<WebElement> buttonNextPage /*= driver.findElementsByCssSelector(BUTTON_NEXT_PAGE)*/;
            //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            do {
                List<WebElement> itemsPrices = driver.findElementsByCssSelector(SPAN_PRICE);
                checkPrice(actions, priceFrom, priceTo, itemsPrices);

                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                buttonNextPage = driver.findElementsByCssSelector(BUTTON_NEXT_PAGE);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

                if (buttonNextPage.size() != 0) {
                    actions.moveToElement(buttonNextPage.get(0));
                    actions.perform();

                    //buttonNextPage.get(0).click();
                    click(buttonNextPage.get(0));

                    waitToPreloader(PRELOADER_MASK);
                }
            }
            while (buttonNextPage.size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Add item in basket and go to basket.", priority = 3, dependsOnMethods = "checkItemsPrice")
    @Parameters({"positionFromEnd"})
    private void addItemToBasketGoBasket(String positionFromEnd) throws Exception {
        try {
            Integer pos = Integer.parseInt(positionFromEnd);
            Actions actions = new Actions(driver);
            List<WebElement> itemsPrices = driver.findElementsByCssSelector(BUTTON_ADD_TO_BASKET_IN_LIST);

            pos = itemsPrices.size() > 1 ? pos : 0;
            actions.moveToElement(itemsPrices.get(itemsPrices.size() - pos - 1));
            actions.perform();

            //itemsPrices.get(itemsPrices.size() - pos - 1).click();
            //(new WebDriverWait(driver, 30))
            //        .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BUTTON_ADD_TO_BASKET_IN_CARD))).click();

            click(itemsPrices.get(itemsPrices.size() - pos - 1));
            click((new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector(BUTTON_ADD_TO_BASKET_IN_CARD))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getPriceItem() throws Exception {
        try {
            WebElement price = driver.findElementByCssSelector(SPAN_ITEM_PRICE);
            //return Integer.parseInt(price.getText().replaceAll(" ", ""));
            return Integer.parseInt(getText(price).replaceAll(" ", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void checkDelivery(ChromeDriver driver, Integer summ) throws Exception {
        try {
            if(summ < FREE_DELIVERY) {
                //Integer price_up = Integer.parseInt(driver.findElementByCssSelector(FREE_DELIVERY_STATUS_PRICE_UP).getText().replaceAll(" ","").replaceAll("\u20BD",""));
                Integer price_up = Integer.parseInt(getText(driver.findElementByCssSelector(FREE_DELIVERY_STATUS_PRICE_UP)).replaceAll(" ","").replaceAll("\u20BD",""));

                Assert.assertEquals(Integer.valueOf(FREE_DELIVERY - summ), Integer.valueOf(price_up));
            } else {
                //String deliveryStatus = driver.findElementByCssSelector(FREE_DELIVERY_ACCEPTED).getText();
                String deliveryStatus = getText(driver.findElementByCssSelector(FREE_DELIVERY_ACCEPTED));

                Assert.assertEquals(deliveryStatus, B_FREE_DELIVERY_ACCEPTED_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkStore(ChromeDriver driver) throws Exception {
        try {
            //String storeMessage = driver.findElementByCssSelector(STORE_STATUS_MESSAGE).getText();
            String storeMessage = getText(driver.findElementByCssSelector(STORE_STATUS_MESSAGE));

            Assert.assertEquals(STORE_STATUS_MESSAGE_NO_LIMIT, storeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkTotalPrice(ChromeDriver driver, Integer summ) {
        try {
            //Integer totalPrice = Integer.parseInt(driver.findElementByCssSelector(SPAN_TOTAL_PRICE).getText().replaceAll(" ", "").replaceAll("\u20BD", ""));
            Integer totalPrice = Integer.parseInt(getText(driver.findElementByCssSelector(SPAN_TOTAL_PRICE)).replaceAll(" ", "").replaceAll("\u20BD", ""));

            Assert.assertEquals(summ, totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Add item to basket while total price not be over needed value.", priority = 4, dependsOnMethods = "addItemToBasketGoBasket")
    @Parameters({"minSum"})
    private void addMoreItem(String minSum) throws Exception {
        try {
            waitToPreloader(BASKET_PRELOADER_MASK);

            Integer min_sum = Integer.parseInt(minSum);
            Integer price = getPriceItem();
            Integer count = 1;

            checkDelivery(driver, price * count);

            while (price * count < min_sum) {
                //driver.findElementByCssSelector(BUTTON_PLUS).click();
                click(driver.findElementByCssSelector(BUTTON_PLUS));

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
}
