package ru.bulgakov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.bulgakov.spring.dto.user.rq.UserDtoRq;
import ru.bulgakov.spring.dto.user.rs.UserDtoRs;
import ru.bulgakov.spring.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserDtoRs toDto(User user);

    User toEntity(UserDtoRq userDtoRq);
}
