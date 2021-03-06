package ru.beru;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTests extends WebDriverInit {
    private final String TITLE = "Маркетплейс Беру - большой ассортимент товаров из интернет-магазинов с быстрой доставкой и по выгодным ценам";

    @Test(description = "Check title site.")
    private void equalsTitle() throws Exception {
        try {
            Assert.assertEquals(driver.getTitle(), TITLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
