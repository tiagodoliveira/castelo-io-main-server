package io.castelo.main_server.end_device;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EndDeviceMapper {
    EndDeviceMapper INSTANCE = Mappers.getMapper(EndDeviceMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEndDeviceFromDto(EndDeviceDTO dto, @MappingTarget EndDevice entity);
}