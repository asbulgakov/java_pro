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

    @Mapping(target = "userId", expression = "java(product.getUser() != null ? product.getUser().getId() : null)")
    @Mapping(target = "username", expression = "java(product.getUser() != null ? product.getUser().getUsername() : null)")
    ProductDtoRs toDto(Product product);

    @Mapping(source = "accountNumber", target = "accountNumber")
    Product toEntity(ProductCreateRq dto);
}
