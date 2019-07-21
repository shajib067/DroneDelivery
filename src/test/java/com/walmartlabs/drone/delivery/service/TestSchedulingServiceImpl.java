package com.walmartlabs.drone.delivery.service;

import com.walmartlabs.drone.delivery.DroneDeliveryApplication;
import com.walmartlabs.drone.delivery.models.Delivery;
import com.walmartlabs.drone.delivery.models.Order;
import com.walmartlabs.drone.delivery.utils.DroneDeliveryConstants;
import com.walmartlabs.drone.delivery.utils.OrderUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class TestSchedulingServiceImpl {

    private final static List<Order> ORDERS = Arrays.asList(
            Order.builder().setOrderId("WM001").setDistance(12.083045973594572).setOrderTime(18710).build(),
            Order.builder().setOrderId("WM002").setDistance(3.605551275463989).setOrderTime(18715).build(),
            Order.builder().setOrderId("WM003").setDistance(50.48762224545735).setOrderTime(19910).build(),
            Order.builder().setOrderId("WM004").setDistance(12.083045973594572).setOrderTime(22310).build()
    );
    private final static List<Delivery> DELIVERIES = Arrays.asList(
            Delivery.builder().setOrderId("WM002").setDeliveryStartTime(21600).setScore(10).build(),
            Delivery.builder().setOrderId("WM001").setDeliveryStartTime(22032).setScore(9).build(),
            Delivery.builder().setOrderId("WM004").setDeliveryStartTime(23481).setScore(10).build(),
            Delivery.builder().setOrderId("WM003").setDeliveryStartTime(24930).setScore(8).build()
    );
    private final static String EXPECTED_NPS = "NPS 75.0";

    private SchedulingService schedulingService = new SchedulingServiceImpl();

    @Rule
    public ExpectedException testRuleException = ExpectedException.none();

    @Test
    public void testDeserializeOrders() throws IOException {
        List<Order> orders = schedulingService.deserializeOrders(DroneDeliveryApplication.DEFAULT_INPUT_FILE);
        assertEquals(ORDERS, orders);
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
        List<Delivery> deliveries = schedulingService.scheduleOrderDeliveries(ORDERS);
        assertEquals(DELIVERIES, deliveries);
    }

    @Test
    public void testOrderTooFar() {
        List<Order> orders = Collections.singletonList(
                Order.builder().setOrderId("W001").setDistance(481).setOrderTime(21600).build());
        List<Delivery> deliveries = schedulingService.scheduleOrderDeliveries(orders);
        assertEquals(0, deliveries.size());
    }

    @Test
    public void testOrderBeyondTimeLimit() {
        List<Order> orders = Collections.singletonList(
                Order.builder().setOrderId("W001").setDistance(1).setOrderTime(79200).build());
        List<Delivery> deliveries = schedulingService.scheduleOrderDeliveries(orders);
        assertEquals(0, deliveries.size());
    }

    @Test
    public void testSerializeDeliveries() throws IOException {

        schedulingService.serializeDeliveries(DroneDeliveryApplication.DEFAULT_OUTPUT_FILE, DELIVERIES, ORDERS.size());

        BufferedReader bufferedReader = new BufferedReader(new FileReader(DroneDeliveryApplication.DEFAULT_OUTPUT_FILE));
        List<String> lines = bufferedReader.lines().collect(toList());
        bufferedReader.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DroneDeliveryApplication.DEFAULT_OUTPUT_FILE));
        bufferedWriter.flush();
        bufferedWriter.close();

        assertEquals(DELIVERIES.get(0).getOrderId() + " " +
                OrderUtils.getTimeFromSecond(DELIVERIES.get(0).getDeliveryStartTime()), lines.get(0));
        assertEquals(DELIVERIES.get(1).getOrderId() + " " +
                OrderUtils.getTimeFromSecond(DELIVERIES.get(1).getDeliveryStartTime()), lines.get(1));
        assertEquals(DELIVERIES.get(2).getOrderId() + " " +
                OrderUtils.getTimeFromSecond(DELIVERIES.get(2).getDeliveryStartTime()), lines.get(2));
        assertEquals(DELIVERIES.get(3).getOrderId() + " " +
                OrderUtils.getTimeFromSecond(DELIVERIES.get(3).getDeliveryStartTime()), lines.get(3));
        assertEquals(EXPECTED_NPS, lines.get(4));
    }

    @Test
    public void testSerializeDeliveriesFailure() throws IOException {
        testRuleException.expect(NullPointerException.class);
        schedulingService.serializeDeliveries(null, DELIVERIES, ORDERS.size());
    }
}
