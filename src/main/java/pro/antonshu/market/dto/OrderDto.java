package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private boolean completeStatus;
    private UserDto userDto;
    private boolean paymentStatus;


    public OrderDto(Long id, boolean completeStatus) {
        this.id = id;
        this.completeStatus = completeStatus;
    }
}