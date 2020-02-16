package pro.antonshu.market.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pro.antonshu.market.dto.OrderDto;
import pro.antonshu.market.dto.UserDto;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.User;

import java.util.List;

@Mapper(/*componentModel = "spring",*/ uses = {UserMapper.class})
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    //    @Mappings({
    @Mapping(source = "userDto", target = "user")
//    @Mapping(source = "productDto", target = "product")})
//    @InheritInverseConfiguration
    Order toOrder(OrderDto orderDto);

//    @InheritInverseConfiguration
    @Mapping(source = "user", target = "userDto")
    OrderDto fromOrder(Order order);

    List<Order> toOrderList(List<OrderDto> orderDtoList);
    List<OrderDto> fromOrderList(List<Order> orderList);

//    UserDto fromUser(User user);
//    User toUser(UserDto userDto);

}
