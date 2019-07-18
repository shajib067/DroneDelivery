package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.utils.DeliveryFileWriter;
import com.walmartlabs.drone.delivery.utils.OrderFileParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.walmartlabs.drone.delivery.utils.OrderUtils.getScore;
import static com.sun.tools.javac.util.Assert.checkNonNull;

public class SchedulingServiceImpl implements SchedulingService {

    private static final int HOUR_SECONDS = 3600;
    private static final int MINUTE_SECONDS = 60;
    private static final int DELIVERY_START_TIME = 6 * 3600;
    private static final int DELIVERY_END_TIME = 22 * 3600;
    private static final String OUTPUT_FILE = "output.txt";

    private OrderFileParser orderFileParser = new OrderFileParser();
    private DeliveryFileWriter deliveryFileWriter = new DeliveryFileWriter();

    @Override
    public List<Order> deserializeOrders(String fileName) throws IOException {
        checkNonNull(fileName, "fileName can not be null");
        return orderFileParser.parseFile(fileName);
    }

    @Override
    public Order nextOrder(List<Order> orderList, long startTime) {

        checkNonNull(orderList, "Orders list can not be null");

        sortOrderByScore(orderList, startTime);
        List<Order> orders = filterValidOrders(orderList, startTime);

        Order result = null;
        long cutOff = -1;

        for (Order order : orders) {
            if (result == null) {
                result = order;
                cutOff = (long) (result.getOrderTime() + HOUR_SECONDS - order.getDistance() * MINUTE_SECONDS);
                continue;
            }
            if(order.getDistance() * HOUR_SECONDS + startTime < cutOff) {
                result = order;
                cutOff = (long) (result.getOrderTime() + HOUR_SECONDS - order.getDistance() * MINUTE_SECONDS);
            }
        }
        orderList.remove(result);

        return result;
    }

    @Override
    public List<Delivery> scheduleOrderDeliveries(List<Order> orders) {

        checkNonNull(orders, "Order list can not be null");

        long startTime = Math.max(orders.get(0).getOrderTime(), DELIVERY_START_TIME);
        int size = orders.size();
        List<Delivery> deliveries = new ArrayList<>();

        while (startTime < DELIVERY_END_TIME && deliveries.size() < size) {
            Order orderToDeliver = nextOrder(orders, startTime);
            deliveries.add(new Delivery(orderToDeliver.getOrderId(), startTime, getScore(orderToDeliver, startTime)));
            startTime += orderToDeliver.getDistance() * MINUTE_SECONDS * 2;
        }

        return deliveries;
    }

    @Override
    public void serializeDeliveries(String fileName, List<Delivery> deliveries, int totalOrderCount) throws IOException {

        checkNonNull(fileName, "fileName can not be null");
        checkNonNull(deliveries, "Delivery list can not be null");

        deliveryFileWriter.writeDeliveriesToFile(fileName, deliveries, totalOrderCount);
    }

    private void sortOrderByScore(List<Order> orders, long startTime) {

        orders.sort((a, b) -> {
            Integer pointA = getScore(a, startTime);
            Integer pointB = getScore(b, startTime);
            return pointB.compareTo(pointA);
        });
    }

    private List<Order> filterValidOrders(List<Order> orders, long startTime) {

        return orders.stream()
                .filter(r -> r.getOrderTime() <= startTime)
                .collect(Collectors.toList());
    }
}
