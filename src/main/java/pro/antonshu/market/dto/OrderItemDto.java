package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.market.mapper.ProductMapper;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private Long id;
    private ProductDto productDto;
    private int quantity;
    private BigDecimal price;
    private OrderDto orderDto;
}
