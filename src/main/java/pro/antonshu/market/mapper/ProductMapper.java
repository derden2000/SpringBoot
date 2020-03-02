package pro.antonshu.market.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pro.antonshu.market.dto.CategoryDto;
import pro.antonshu.market.dto.ProductDto;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Product;

import java.util.List;

@Mapper(/*componentModel = "spring", */uses = CategoryMapper.class)
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category", target = "categoryDto")
    ProductDto fromProduct(Product product);

    @Mapping(source = "categoryDto", target = "category")
    Product ToProduct(ProductDto productDto);

//    CategoryDto categoryToCategoryDto(Category category);
//    Category categoryDtoFromCategory(CategoryDto categoryDto);

    List<Product> toProductList(List<ProductDto> userDtoList);
    List<ProductDto> fromProductList(List<Product> userList);
}
