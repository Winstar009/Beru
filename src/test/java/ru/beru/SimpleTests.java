package ru.beru;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTests extends WebDriverInit {
    @Test
    public void equalsTitle() {
        String title = driver.getTitle();
        String expectedTitle = "Маркетплейс Беру - большой ассортимент товаров из интернет-магазинов с быстрой доставкой и по выгодным ценам";
        Assert.assertEquals(title, expectedTitle);
    }
}
