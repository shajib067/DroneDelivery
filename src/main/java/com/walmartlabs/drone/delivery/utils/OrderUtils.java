package com.walmartlabs.drone.delivery.utils;

import com.walmartlabs.drone.delivery.models.Order;

public final class OrderUtils {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int DELIVERY_END_TIME = 22 * 3600;
    private static final double DRONE_SPEED_PER_SECOND = 0.016667;
    private static final String TIME_SPLITTER = ":";

    public static int getScore(Order order, long startTime) {
        double oneWayShippingDuration = order.getDistance() / DRONE_SPEED_PER_SECOND;
        long deliveryTime = (long) (startTime + Math.ceil(oneWayShippingDuration));
        if(deliveryTime + oneWayShippingDuration > DELIVERY_END_TIME)
            return 0;
        return 10 - (int)((deliveryTime - order.getOrderTime()) / SECONDS_IN_HOUR);
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
        String[] timeUnits = time.split(TIME_SPLITTER);
        return SECONDS_IN_HOUR * Integer.parseInt(timeUnits[0]) +
                SECONDS_IN_MINUTE * Integer.parseInt(timeUnits[1]) +
                Integer.parseInt(timeUnits[2]);
    }

    public static String getTimeFromSecond(long time) {
        int hh = (int)(time / SECONDS_IN_HOUR);
        time %= SECONDS_IN_HOUR;
        int mm = (int) (time / SECONDS_IN_MINUTE);
        int ss = (int)(time % SECONDS_IN_MINUTE);
        return "" + (hh < 10? ("0" + hh) : hh) + ":" +
                (mm < 10? ("0" + mm) : mm) + ":" +
                (ss < 10? ("0" + ss) : ss);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
