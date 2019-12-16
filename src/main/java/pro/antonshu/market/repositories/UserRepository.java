package pro.antonshu.market.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.antonshu.market.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByPhone(String phone);

    boolean existsByPhone(String phone);
}
