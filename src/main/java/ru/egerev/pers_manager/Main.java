package ru.egerev.pers_manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            DataCategories dataCategories = null;

            if (Files.exists(Paths.get("data.bin"))) {
                dataCategories = DataCategories.loadDataCategories();
            } else {
                dataCategories = new DataCategories();
            }

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader
                             (new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("New connection accepted");
                    Product product = Product.loadJson(reader.readLine());

                    // обрабатываем полученную информацию
                    dataCategories.processing(product);
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                            .create();
                    String result = gson.toJson(dataCategories);
                    writer.println(result);

                    // сохраняем объект статистики
                    dataCategories.saveDataCategories();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
