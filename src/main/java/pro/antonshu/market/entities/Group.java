package pro.antonshu.market.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "groups")
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "group")
    private List<Product> productsOfGroup;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groups_categories",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Collection<Category> categories;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Collection<Category> getCategories() {
        return categories;
    }
}