package pro.antonshu.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Group;
import pro.antonshu.market.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByGroup(Group group);

    List<Product> findAllByTitle(String title);
}
