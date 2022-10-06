package ru.egerev.pers_manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCategories {

    private transient Map<String, String> categories;

    private transient Map<String, List<Product>> productsForCategory;  // ключ - категория; значение - List из объектов типа Product

    private MaxCategory maxCategory;

    public DataCategories() throws IOException {
        this.categories = loadCategories();
        productsForCategory = new HashMap<>();
    }

    public Map<String, List<Product>> getProductsForCategory() {
        return productsForCategory;
    }

    public MaxCategory getMaxCategory() {
        return maxCategory;
    }

    // метод для загрузки категорий продуктов из файла "categories.tsv"
    public static Map loadCategories() throws IOException {
        Map categories = new HashMap<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("categories.tsv"))) {
            while (bfr.ready()) {
                String[] line = bfr.readLine().split("\t");
                categories.put(line[0], line[1]);
            }
        }
        return categories;
    }

    // определение категории продукта
    public String determineCategory(Product product) {
        String productName = product.getTitle();
        if (categories.containsKey(productName)) {
            return categories.get(productName);
        }
        return "другое";
    }

    // добавление нового продукта в Map, где ключ - категория; значение - List из объектов типа Product
    public void accept(Product product) {
        String categoryName = determineCategory(product);
        if (productsForCategory.containsKey(categoryName)) {
            productsForCategory.get(categoryName).add(product);
        } else {
            List<Product> products = new ArrayList<>();
            products.add(product);
            productsForCategory.put(categoryName, products);
        }
    }

    // обработка информации от сервера и подготовка ответа
    public void processing(Product product) {
        accept(product);
        findMaxCategory();
    }

    // поиск категорий с максимальными суммами и создание на их основе объектов для отправки информации с сервера
    public void findMaxCategory() {

        // создаем Map, содержащую только информацию о категории и общей сумме по ней
        // и в процессе определяем, какое значение максимальное
        Map<String, Double> categorySum = new HashMap<>();
        double maxSum = 0.0;
        for (String category : productsForCategory.keySet()) {
            double sum = 0.0;
            for (Product element : productsForCategory.get(category)) {
                sum += element.getSum();
            }
            categorySum.put(category, sum);
            if (sum > maxSum) {
                maxSum = sum;
            }
        }

        //определяем категорию с максимальной суммой
        for (String category : categorySum.keySet()) {
            if (categorySum.get(category) == maxSum) {
                maxCategory = new MaxCategory(category, maxSum);
            }
        }
    }


    // внутренний класс, описывающий объект с максимальной суммой по категории
    public class MaxCategory {

        private String category;
        private double sum;

        public MaxCategory(String category, double sum) {
            this.category = category;
            this.sum = sum;
        }

        @Override
        public int hashCode() {
            int result = category == null ? 0 : category.hashCode();
            result = 31 * result + (int) sum;
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            MaxCategory maxCategory = (MaxCategory) obj;
            return sum == maxCategory.sum && category.equals(maxCategory.category);
        }
    }
}
