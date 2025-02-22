package umlerr.serviceusers.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import umlerr.serviceusers.model.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "actual", ignore = true)
    Users toEntity(RegisterDTO registerDTO);
}
