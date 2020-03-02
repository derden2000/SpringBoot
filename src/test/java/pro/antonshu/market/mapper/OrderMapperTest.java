package pro.antonshu.market.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pro.antonshu.market.dto.OrderDto;
import pro.antonshu.market.dto.UserDto;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void toOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCompleteStatus(false);
        orderDto.setPaymentStatus(true);
        orderDto.setUserDto(new UserDto(2L, "phone", "firstName", "lastName", "mail"));
        Order order = mapper.toOrder(orderDto);

        assertEquals(orderDto.getId(), order.getId());
        assertEquals(orderDto.isCompleteStatus(), order.isCompleteStatus());
        assertEquals(orderDto.isPaymentStatus(), order.isPaymentStatus());
        assertEquals(orderDto.getUserDto().getEmail(), order.getUser().getEmail());
        assertEquals(orderDto.getUserDto().getPhone(), order.getUser().getPhone());
        assertEquals(orderDto.getUserDto().getFirstName(), order.getUser().getFirstName());
        assertEquals(orderDto.getUserDto().getLastName(), order.getUser().getLastName());
    }

    @Test
    void fromOrder() {
        User user = new User();
        user.setPhone("123567");
        user.setEmail("sdferf");
        Order order = new Order();
        order.setId(34L);
        order.setCompleteStatus(false);
        order.setPaymentStatus(false);
        order.setDate(new Date());
        order.setUser(user);
        OrderDto orderDto = mapper.fromOrder(order);

        assertEquals(orderDto.getId(), order.getId());
        assertEquals(orderDto.isPaymentStatus(), order.isPaymentStatus());
        assertEquals(orderDto.isCompleteStatus(), order.isCompleteStatus());
        assertEquals(orderDto.getUserDto().getPhone(), order.getUser().getPhone());
        assertEquals(orderDto.getUserDto().getEmail(), order.getUser().getEmail());
    }

    @Test
    void toOrderList() {
    }

    @Test
    void fromOrderList() {
    }
}