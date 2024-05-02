package kafkastreamsbase.ordersdomain.domain;

public record Store(String locationId,
                    Address address,
                    String contactNum) {
}
