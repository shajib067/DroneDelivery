package com.walmartlabs.drone.delivery.utils;

import com.walmartlabs.drone.delivery.models.Order;

public final class OrderUtils {

    public static int getScore(Order order, long startTime) {
        double oneWayShippingDuration = order.getDistance() / DroneDeliveryConstants.DRONE_SPEED_PER_SECOND;
        long deliveryTime = (long) (startTime + Math.ceil(oneWayShippingDuration));
        if(deliveryTime + oneWayShippingDuration > DroneDeliveryConstants.DELIVERY_END_TIME)
            return 0;
        return 10 - (int)((deliveryTime - order.getOrderTime()) / DroneDeliveryConstants.SECONDS_IN_HOUR);
    }

    public static double getDistance(String path) {
        long vertical = 0, horizontal = 0;
        int i = 0;
        while(++i < path.length() && Character.isDigit(path.charAt(i))) {
            vertical = vertical * 10 + (path.charAt(i) - '0');
        }
        while (++i < path.length()) {
            horizontal = horizontal * 10 + (path.charAt(i) - '0');
        }
        return Math.sqrt(vertical * vertical + horizontal * horizontal);
    }

    public static long getOrderTimeInSecond(String time) {
        String[] timeUnits = time.split(DroneDeliveryConstants.TIME_SPLITTER);
        return DroneDeliveryConstants.SECONDS_IN_HOUR * Integer.parseInt(timeUnits[0]) +
                DroneDeliveryConstants.SECONDS_IN_MINUTE * Integer.parseInt(timeUnits[1]) +
                Integer.parseInt(timeUnits[2]);
    }

    public static String getTimeFromSecond(long time) {
        int hh = (int)(time / DroneDeliveryConstants.SECONDS_IN_HOUR);
        time %= DroneDeliveryConstants.SECONDS_IN_HOUR;
        int mm = (int) (time / DroneDeliveryConstants.SECONDS_IN_MINUTE);
        int ss = (int)(time % DroneDeliveryConstants.SECONDS_IN_MINUTE);
        return "" + (hh < 10? ("0" + hh) : hh) + ":" +
                (mm < 10? ("0" + mm) : mm) + ":" +
                (ss < 10? ("0" + ss) : ss);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static <T> T checkNonNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        } else {
            return reference;
        }
    }
}
