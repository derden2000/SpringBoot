package pro.antonshu.market.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pro.antonshu.market.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByPhone(String phone);

    boolean isUserExist(String phone);

    List<User> getAllUsers();

    User regNewUser(User user);
}
