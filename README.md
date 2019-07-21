# Drone Delivery Scheduling
Clone the project from [here](https://github.com/shajib067/DroneDelivery) and build from the root directory, run (for macOS/linux with mvn pre-installed):<br/>
> $ mvn install<br/>
> $ mvn exec:java -Dexec.mainClass=com.walmartlabs.drone.delivery.DroneDeliveryApplication -Dexec.args="/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/orders.txt"<br/>

Sample output:<br/>
>[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ droneDelivery ---<br/>
>Delivery schedules' file path is:<br/>
>/Users/shajibkhan/Workspace/DroneDelivery/src/main/resources/output.txt<br/>
>[INFO] ------------------------------------------------------------------------<br/>
>[INFO] BUILD SUCCESS<br/>
>[INFO] ------------------------------------------------------------------------<br/>
>[INFO] Total time: 1.300 s<br/>



Sample content of input file:<br/>
>WM001 N11W5 05:11:50<br/>
>WM002 S3E2  05:11:55<br/>
>WM003 N7E50 05:31:50<br/>
>WM004 N11E5 06:11:50<br/>

Content of output.txt file for above input:<br/>
> WM002 06:00:00<br/>
> WM001 06:07:12<br/>
> WM004 06:31:21<br/>
> WM003 06:55:30<br/>
> NPS 75.0<br/>

<h3><b>Assumptions:</b>:</h3> 

- The drone can move in any direction, so distance can be calculated from order's location coordinate using pythagorean rule

- The input file contains orders for only one day

- The input file does not contain invalid contents

- The solution needs to schedule orders for one day

- There will be only one file with orders, no subsequent order files will be processed on the same day

- NPS (Net Promoter Score) is the difference between percentage of promoters and detractors based on total orders placed. The orders not delivered that day are counted as detractors with score 0.

- NPS is maximized if total score is maximized

<h3><b>Algorithm</b>:</h3> 
<p>Using greedy algorithm with optimization approach that picks the order with highest score and finds the orders with maximum possbile scores that can be processed before it in each iteration. The algorithm is as follows:</p>

1. set the deliveryStartTime to maximum between first order placement time and 6:00AM
2. filter out the orders for which drone can not return to warehouse at or before 10:00PM
3. sort the orders by customer feedback score and point nextOrder to the order with max score
4. calculate nextOrder's cutOff time, this is calculated by adding an hour to order placement time until it's greater than or equal to delivery time of nextOrder and then
    
    set cutOff := cutOff - nextOrder's shipping duration;
5. go through the list in descending order of score until an order found that can be delivered and drone can return to warehouse before cutOff, point nextOrder to this order, update cutOff time; repeat till the end of list is reached
6. remove nextOrder from the order list
7. add delivery created from nextOder, to delivery list, to start shipping at Math.max(deliveryStartTime, nextOrder's order placement time)
8. set deliveryStartTime := nextOrder's delivery time + shipping duration; that's when the drone returns to warehouse after delivery
9. go to step 2, if order list is not empty
10. return delivery list and calculate NPS from delivery list
11. write deliveries and NPS to a file

<p>
For n number of orders there are n invocation to the process to find the next order, each of which takes nlogn time to sort by score and linear time to find next order. So, the time complexity is n<sup>2</sup>log(n). 
</p>

<b>Notes:</b><br/>
Weighted job scheduling algorithm could have been applied if delivery times were fixed, and we would have to choose a subset or orders with max total score possible; that would give nlog(n) time complexity. I tried this [here](https://github.com/shajib067/DroneDelivery/tree/weighted-job-scheduling), but doesn't always finds right solution. this might be fixed with some modified key to choose order.
