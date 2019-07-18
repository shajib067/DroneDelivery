package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.utils.DroneDeliveryConstants;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestSchedulingServiceImpl {

    private SchedulingService schedulingService = new SchedulingServiceImpl();

    private final static List<Order> ORDERS = Arrays.asList(
            Order.builder().setOrderId("WM001").setDistance(12.083045973594572).setOrderTime(18710).build(),
            Order.builder().setOrderId("WM002").setDistance(3.605551275463989).setOrderTime(18715).build(),
            Order.builder().setOrderId("WM003").setDistance(50.48762224545735).setOrderTime(19910).build(),
            Order.builder().setOrderId("WM004").setDistance(12.083045973594572).setOrderTime(22310).build());

    @Rule
    public ExpectedException testRuleException = ExpectedException.none();

    @Test
    public void testDeserializeOrders() throws IOException {
        final String fileName = getClass().getClassLoader().getResource("orders.txt").getFile();
        List<Order> orders = schedulingService.deserializeOrders(fileName);
        assertEquals(4, orders.size());
        assertEquals(ORDERS.get(0).getOrderId(), orders.get(0).getOrderId());
        assertEquals(ORDERS.get(0).getDistance(), orders.get(0).getDistance(), 0.001);
        assertEquals(ORDERS.get(0).getOrderTime(), orders.get(0).getOrderTime());
    }

    @Test
    public void testDeserializeFailure() throws IOException {
        testRuleException.expect(NullPointerException.class);
        schedulingService.deserializeOrders(null);
    }

    @Test
    public void testNextOrder() {
        Order order = schedulingService.nextOrder(new ArrayList<>(ORDERS), DroneDeliveryConstants.DELIVERY_START_TIME);
        assertEquals(ORDERS.get(1), order);
    }

    @Test
    public void testScheduleOrderDeliveries() {
    }

    @Test
    public void testSerializeDeliveries() {
    }
}
