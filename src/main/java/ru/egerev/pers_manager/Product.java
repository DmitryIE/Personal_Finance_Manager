package ru.egerev.pers_manager;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Product {

    private String title;
    private LocalDate date;
    private double sum;

    public Product(String title, String date, Double sum) {
        this.title = title;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getSum() {
        return sum;
    }

    // преобразование объекта в json и сохранение его в String
    public String saveToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // преобразование строки в формате json в объект product
    public static Product loadJson(String jsonToString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonToString, Product.class);
    }

    @Override
    public String toString() {
        return "наименование продукта: " + title + " дата: " + date + " сумма: " + sum;
    }
}
