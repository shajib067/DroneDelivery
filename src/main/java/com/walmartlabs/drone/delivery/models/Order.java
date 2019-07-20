package com.walmartlabs.drone.delivery.models;

import com.walmartlabs.drone.delivery.utils.DroneDeliveryConstants;

public class Order{

    private String orderId;
    private double distance;
    private long orderTime; // in seconds
    private long startTime;
    private long finishTime;
    private long shippingDuration;
    private int score;

    public Order(String orderId, double distance, long orderTime, long shippingDuration, long startTime, long finishTime, int score) {
        this.setStartTime(startTime);
        this.setFinishTime(finishTime);
        this.setOrderId(orderId);
        this.setDistance(distance);
        this.setOrderTime(orderTime);
        this.setShippingDuration(shippingDuration);
        this.setScore(score);
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getShippingDuration() {
        return shippingDuration;
    }

    public void setShippingDuration(long shippingDuration) {
        this.shippingDuration = shippingDuration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
