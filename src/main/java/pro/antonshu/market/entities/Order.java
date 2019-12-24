package pro.antonshu.market.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pro.antonshu.market.utils.Basket;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Data
@ToString(exclude = {"items"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date date;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @Column(name = "price")
    private BigDecimal amount;

    @Column(name = "address")
    private String address;

    @Column(name = "complete_status", columnDefinition = "boolean DEFAULT false")
    private boolean completeStatus;

    public Order(User user, Basket basket) {
        this.user = user;
        this.amount = BigDecimal.valueOf(basket.getTotalCost());
        this.items = new ArrayList<>();
        for (OrderItem item : basket.getContent()) {
            items.add(item);
        }
    }

    public Order(User user, List<OrderItem> items, BigDecimal amount, String address) {
        this.user = user;
        this.items = items;
        this.amount = amount;
        this.address = address;
    }
}
