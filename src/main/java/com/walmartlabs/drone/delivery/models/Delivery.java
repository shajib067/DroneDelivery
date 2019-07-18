package com.walmartlabs.drone.delivery.models;

public class Delivery {
    private String orderId;
    private long deliveryStartTime;
    private int score;

    public Delivery(String orderId, long deliveryStartTime, int score) {
        this.orderId = orderId;
        this.deliveryStartTime = deliveryStartTime;
        this.score = score;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(long deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
