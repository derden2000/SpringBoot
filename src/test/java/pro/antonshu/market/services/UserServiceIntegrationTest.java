package pro.antonshu.market.services;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.UserRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceIntegrationTest {

//    @TestConfiguration
//    static class EmployeeServiceImplTestContextConfiguration {
//
//        @Bean
//        public UserService userService() {
//            return new UserServiceImpl();
//        }
//    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;




    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(44L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("123@123.ru");
        user.setPhone("12345678");

        Mockito.when(userRepository.findOneByPhone(user.getPhone()))
                .thenReturn(user);
        Mockito.when(userRepository.findOneById(user.getId()))
                .thenReturn(user);
        Mockito.when(userRepository.existsByPhone(user.getPhone()))
                .thenReturn(true);
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
    }

    @Test
    void findByPhone() {
        String phone = "12345678";
        User found = userService.findByPhone(phone);

        assertThat(found.getPhone()).isEqualTo(phone);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByPhone("12345678");
    }

    @Test
    void findById() {
        Long id = 44L;
        User found = userService.findById(id);

        assertThat(found.getId()).isEqualTo(id);
        Mockito.verify(userRepository, Mockito.times(1)).findOneById(id);
    }

    @Test
    void isUserExist() {
        String phone = "12345678";
        Boolean found = userService.isUserExist(phone);

        assertThat(found).isEqualTo(true);
        Mockito.verify(userRepository, Mockito.times(1)).existsByPhone("12345678");
    }

    @Test
    void getAllUsers() {
        assertThat(userService.getAllUsers()).isNotNull();
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void saveUser() {
        User user = new User();
        user.setId(44L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("123@123.ru");
        user.setPhone("12345678");

        assertThat(userService.save(user)).isEqualTo(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
}