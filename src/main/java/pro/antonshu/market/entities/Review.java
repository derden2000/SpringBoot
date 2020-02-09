package pro.antonshu.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "score")
    @Range(min = 1, max = 5)
    private int score;

    public Review(Product product, User user, String reviewText, @Range(min = 1, max = 5) int score) {
        this.product = product;
        this.user = user;
        this.reviewText = reviewText;
        this.score = score;
    }
}
