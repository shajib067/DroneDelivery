package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;

import java.io.IOException;
import java.util.List;

public interface SchedulingService {
    List<Order> deserializeOrders(String fileName) throws IOException;
    Order nextOrder(List<Order> input, long startTime);
    List<Delivery> scheduleOrderDeliveries(List<Order> orders);
    String serializeDeliveries(String fileName, List<Delivery> deliveries, int totalOrderCount) throws IOException;
}
