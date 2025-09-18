package ru.bulgakov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.bulgakov.spring.dto.user.rs.UserDtoRs;
import ru.bulgakov.spring.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDtoRs toDto(User user);

    User toEntity(UserDtoRs dto);
}
