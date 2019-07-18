package com.walmartlabs.drone.delivery.utils;

import com.walmartlabs.drone.delivery.models.Delivery;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.walmartlabs.drone.delivery.utils.OrderUtils.getTimeFromSecond;

public class DeliveryFileWriter {

    public void writeDeliveriesToFile(String fileName, List<Delivery> orderDeliveries, int totalOrderCount) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

        Collections.sort(orderDeliveries, Comparator.comparingLong(Delivery::getDeliveryStartTime));

        for (Delivery delivery : orderDeliveries) {
            bufferedWriter.write(delivery.getOrderId() + " " + getTimeFromSecond(delivery.getDeliveryStartTime()) + "\n");
        }

        bufferedWriter.write("NPS = " + calculateNPS(orderDeliveries, totalOrderCount) + "\n");

        bufferedWriter.close();
    }

    public double calculateNPS(List<Delivery> deliveries, int totalOrderCount) {

        double promoters = 0, neutral = 0, detractors = 0;

        for (Delivery delivery : deliveries) {
            if (delivery.getScore() >= 9)
                promoters++;
            else if (delivery.getScore() > 6)
                neutral++;
        }
        detractors = totalOrderCount - promoters - neutral;

        return  (promoters - detractors) / totalOrderCount * 100;
    }
}
