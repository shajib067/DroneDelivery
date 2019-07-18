package com.walmartlabs;

import java.io.*;
import java.util.*;

public class OrderDelivery {

    public static void main(String[] args) throws IOException {
        /*if (args.length == 0) {
            throw new IOException("No file path given");
        }*/
        OrderProcessor orderProcessor = new OrderProcessor();
        FileParser fileParser = new FileParser();
        DeliveryFileWriter deliveryFileWriter = new DeliveryFileWriter();

        List<Order> orders = fileParser.parseFile("/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/orders.txt");
        int totalOrderCount = orders.size();

        List<Delivery> deliveries = orderProcessor.scheduleOrderDeliveries(orders);
        deliveryFileWriter.writeOrderDeliveriesToFile("/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/output.txt", deliveries, totalOrderCount);
    }
}
