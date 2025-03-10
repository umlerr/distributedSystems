package umlerr.servicelistings.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import umlerr.servicelistings.model.Listing;

@Mapper(componentModel = "spring")
public interface ListingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actual", ignore = true)
    Listing toEntity(ListingDTO listingDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actual", ignore = true)
    void updateEntityFromDTO(ListingDTO listingDTO, @MappingTarget Listing listing);
}
