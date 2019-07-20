package com.walmartlabs.drone.delivery;

import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.service.SchedulingService;
import com.walmartlabs.drone.delivery.service.SchedulingServiceImpl;

import java.io.*;
import java.util.*;

import static com.walmartlabs.drone.delivery.utils.OrderUtils.isNullOrEmpty;

public class DroneDeliveryApplication {

    private static final String DEFAULT_INPUT_FILE = "/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/orders.txt";
    private static final String DEFAULT_OUTPUT_FILE = "/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/output.txt";

    private SchedulingService schedulingService = new SchedulingServiceImpl();

    public void run(String inputFile, String outputFile) throws IOException {
        inputFile = isNullOrEmpty(inputFile) ? DEFAULT_INPUT_FILE : inputFile;
        outputFile = isNullOrEmpty(outputFile) ? DEFAULT_OUTPUT_FILE : outputFile;

        List<Order> orders = schedulingService.deserializeOrders(inputFile);
        int totalOrderCount = orders.size();

        List<Delivery> deliveries = schedulingService.scheduleOrderDeliveries(orders);
        outputFile = schedulingService.serializeDeliveries(outputFile, deliveries, totalOrderCount);

        System.out.println("\nDelivery schedules' file path is:\n" + outputFile + "\n");
    }

    public static void main(String[] args) throws IOException {
        String orderFile = null, deliverFIle = null;
        if (args.length > 0) {
            orderFile = args[0];
        }
        if (args.length > 1) {
            deliverFIle = args[1];
        }
        DroneDeliveryApplication droneDeliveryApplication = new DroneDeliveryApplication();
        droneDeliveryApplication.run(orderFile, deliverFIle);
    }
}
