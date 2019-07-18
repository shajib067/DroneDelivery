package com.walmartlabs;

public final class OrderUtils {
    private static final int HOUR_SECONDS = 3600;
    private static final int MINUTE_SECONDS = 60;
    private static final int DELIVERY_END_TIME = 22 * 3600;

    public static int getScore(Order order, long startTime) {
        long deliveryTime = (long) (startTime + Math.ceil(order.getDistance() * MINUTE_SECONDS));
        if(deliveryTime + order.getDistance() * MINUTE_SECONDS > DELIVERY_END_TIME)
            return 0;
        return 10 - (int)((deliveryTime - order.getOrderTime()) / HOUR_SECONDS);
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

    public static long getOrderTimeInSeconds(String time) {
        String[] timeUnits = time.split(":");
        return HOUR_SECONDS * Integer.parseInt(timeUnits[0]) +
                MINUTE_SECONDS * Integer.parseInt(timeUnits[1]) +
                Integer.parseInt(timeUnits[2]);
    }

    public static String getTimeFromSecond(long time) {
        int hh = (int)(time / HOUR_SECONDS);
        time %= HOUR_SECONDS;
        int mm = (int) (time / MINUTE_SECONDS);
        int ss = (int)(time % MINUTE_SECONDS);
        return "" + (hh < 10? ("0" + hh) : hh) + ":" +
                (mm < 10? ("0" + mm) : mm) + ":" +
                (ss < 10? ("0" + ss) : ss);
    }
}
