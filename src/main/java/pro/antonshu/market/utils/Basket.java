package pro.antonshu.market.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import pro.antonshu.market.entities.OrderItem;
import pro.antonshu.market.services.ProductService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Корзина представлена в виде Map c id товара и количесвом единиц товара в корзине.
 *  Key     - Long (id товара);
 *  Value   - Integer(кол-во единиц товара);
 */

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Basket {

    private ProductService productService;
    private List<OrderItem> items;

    public Basket(ProductService productService) {
        this.items = new ArrayList<>();
        this.productService = productService;
    }

    public void clear() {
        items.clear();
    }

    public boolean add(Long id, Integer quantity) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(id)) {
                i.setQuantity(quantity);
                return true;
            }
        }
        items.add(new OrderItem(productService.getProductById(id)));
        return true;
    }

    public boolean del(Long id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId().equals(id)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public int size() {
        return items.size();
    }

    public boolean contains(Long id) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Long getCostOfProduct(Long id) {
        Long[] sum = {0L};
        items.stream().filter(x -> x.getProduct().getId().equals(id)).forEach(k -> sum[0] += k.getPrice().longValue() * k.getQuantity());
        return sum[0];
    }

    public Long getTotalCost() {
        Long[] sum = {0L};
        items.stream().forEach(k -> sum[0] += k.getPrice().longValue() * k.getQuantity());
        return sum[0];
    }

    public List<OrderItem> getContent() {
        return items;
    }
}
