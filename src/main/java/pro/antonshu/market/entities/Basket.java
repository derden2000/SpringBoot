package pro.antonshu.market.entities;

import java.util.HashMap;
import java.util.Map;

public class Basket {

    private Map<Integer, Long> content;

    public Basket() {
        this.content = new HashMap<>();
    }

    public boolean add(Integer key, Long value) {
        content.put(key, value);
        return true;
    }

    public boolean del(Integer key) {
        content.remove(key);
        return true;
    }

    public Map<Integer, Long> getContent() {
        return content;
    }

    public boolean contains(Integer key) {
        return content.containsKey(key);
    }

    public int size() {
        return content.size();
    }

    public Long getTotalCost() {
        Long[] res = {0L};
        content.forEach((k, v) -> res[0] += v);
        return res[0];
    }
}
