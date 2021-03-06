package pro.antonshu.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Group;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findOneByTitle(String title);

    boolean existsByTitle(String title);

    List<Category> findAllByGroup(Group group);
}
