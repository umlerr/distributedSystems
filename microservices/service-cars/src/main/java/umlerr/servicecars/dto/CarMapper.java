package umlerr.servicecars.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import umlerr.servicecars.model.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actual", ignore = true)
    Car toEntity(CarDTO carDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actual", ignore = true)
    void updateEntityFromDTO(CarDTO carDTO, @MappingTarget Car car);
}
