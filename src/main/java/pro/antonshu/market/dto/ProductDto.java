package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.market.entities.Category;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {

    private String title;
    private String description;
    private CategoryDto categoryDto;
    private BigDecimal price;
}
