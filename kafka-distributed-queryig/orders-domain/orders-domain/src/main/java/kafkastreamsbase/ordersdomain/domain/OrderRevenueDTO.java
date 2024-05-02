package kafkastreamsbase.ordersdomain.domain;

public record OrderRevenueDTO(
        String locationId,

        OrderType orderType,
        TotalRevenue totalRevenue
) {
}
