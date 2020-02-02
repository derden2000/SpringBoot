package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.entities.Review;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findAllByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Boolean alreadyWrittenByUser(User user, Product product) {
        return reviewRepository.alreadyWrittenByUser(user, product).size() > 0;
    }
}
