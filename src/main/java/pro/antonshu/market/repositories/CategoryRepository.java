package pro.antonshu.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.antonshu.market.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
