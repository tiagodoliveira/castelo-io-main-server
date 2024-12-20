package io.castelo.main_server.end_device;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EndDeviceMapper {
    EndDeviceMapper INSTANCE = Mappers.getMapper(EndDeviceMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "endDeviceMac", ignore = true)
    @Mapping(target = "endDeviceModel", ignore = true)
    @Mapping(target = "components", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEndDeviceFromDto(EndDeviceDTO dto, @MappingTarget EndDevice entity);
}