package ru.bulgakov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.bulgakov.spring.dto.product.rq.ProductCreateRq;
import ru.bulgakov.spring.dto.product.rs.ProductDtoRs;
import ru.bulgakov.spring.model.Product;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface ProductMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    ProductDtoRs toDto(Product product);

    @Mapping(target = "accountNumber", source = "accountNumber")
    Product toEntity(ProductCreateRq dto);
}
