package ru.egerev.pers_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {

    private static final int PORT = 8989;
    private static final String LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(LOCALHOST, PORT);
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            Product product = generateProducts();
            writer.println(product.saveToJson());

            System.out.println(product);
            System.out.println(reader.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // генерация случайных объектов Product
    public static Product generateProducts() {
        String[] names = new String[]{"булка", "колбаса", "сухарики", "курица", "тапки", "грибы", "алмазы"};
        String[] localDates = new String[]{"2022.10.07", "2022.02.10", "2022.10.06", "2020.03.09", "2021.10.06", "2022.10.10", "2022.02.11"};
        double[] sum = new double[]{2000., 6000., 8000., 8450, 32444, 5000., 7000.};
        Random random = new Random();
        int number1 = random.nextInt(7);
        int number2 = random.nextInt(7);
        int number3 = random.nextInt(7);
        Product product = new Product(names[number1], localDates[number2], sum[number3]);
        return product;
    }
}
