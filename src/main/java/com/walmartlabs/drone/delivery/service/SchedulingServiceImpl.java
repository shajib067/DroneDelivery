package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.utils.DeliveryFileWriter;
import com.walmartlabs.drone.delivery.utils.DroneDeliveryConstants;
import com.walmartlabs.drone.delivery.utils.OrderFileParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.walmartlabs.drone.delivery.utils.OrderUtils.getScore;
import static com.walmartlabs.drone.delivery.utils.OrderUtils.checkNonNull;

public class SchedulingServiceImpl implements SchedulingService {

    private OrderFileParser orderFileParser = new OrderFileParser();
    private DeliveryFileWriter deliveryFileWriter = new DeliveryFileWriter();

    @Override
    public List<Order> deserializeOrders(String fileName) throws IOException {
        checkNonNull(fileName, "fileName can not be null");
        return orderFileParser.parseFile(fileName);
    }

    int getPrevOrderIndex(Order[] orders, int i) {
        int left = 0, right = i - 1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if(orders[mid].getFinishTime() <= orders[i].getStartTime()) {
                if(mid + 1 < i && orders[mid + 1].getFinishTime() > orders[i].getStartTime()) {
                    return mid;
                }
                else {
                    left = mid + 1;
                }
            }
            else {
                right = mid - 1;
            }
        }
        return -1;
    }

    @Override
    public Order nextOrder(List<Order> orders) {

        checkNonNull(orders, "Orders list can not be null");

        sortOrdersByFinishTime(orders);

        Order[] orderArray = orders.stream().toArray(Order[]::new);
        int[] scores = new int[orderArray.length];
        int[] prevOrderIndex = new int[orderArray.length];

        scores[0] = orderArray[0].getScore();
        prevOrderIndex[0] = 0;

        for(int i = 1; i < orderArray.length; i++) {
            int score = orderArray[i].getScore();
            int prevIndex = getPrevOrderIndex(orderArray, i);
            prevOrderIndex[i] = i;
            if (prevIndex != -1) {
                score += scores[prevIndex];
                prevOrderIndex[i] = prevOrderIndex[prevIndex];
            }
            if(score <= scores[i - 1]) {
                prevOrderIndex[i] = prevOrderIndex[i - 1];
            }
            scores[i] = Math.max(scores[i - 1], score);
        }

        Order result = orderArray[prevOrderIndex[orderArray.length - 1]];

        orders.remove(result);

        return result;
    }

    @Override
    public List<Delivery> scheduleOrderDeliveries(List<Order> orderList) {

        checkNonNull(orderList, "Order list can not be null");

        long startTime = DroneDeliveryConstants.DELIVERY_START_TIME;

        List<Order> orders = new ArrayList<>(orderList);
        List<Delivery> deliveries = new ArrayList<>();

        while (!orders.isEmpty()) {
            updateOrders(orders, startTime);
            orders = filterValidOrders(orders);
            Order orderToDeliver = nextOrder(orders);
            deliveries.add(new Delivery(orderToDeliver.getOrderId(), orderToDeliver.getStartTime(), orderToDeliver.getScore()));
            startTime = orderToDeliver.getFinishTime();
        }

        return deliveries;
    }

    @Override
    public String serializeDeliveries(String fileName, List<Delivery> deliveries, int totalOrderCount) throws IOException {

        checkNonNull(fileName, "fileName can not be null");
        checkNonNull(deliveries, "Delivery list can not be null");

        deliveryFileWriter.writeDeliveriesToFile(fileName, deliveries, totalOrderCount);

        return fileName;
    }

    private List<Order> filterValidOrders(List<Order> orders) {

        return orders.stream()
                .filter(r -> r.getFinishTime() <= DroneDeliveryConstants.DELIVERY_END_TIME)
                .collect(Collectors.toList());
    }

    private void updateOrders(List<Order> orders, long startTime) {
        orders.stream().forEach(order -> {
            if(startTime > order.getStartTime()) {
                order.setStartTime(startTime);
                order.setFinishTime(order.getStartTime() + order.getShippingDuration() * 2);
            }
            order.setScore(getScore(order));
        });
    }

    private void sortOrdersByFinishTime(List<Order> orders) {
        orders.sort(Comparator.comparingLong(Order::getFinishTime));
    }
}
