package kafkastreams.ordersmanagementstreams.controller;

import kafkastreams.ordersmanagementstreams.service.MetaDataService;
import kafkastreamsbase.ordersdomain.domain.HostInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/metadata")
public class MetaDataCollector {

    private MetaDataService metaDataService;

    public MetaDataCollector(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @GetMapping("/all")
    public List<HostInfoDTO> getStreamMetaData() {
        // returns all the different host and the associated port of all the Kafka stream
        // instances of this application.
        return metaDataService.getStreamMetaData();
    }
}
