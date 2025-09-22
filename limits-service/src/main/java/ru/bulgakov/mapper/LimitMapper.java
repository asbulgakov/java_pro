package ru.bulgakov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.bulgakov.dto.LimitDtoRs;
import ru.bulgakov.model.UserLimit;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LimitMapper {
    LimitDtoRs toDto(UserLimit entity);
}
