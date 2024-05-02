package kafkastreams.ordersmanagementstreams.client;


import kafkastreamsbase.ordersdomain.domain.HostInfoDTO;
import kafkastreamsbase.ordersdomain.domain.OrderCountPerStoreDTO;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;

@Component
@Slf4j
public class OrdersServiceClient {
    /*
    - This is going to be the one which will hold the rest client to make the call to the other instance.
    - This is going to be the one which will hold the rest client to make the call to the other instance.
     */
    private WebClient webClient;

    public OrdersServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<OrderCountPerStoreDTO> retrieveOrdersCountByOrderType(HostInfoDTO hostInfoDTO,
                                                                      String orderType) {
        String basePath = "http://" + hostInfoDTO.host() + ":" + hostInfoDTO.port();
        // this is the base path to identify the other machines, address and port
        String url = UriComponentsBuilder
                .fromHttpUrl(basePath)
                .path("/v1/orders/count/{order_type}")
                /*
                this is going to make sure and instruct the other instance when it receives this request.
                Do not make the call to the other instances
                 */
                .queryParam("query_other_hosts",false)
                .buildAndExpand(orderType)
                .toString();
        log.info("retrieveOrdersCountByOrderType url : {} ", url);
        return webClient.get()
                .uri(url)
                .retrieve()
                //This is going to give you, this is going to give you a response back
                .bodyToFlux(OrderCountPerStoreDTO.class)
                .collectList()
                // converting flux to list
                .block();
        //  when you call the block function, this is going to be returning you a list that you are expecting
    }

    public OrderCountPerStoreDTO retrieveOrdersCountByOrderTypeAndLocationId(HostInfoDTO hostInfoDTO, String orderType, String locationId) {
        String basePath = "http://" + hostInfoDTO.host() + ":" + hostInfoDTO.port();

        String url = UriComponentsBuilder
                .fromHttpUrl(basePath)
                .path("/v1/orders/count/{order_type}/location")
                .queryParam("query_other_hosts",false)
                .queryParam("location_id",locationId)
                .buildAndExpand(orderType)
                .toString();
        log.info("retrieveOrdersCountByOrderTypeAndLocationId url : {} ", url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(OrderCountPerStoreDTO.class)
                .block();
    }
    /*
    flux and mono as a reactive types.
Flux represent multiple values or mono represent a single value
     */
}
