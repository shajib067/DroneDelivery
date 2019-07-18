package com.walmartlabs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.walmartlabs.OrderUtils.getDistance;
import static com.walmartlabs.OrderUtils.getOrderTimeInSecond;

public class FileParser {
    private static final String SPLITTER = " ";

    public List<Order> parseFile(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine = null;
        List<Order> orders = new ArrayList<>();

        while((currentLine = bufferedReader.readLine()) != null) {
            String[] order = currentLine.split(SPLITTER);
            String orderId = order[0];
            double distance = getDistance(order[1]);
            long orderTime = getOrderTimeInSecond(order[2]);
            orders.add(new Order(orderId, distance, orderTime));
        }

        bufferedReader.close();

        return orders;
    }
}
