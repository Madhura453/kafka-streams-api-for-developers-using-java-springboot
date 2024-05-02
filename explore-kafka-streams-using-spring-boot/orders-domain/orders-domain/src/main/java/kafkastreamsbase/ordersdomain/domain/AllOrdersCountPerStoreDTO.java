package kafkastreamsbase.ordersdomain.domain;

public record AllOrdersCountPerStoreDTO(String locationId,
                                        Long orderCount,
                                        OrderType orderType) {
}
