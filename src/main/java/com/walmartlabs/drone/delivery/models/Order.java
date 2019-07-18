package com.walmartlabs.drone.delivery.models;

import java.util.Objects;

public final class Order{

    private final String orderId;
    private final double distance; // in block units
    private final long orderTime; // in seconds

    public Order(OrderBuilder builder) {
        this.orderId = builder.orderId;
        this.distance = builder.distance;
        this.orderTime = builder.orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getDistance() {
        return distance;
    }

    public long getOrderTime() {
        return orderTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return order.orderId.equals(this.orderId)
                && order.distance == this.distance
                && order.orderTime == this.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.orderId, this.distance, this.orderTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + this.orderId +
                ", distance=" + this.distance +
                ", orderTime=" + this.orderTime +
                "}";
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static  final class OrderBuilder {
        private String orderId;
        private double distance;
        private long orderTime;

        private OrderBuilder() {
        }

        public OrderBuilder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        public OrderBuilder setOrderTime(long orderTime) {
            this.orderTime = orderTime;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
