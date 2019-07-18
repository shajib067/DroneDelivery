package com.walmartlabs.drone.delivery.models;

import java.util.Objects;

public final class Delivery {
    private String orderId;
    private long deliveryStartTime;
    private int score;

    public Delivery(DeliveryBuilder builder) {
        this.orderId = builder.orderId;
        this.deliveryStartTime = builder.deliveryStartTime;
        this.score = builder.score;
    }

    public String getOrderId() {
        return orderId;
    }

    public long getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Delivery delivery = (Delivery) o;
        return delivery.orderId.equals(this.orderId)
                && delivery.deliveryStartTime == this.deliveryStartTime
                && delivery.score == this.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.orderId, this.deliveryStartTime, this.score);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "orderId=" + this.orderId +
                ", deliveryStartTime=" + this.deliveryStartTime +
                ", score=" + this.score +
                "}";
    }

    public static DeliveryBuilder builder() {
        return new DeliveryBuilder();
    }

    public static class DeliveryBuilder {
        private String orderId;
        private long deliveryStartTime;
        private int score;

        public DeliveryBuilder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public DeliveryBuilder setDeliveryStartTime(long deliveryStartTime) {
            this.deliveryStartTime = deliveryStartTime;
            return this;
        }

        public DeliveryBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public Delivery build() {
            return new Delivery(this);
        }
    }
}
