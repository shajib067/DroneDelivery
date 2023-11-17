package com.walmartlabs.drone.delivery.utils;

import com.walmartlabs.drone.delivery.models.Order;
import io.github.pixee.security.BoundedLineReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFileParser {
    private static final String SPLITTER = " ";

    public List<Order> parseFile(String fileName) throws IOException {

        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<Order> orders = new ArrayList<>();
        String currentLine = null;

        while((currentLine = BoundedLineReader.readLine(bufferedReader, 5_000_000)) != null) {
            String[] order = currentLine.split(SPLITTER);
            String orderId = order[0];
            double distance = OrderUtils.getDistance(order[1]);
            long orderTime = OrderUtils.getOrderTimeInSecond(order[2]);
            orders.add(Order.builder().setOrderId(orderId).setDistance(distance).setOrderTime(orderTime).build());
        }

        bufferedReader.close();

        return orders;
    }
}
