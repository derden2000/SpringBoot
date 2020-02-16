package pro.antonshu.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.antonshu.market.dto.UserDto;
import pro.antonshu.market.entities.User;

//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import pro.antonshu.market.dto.UserDto;
//import pro.antonshu.market.entities.User;
//
//import java.util.List;
//
@Mapper/*(componentModel = "spring", uses = {OrderMapper.class})*/
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto fromUser(User user);
    User toUser(UserDto userDto);

//    List<User> toUserList(List<UserDto> userDtoList);
//
//    List<UserDto> fromUserList(List<User> userList);

}
