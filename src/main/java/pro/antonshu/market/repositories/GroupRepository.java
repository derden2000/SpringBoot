package pro.antonshu.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findOneByTitle(String title);

    Group findOneById(Long Id);

    boolean existsByTitle(String title);

//    Group findOneByCategory(Long categoryId);
}
