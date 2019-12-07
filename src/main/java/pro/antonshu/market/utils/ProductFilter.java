package pro.antonshu.market.utils;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.repositories.ProductSpecification;

import java.util.Map;

@Getter
public class ProductFilter {

    private Specification<Product> spec;
    private StringBuilder filterDefinition;
    private StringBuilder filterDefinitionWithoutPaging;


    public ProductFilter(Map<String, String> map) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();
        this.filterDefinitionWithoutPaging = new StringBuilder();

        if (map.containsKey("min_price") && !map.get("min_price").isEmpty()) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEq(Integer.parseInt(map.get("min_price"))));
            filterDefinition.append("&min_price=").append(Integer.parseInt(map.get("min_price")));
            filterDefinitionWithoutPaging.append("&min_price=").append(Integer.parseInt(map.get("min_price")));
        }
        if (map.containsKey("max_price") && !map.get("max_price").isEmpty()) {
            spec = spec.and(ProductSpecification.priceLesserThanOrEq(Integer.parseInt(map.get("max_price"))));
            filterDefinition.append("&max_price=").append(Integer.parseInt(map.get("max_price")));
            filterDefinitionWithoutPaging.append("&max_price=").append(Integer.parseInt(map.get("max_price")));
        }
        if (map.containsKey("category") && !map.get("category").isEmpty()) {
            spec = spec.and(ProductSpecification.getProductsByCategory(Integer.parseInt(map.get("category"))));
            filterDefinition.append("&category=").append(Integer.parseInt(map.get("category")));
            filterDefinitionWithoutPaging.append("&category=").append(Integer.parseInt(map.get("category")));
        }
        if (map.containsKey("page_values_num") && !map.get("page_values_num").isEmpty()) {
            filterDefinition.append("&page_values_num=").append(Integer.parseInt(map.get("page_values_num")));
            filterDefinitionWithoutPaging.append("&page_values_num=").append(Integer.parseInt(map.get("page_values_num")));
        }
        if (map.containsKey("sort_by") && !map.get("sort_by").isEmpty()) {
            filterDefinition.append("&sort_by=").append(map.get("sort_by"));
            filterDefinitionWithoutPaging.append("&sort_by=").append(map.get("sort_by"));
        }
        if (map.containsKey("pageIndex") && !map.get("pageIndex").isEmpty()) {
            filterDefinition.append("&pageIndex=").append(Integer.parseInt(map.get("pageIndex")));
        }
    }
}
