package pro.antonshu.market.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import pro.antonshu.market.dto.CategoryDto;
import pro.antonshu.market.dto.ProductDto;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.services.CategoryService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperTest {

    private ProductMapper mapper
            = Mappers.getMapper(ProductMapper.class);
    @Test
    public void givenSourceToDestination_whenMaps_thenCorrect() {
        Product source = new Product();
        source.setTitle("SourceTitle");
        source.setDescription("SourceDescription");
        source.setCategory(new Category(1L, "First"));
        source.setPrice(new BigDecimal(100));
        ProductDto destination = mapper.fromProduct(source);

        assertEquals(source.getTitle(), destination.getTitle());
        assertEquals(source.getDescription(), destination.getDescription());
        assertEquals(source.getCategory().getTitle(), destination.getCategoryDto().getTitle());
        assertEquals(source.getPrice(), destination.getPrice());
    }
    @Test
    public void givenDestinationToSource_whenMaps_thenCorrect() {
        ProductDto destination = new ProductDto();
        destination.setTitle("DestinationTitle");
        destination.setDescription("DestinationDescription");
        destination.setCategoryDto(new CategoryDto("Sample"));
        destination.setPrice(new BigDecimal(100));
        Product source = mapper.ToProduct(destination);
        assertEquals(destination.getTitle(), source.getTitle());
        assertEquals(destination.getDescription(), source.getDescription());
        assertEquals(destination.getCategoryDto().getTitle(), source.getCategory().getTitle());
        assertEquals(destination.getPrice(), source.getPrice());
    }
}
