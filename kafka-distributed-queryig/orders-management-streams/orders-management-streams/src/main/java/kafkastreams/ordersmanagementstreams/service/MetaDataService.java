package kafkastreams.ordersmanagementstreams.service;

import kafkastreamsbase.ordersdomain.domain.HostInfoDTO;
import kafkastreamsbase.ordersdomain.domain.HostInfoDTOWithKey;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;
import org.apache.kafka.streams.StreamsMetadata;
import org.apache.kafka.streams.state.HostInfo;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MetaDataService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public MetaDataService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    public List<HostInfoDTO> getStreamMetaData() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        Collection<StreamsMetadata> metadataForAllStreamsClients =
                kafkaStreams.metadataForAllStreamsClients();
        return metadataForAllStreamsClients
                .stream()
                .map(streamsMetadata -> {
                    HostInfo hostInfo = streamsMetadata.hostInfo();
                    return new HostInfoDTO(hostInfo.host(), hostInfo.port());
                })
                .toList();
    }

    public HostInfoDTOWithKey getStreamsMetaDataForKey(String storeName, String locationId) {
        /*
        We need to know for the given key what the store name
        queryMetadataForKey= keySerializer
         */
        KeyQueryMetadata metaDataForKey = streamsBuilderFactoryBean
                .getKafkaStreams()
                .queryMetadataForKey(storeName, locationId, Serdes.String().serializer());

        if (metaDataForKey != null) {
            HostInfo activeHost = metaDataForKey.activeHost();
            // this is going to give you which host actually holds this particular data.
            return new HostInfoDTOWithKey(activeHost.host(),activeHost.port(),locationId);
        }

        return null;
    }
}
