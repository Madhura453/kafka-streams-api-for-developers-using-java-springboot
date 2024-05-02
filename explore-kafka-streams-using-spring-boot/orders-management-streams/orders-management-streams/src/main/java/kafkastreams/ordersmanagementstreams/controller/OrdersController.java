package kafkastreams.ordersmanagementstreams.controller;

import kafkastreams.ordersmanagementstreams.service.OrderService;
import kafkastreamsbase.ordersdomain.domain.AllOrdersCountPerStoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrdersController {

    @Autowired
    OrderService orderService;

    @GetMapping("/count/{order_type}")
    public ResponseEntity<?> ordersCount(@PathVariable("order_type") String orderType) {
// o/p orders_count_by_type.json= OrderCountPerStoreDTO
        return ResponseEntity.ok(orderService.getOrdersCount(orderType));
    }

    @GetMapping("/count/{order_type}/location")
    public ResponseEntity<?> ordersCountByLocationId(
            @PathVariable("order_type") String orderType,
            @RequestParam(value = "location_id", required = false) String locationId
    ) {
        if (StringUtils.hasLength(locationId)) {
            return ResponseEntity.ok(orderService.getOrdersCountByLocationId(orderType, locationId));
        }
        return ResponseEntity.ok(orderService.getOrdersCount(orderType));
    }

    @GetMapping("/count")
    public List<AllOrdersCountPerStoreDTO> allOrdersCount(
    ) {
        return orderService.getAllOrdersCount();
    }

    @GetMapping("/revenue/{order_type}")
    public ResponseEntity<?> revenueByOrderType(
            @PathVariable("order_type") String orderType) {
        return ResponseEntity.ok(orderService.revenueByOrderType(orderType));
    }

    @GetMapping("/revenue/location/{order_type}")
    public ResponseEntity<?> revenueByOrderTypeAndLocationId(
            @PathVariable("order_type") String orderType,
            @RequestParam(value = "location_id", required = false) String locationId) {
        if (StringUtils.hasLength(locationId)) {
            return ResponseEntity.ok(orderService.getRevenueByLocationId(orderType, locationId));
        }
        return ResponseEntity.ok(orderService.revenueByOrderType(orderType));
    }
}

