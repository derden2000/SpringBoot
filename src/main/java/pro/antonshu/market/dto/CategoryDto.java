package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.market.entities.Product;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {

    private String title;

    public CategoryDto(String title) {
        this.title = title;
    }
}
