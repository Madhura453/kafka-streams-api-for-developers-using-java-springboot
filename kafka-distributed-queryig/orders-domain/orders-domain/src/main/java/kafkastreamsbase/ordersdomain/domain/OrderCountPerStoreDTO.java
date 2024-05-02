package kafkastreamsbase.ordersdomain.domain;

public record OrderCountPerStoreDTO(String locationId,
                                    Long orderCount) {
}
