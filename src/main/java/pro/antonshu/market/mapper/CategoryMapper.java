package pro.antonshu.market.mapper;

import org.mapstruct.Mapper;
import pro.antonshu.market.dto.CategoryDto;
import pro.antonshu.market.entities.Category;

@Mapper/*(componentModel = "spring", uses = ProductMapper.class)*/
public interface CategoryMapper {


    CategoryDto categoryToCategoryDto(Category category);
    Category categoryDtoFromCategory(CategoryDto categoryDto);
}
