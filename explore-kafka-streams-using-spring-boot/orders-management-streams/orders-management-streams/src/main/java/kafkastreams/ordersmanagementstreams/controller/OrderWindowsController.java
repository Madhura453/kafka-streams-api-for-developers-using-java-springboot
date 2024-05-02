package kafkastreams.ordersmanagementstreams.controller;

import kafkastreams.ordersmanagementstreams.service.OrdersWindowService;
import kafkastreamsbase.ordersdomain.domain.OrdersCountPerStoreByWindowsDTO;
import kafkastreamsbase.ordersdomain.domain.OrdersRevenuePerStoreByWindowsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderWindowsController {

    @Autowired
    private OrdersWindowService ordersWindowService;

    @GetMapping("/windows/count/{order_type}")
    public List<OrdersCountPerStoreByWindowsDTO> ordersCountByWindowsAndOrderType(
            @PathVariable("order_type") String orderType
    ) {
        return ordersWindowService.getOrdersCountWindowsByType(orderType);
    }

    @GetMapping("/windows/count")
    public List<OrdersCountPerStoreByWindowsDTO> getAllOrdersCountByWindows(
    ) {
        return ordersWindowService.getAllOrdersCountByWindows();
    }
    @GetMapping("/windows/time-range/count")
    public List<OrdersCountPerStoreByWindowsDTO> getAllOrdersCountByWindowsWithInTimeRange(
            @RequestParam(value = "from_time", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDateTime fromTime,
            @RequestParam(value = "to_time", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDateTime toTime
    ) {
        /*
        So if you provide this value, this is going to automatically read the value that's been sent from the endpoint.
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        - So if the end time that you are passing is the start time for the next window.
          So in this case, you may be able to see the data for the window that's starting with
         */
        if(fromTime!=null&&toTime!=null)
        {
            return ordersWindowService.getAllOrdersCountByWindowsInTimeRange(fromTime,toTime);
        }
        return ordersWindowService.getAllOrdersCountByWindows();
    }

    @GetMapping("/windows/revenue/{order_type}")
    public List<OrdersRevenuePerStoreByWindowsDTO> getAllOrdersRevenueByWindowsType(
            @PathVariable("order_type") String orderType
    ) {
        return ordersWindowService.getOrdersRevenueWindowsByType(orderType);

    }
}
