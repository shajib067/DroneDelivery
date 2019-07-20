package com.walmartlabs.drone.delivery.utils;

import com.walmartlabs.drone.delivery.models.Order;

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

        while((currentLine = bufferedReader.readLine()) != null) {
            String[] order = currentLine.split(SPLITTER);
            String orderId = order[0];
            double distance = OrderUtils.getDistance(order[1]);
            long orderTime = OrderUtils.getOrderTimeInSecond(order[2]);
            long startTime = Math.max(orderTime, DroneDeliveryConstants.DELIVERY_START_TIME);
            long shippingDuration = (long) Math.ceil(distance / DroneDeliveryConstants.DRONE_SPEED_PER_SECOND);
            long finishTime = startTime + shippingDuration * 2;
            orders.add(new Order(orderId, distance, orderTime, shippingDuration, startTime, finishTime, 0));
        }

        bufferedReader.close();

        return orders;
    }
}
