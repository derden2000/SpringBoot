package pro.antonshu.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.entities.Review;
import pro.antonshu.market.entities.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);

    @Transactional
    @Query("select r from Review r where r.user=?1 and r.product=?2")
    List<Review> alreadyWrittenByUser(User user, Product product);
}
