package ru.egerev.pers_manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DataCategoriesTest {

    @DisplayName("Проверка определения категории продукта")
    @Test
    public void testDetermineCategory() throws Exception {
        DataCategories dataCategories = new DataCategories();
        Product product = new Product("греча", "2029.10.05", 20000.);
        Assertions.assertEquals("другое", dataCategories.determineCategory(product));
    }

    @DisplayName("Проверка включения объекта Product в Map с категориями и продуктами")
    @Test
    public void testAccept() throws Exception {
        DataCategories dataCategories = new DataCategories();
        int valueBegin = dataCategories.getProductsForCategory().size();
        Product productAdd = new Product("греча", "2029.10.05", 20000.);
        dataCategories.accept(productAdd);
        int valueFinish = dataCategories.getProductsForCategory().size();
        int difference = valueFinish - valueBegin;
        Assertions.assertEquals(1, difference);
    }

    @DisplayName("Проверка поиска максимальной категории по сумме")
    @Test
    public void findMaxCategory() throws Exception {
        DataCategories dataCategories = new DataCategories();
        Product product = new Product("греча", "2029.10.05", 8000.);
        dataCategories.processing(product);
        DataCategories.MaxCategory maxCategory = new DataCategories().new MaxCategory("другое", 8000.0);
        Assertions.assertEquals(maxCategory, dataCategories.getMaxCategory());
    }
}
