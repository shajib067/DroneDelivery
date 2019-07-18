package com.walmartlabs;

public class Order{

    private String orderId;
    private double distance;
    private long orderTime; // in seconds

    public Order(String orderId, double distance, long orderTime) {
        this.setOrderId(orderId);
        this.setDistance(distance);
        this.setOrderTime(orderTime);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }
}
