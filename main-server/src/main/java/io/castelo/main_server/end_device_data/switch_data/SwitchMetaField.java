package io.castelo.main_server.end_device_data.switch_data;

import org.springframework.data.mongodb.core.mapping.Field;

public record SwitchMetaField(
        @Field("endDeviceMac")
        String endDeviceMac,
        @Field("switchNumber")
        int switchNumber
) {
}
