package pro.antonshu.market.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pro.antonshu.market.entities.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private String firstName = "firstName";
    private String lastName = "lastName";
    private String email = "123@123.ru";
    private String phone = "12345678";

    @BeforeEach
    void setUp() {
        User user = new User();
//        user.setId(44L);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findOneByPhone() {
        User found = userRepository.findOneByPhone(phone);

        assertThat(found.getPhone()).isEqualTo(phone);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getFirstName()).isEqualTo(firstName);
        assertThat(found.getLastName()).isEqualTo(lastName);
    }

//    @Test
//    void findOneById() {
//        User found = userRepository.findOneById(1L);
//        System.out.println("User: " + found);
//
////        assertThat(found.getId()).isNotNull();
//        assertThat(found.getPhone()).isNotNull();
//        assertThat(found.getEmail()).isNotNull();
//        assertThat(found.getFirstName()).isNotNull();
//        assertThat(found.getLastName()).isNotNull();
//    }

    @Test
    void existsByPhone() {
        assertThat(userRepository.existsByPhone("12345678")).isEqualTo(true);
    }
}