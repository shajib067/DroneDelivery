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

    /*private final static List<Order> ORDERS = Arrays.asList(
            new Order("WM001", 12.083045973594572, 18710),
            new Order("WM002", 3.605551275463989, 18715),
            new Order("WM003", 50.48762224545735, 19910),
            new Order("WM004", 12.083045973594572, 22310));*/

    @Rule
    public ExpectedException testRuleException = ExpectedException.none();

    @Test
    public void testDeserializeOrders() throws IOException {
        final String fileName = getClass().getClassLoader().getResource("orders.txt").getFile();
        List<Order> orders = schedulingService.deserializeOrders(fileName);
        assertEquals(4, orders.size());
        /*assertEquals(ORDERS.get(0).getOrderId(), orders.get(0).getOrderId());
        assertEquals(ORDERS.get(0).getDistance(), orders.get(0).getDistance(), 0.001);
        assertEquals(ORDERS.get(0).getOrderTime(), orders.get(0).getOrderTime());*/
    }

/*
    @Test
    public void testDeserializeFailure() throws IOException {
        testRuleException.expect(NullPointerException.class);
        schedulingService.deserializeOrders(null);
    }

    @Test
    public void nextOrder() {
        schedulingService.nextOrder(new ArrayList<>(ORDERS), DroneDeliveryConstants.DELIVERY_START_TIME);
    }
*/

    @Test
    public void scheduleOrderDeliveries() {
    }

    @Test
    public void serializeDeliveries() {
    }
}
