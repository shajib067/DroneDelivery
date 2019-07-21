package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.utils.DeliveryFileWriter;
import com.walmartlabs.drone.delivery.utils.DroneDeliveryConstants;
import com.walmartlabs.drone.delivery.utils.OrderFileParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.walmartlabs.drone.delivery.utils.OrderUtils.calculateScore;
import static com.walmartlabs.drone.delivery.utils.OrderUtils.checkNonNull;

public class SchedulingServiceImpl implements SchedulingService {

    private OrderFileParser orderFileParser = new OrderFileParser();
    private DeliveryFileWriter deliveryFileWriter = new DeliveryFileWriter();

    @Override
    public List<Order> deserializeOrders(String fileName) throws IOException {
        checkNonNull(fileName, "fileName can not be null");
        return orderFileParser.parseFile(fileName);
    }

    @Override
    public Order nextOrder(List<Order> orders, long deliveryStartTime) {

        checkNonNull(orders, "Orders list can not be null");

        sortOrderByScore(orders, deliveryStartTime);

        Order result = null;
        long cutOff = Long.MAX_VALUE;

        for (Order order : orders) {
            long currentOrderStartTime = Math.max(deliveryStartTime, order.getOrderTime());
            long oneWayShippingDuration = (long) (order.getDistance() / DroneDeliveryConstants.DRONE_SPEED_PER_SECOND);

            if (result == null || currentOrderStartTime + oneWayShippingDuration * 2 <= cutOff) {
                long oldCutOff = cutOff;
                cutOff = order.getOrderTime() + DroneDeliveryConstants.SECONDS_IN_HOUR;
                long d = currentOrderStartTime + oneWayShippingDuration;
                while(cutOff < d) {
                    cutOff += DroneDeliveryConstants.SECONDS_IN_HOUR;
                }
                cutOff = Math.min(cutOff, oldCutOff - oneWayShippingDuration);
                cutOff -= oneWayShippingDuration;
                result = order;
            }
        }
        orders.remove(result);

        return result;
    }

    @Override
    public List<Delivery> scheduleOrderDeliveries(List<Order> orderList) {

        checkNonNull(orderList, "Order list can not be null");

        long deliveryStartTime = Math.max(orderList.get(0).getOrderTime(), DroneDeliveryConstants.DELIVERY_START_TIME);
        List<Order> orders = new ArrayList<>(orderList);
        List<Delivery> deliveries = new ArrayList<>();

        while (!orders.isEmpty()) {
            orders = filterValidOrders(orders, deliveryStartTime);
            Order orderToDeliver = nextOrder(orders, deliveryStartTime);
            if (orderToDeliver != null) {
                deliveryStartTime = Math.max(deliveryStartTime, orderToDeliver.getOrderTime());
                deliveries.add(Delivery.builder()
                        .setOrderId(orderToDeliver.getOrderId())
                        .setDeliveryStartTime(deliveryStartTime)
                        .setScore(calculateScore(orderToDeliver, deliveryStartTime))
                        .build());
                deliveryStartTime += orderToDeliver.getDistance() / DroneDeliveryConstants.DRONE_SPEED_PER_SECOND * 2;
            }
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

    private void sortOrderByScore(List<Order> orders, long deliveryStartTime) {

        orders.sort((a, b) -> {
            Integer pointA = calculateScore(a, deliveryStartTime);
            Integer pointB = calculateScore(b, deliveryStartTime);
            return pointB.compareTo(pointA);
        });
    }

    private List<Order> filterValidOrders(List<Order> orders, long deliveryStartTime) {

        return orders.stream()
                .filter(r -> Math.max(r.getOrderTime(), deliveryStartTime)
                        + r.getDistance() / DroneDeliveryConstants.DRONE_SPEED_PER_SECOND * 2
                        <= DroneDeliveryConstants.DELIVERY_END_TIME)
                .collect(Collectors.toList());
    }
}
