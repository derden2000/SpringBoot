package pro.antonshu.market.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.market.utils.Basket;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @Column(name = "price")
    private BigDecimal amount;

    @Column(name = "address")
    private String address;

    public Order(User user, Basket basket) {
        this.user = user;
        this.amount = BigDecimal.valueOf(basket.getTotalCost());
        this.items = new ArrayList<>();
    }

    public Order(User user, List<OrderItem> items, BigDecimal amount, String address) {
        this.user = user;
        this.items = items;
        this.amount = amount;
        this.address = address;
    }
}
