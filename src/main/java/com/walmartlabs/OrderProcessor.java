package com.walmartlabs;

import java.util.*;
import java.util.stream.Collectors;

import static com.walmartlabs.OrderUtils.getScore;

public class OrderProcessor {

    private static final int HOUR_SECONDS = 3600;
    private static final int MINUTE_SECONDS = 60;
    private static final int DELIVERY_START_TIME = 6 * 3600;
    private static final int DELIVERY_END_TIME = 22 * 3600;
    private static final String OUTPUT_FILE = "output.txt";

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

    private Order nextOrder(List<Order> input, long startTime) {

        sortOrderByScore(input, startTime);
        List<Order> orders = filterValidOrders(input, startTime);

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
        input.remove(result);

        return result;
    }

    public List<Delivery> scheduleOrderDeliveries(List<Order> orders) {

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
}
