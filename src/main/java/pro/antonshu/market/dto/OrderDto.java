package pro.antonshu.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.market.entities.User;

@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private UserDto userDto;
    private boolean completeStatus;
    private boolean paymentStatus;

    public OrderDto(Long id, boolean completeStatus) {
        this.id = id;
        this.completeStatus = completeStatus;
    }
}
