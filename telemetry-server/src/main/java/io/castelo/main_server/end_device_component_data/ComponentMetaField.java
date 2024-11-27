package io.castelo.main_server.end_device_component_data;

import org.springframework.data.mongodb.core.mapping.Field;

public record ComponentMetaField(
        @Field("endDeviceMac")
        String endDeviceMac,
        @Field("componentNumber")
        int componentNumber
) {
}
