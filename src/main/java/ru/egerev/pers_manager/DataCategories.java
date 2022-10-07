package ru.egerev.pers_manager;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataCategories {

    private Map<String, String> categories;

    private Map<String, List<Product>> productsForCategory;  // ключ - категория; значение - List из объектов типа Product
    @Expose
    private MaxCategory maxCategory;
    @Expose
    private MaxYearCategory maxYearCategory;
    @Expose
    private MaxMonthCategory maxMonthCategory;
    @Expose
    private MaxDayCategory maxDayCategory;

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
        findMaxYearCategory();
        findMaxMonthCategory();
        findMaxDayCategory();
    }

    public void findMaxCategory() {
        // создаем Map, содержащую только информацию о категории и общей сумме по ней
        Map<String, Double> categorySum = new HashMap<>();
        categorySum = productsForCategory.entrySet().stream()
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> e.getValue().stream().mapToDouble(i -> i.getSum()).sum()));

        // ищем максимальное значение
        double maxValue = categorySum.entrySet().stream()
                .map(e -> e.getValue())
                .max(Double::compareTo)
                .get();

        //определяем категорию с максимальной суммой
        for (String category : categorySum.keySet()) {
            if (categorySum.get(category) == maxValue) {
                maxCategory = new MaxCategory(category, maxValue);
            }
        }
    }

    public void findMaxYearCategory() {

        // создаем отфильтрованный по году Map
        Map<String, List<Product>> productsForCategoryFilter = new HashMap<>();
        for (String category : productsForCategory.keySet()) {
            List<Product> productList = new ArrayList<>();
            for (Product element : productsForCategory.get(category)) {
                if (element.getDate().getYear() == LocalDate.now().getYear()) {
                    productList.add(element);
                }
            }
            if (productList.size() > 0) {
                productsForCategoryFilter.put(category, productList);
            }
        }

        // создаем Map, содержащую только информацию о категории и общей сумме по ней
        Map<String, Double> categorySum = new HashMap<>();

        categorySum = productsForCategoryFilter.entrySet().stream()
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> e.getValue().stream().mapToDouble(i -> i.getSum()).sum()));

        // ищем максимальное значение
        double maxValue = 0;
        try {
            maxValue = categorySum.entrySet().stream()
                    .map(e -> e.getValue())
                    .max(Double::compareTo)
                    .get();
        } catch (NoSuchElementException e) {
        }

        //определяем категорию с максимальной суммой
        for (String category : categorySum.keySet()) {
            if (categorySum.get(category) == maxValue) {
                maxYearCategory = new MaxYearCategory(category, maxValue);
            }
        }
    }

    public void findMaxMonthCategory() {

        // создаем отфильтрованный по году Map
        Map<String, List<Product>> productsForCategoryFilter = new HashMap<>();
        for (String category : productsForCategory.keySet()) {
            List<Product> productList = new ArrayList<>();
            for (Product element : productsForCategory.get(category)) {
                if (element.getDate().getYear() == LocalDate.now().getYear() &&
                        element.getDate().getMonth() == LocalDate.now().getMonth()) {
                    productList.add(element);
                }
            }
            if (productList.size() > 0) {
                productsForCategoryFilter.put(category, productList);
            }
        }
        // создаем Map, содержащую только информацию о категории и общей сумме по ней
        Map<String, Double> categorySum = new HashMap<>();

        categorySum = productsForCategoryFilter.entrySet().stream()
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> e.getValue().stream().mapToDouble(i -> i.getSum()).sum()));

        // ищем максимальное значение
        double maxValue = 0;
        try {
            maxValue = categorySum.entrySet().stream()
                    .map(e -> e.getValue())
                    .max(Double::compareTo)
                    .get();
        } catch (NoSuchElementException e) {
        }

        //определяем категорию с максимальной суммой
        for (String category : categorySum.keySet()) {
            if (categorySum.get(category) == maxValue) {
                maxMonthCategory = new MaxMonthCategory(category, maxValue);
            }
        }
    }

    public void findMaxDayCategory() {

        // создаем отфильтрованный по году Map
        Map<String, List<Product>> productsForCategoryFilter = new HashMap<>();
        for (String category : productsForCategory.keySet()) {
            List<Product> productList = new ArrayList<>();
            for (Product element : productsForCategory.get(category)) {
                if (element.getDate().getYear() == LocalDate.now().getYear() &&
                        element.getDate().getMonth() == LocalDate.now().getMonth() &&
                        element.getDate().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                    productList.add(element);
                }
            }
            if (productList.size() > 0) {
                productsForCategoryFilter.put(category, productList);
            }
        }
        // создаем Map, содержащую только информацию о категории и общей сумме по ней
        Map<String, Double> categorySum = new HashMap<>();

        categorySum = productsForCategoryFilter.entrySet().stream()
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> e.getValue().stream().mapToDouble(i -> i.getSum()).sum()));

        // ищем максимальное значение
        double maxValue = 0;
        try {
            maxValue = categorySum.entrySet().stream()
                    .map(e -> e.getValue())
                    .max(Double::compareTo)
                    .get();
        } catch (NoSuchElementException e) {
        }

        //определяем категорию с максимальной суммой
        for (String category : categorySum.keySet()) {
            if (categorySum.get(category) == maxValue) {
                maxDayCategory = new MaxDayCategory(category, maxValue);
            }
        }
    }

    public void saveDataCategories() throws Exception {
        try (FileWriter fileWriter = new FileWriter("data.bin")) {
            Gson gson = new Gson();
            gson.toJson(this, fileWriter);
        }
    }

    public static DataCategories loadDataCategories() throws Exception {
        try (FileReader fileReader = new FileReader("data.bin")) {
            Gson gson = new Gson();
            return gson.fromJson(fileReader, DataCategories.class);
        }
    }

    // внутренний класс, описывающий объект с максимальной суммой по категории
    public class MaxCategory {

        @Expose
        private String category;
        @Expose
        private double sum;

        public MaxCategory(String category, double sum) {
            this.category = category;
            this.sum = sum;
        }

        // для проведения тестов
        @Override
        public int hashCode() {
            int result = category == null ? 0 : category.hashCode();
            result = 31 * result + (int) sum;
            return result;
        }


        // для проведения тестов
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

    public class MaxYearCategory {

        @Expose
        private String category;
        @Expose
        private double sum;

        public MaxYearCategory(String category, double sum) {
            this.category = category;
            this.sum = sum;
        }
    }

    public class MaxMonthCategory {

        @Expose
        private String category;
        @Expose
        private double sum;

        public MaxMonthCategory(String category, double sum) {
            this.category = category;
            this.sum = sum;
        }
    }

    public class MaxDayCategory {

        @Expose
        private String category;
        @Expose
        private double sum;

        public MaxDayCategory(String category, double sum) {
            this.category = category;
            this.sum = sum;
        }
    }
}
