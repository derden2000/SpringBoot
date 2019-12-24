package pro.antonshu.market.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import pro.antonshu.market.services.ProductService;

import java.util.HashMap;
import java.util.Map;

/*
 *  Корзина представлена в виде Map c id товара и количесвом единиц товара в корзине.
 *  Key     - Long (id товара);
 *  Value   - Integer(кол-во единиц товара);
 */

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Basket {

    private Map<Long, Integer> content;
    private ProductService productService;

    public Basket(ProductService productService) {
        this.content = new HashMap<>();
        this.productService = productService;
    }

    public boolean add(Long key, Integer value) {
        content.put(key, value);
        return true;
    }

    public boolean del(Long key) {
        content.remove(key);
        return true;
    }

    public Map<Long, Integer> getContent() {
        return content;
    }

    public boolean contains(Long key) {
        return content.containsKey(key);
    }

    public int size() {
        return content.size();
    }

    public Long getTotalCost() {
        Long[] res = {0L};
        content.forEach((k, v) -> res[0] += productService.getProductById(k).getPrice().longValue() * v);
        return res[0];
    }

    public Long getCostOfProduct(Long id) {
        return productService.getProductById(id).getPrice().longValue() * content.get(id);
    }
}
