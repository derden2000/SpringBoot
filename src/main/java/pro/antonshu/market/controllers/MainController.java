package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.entities.Basket;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.services.CategoryService;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.utils.ProductFilter;

import javax.annotation.PostConstruct;
import java.util.Map;

@Controller
public class MainController {

    private ProductService productService;
    private CategoryService categoryService;

    private Basket basket;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void init() {
        this.basket = new Basket();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/cart")
    public String Cart(Model model) {
        model.addAttribute("basket", basket);
        model.addAttribute("sr", productService);
        return "cart";
    }

    @GetMapping("/products")
    public String getProducts(Model model, @RequestParam Map<String, String> params) {

        int pageIndex = 1;
        if (params.containsKey("pageIndex")) {
            pageIndex = Integer.parseInt(params.get("pageIndex"));
        }
        int pageValuesNum = 5;
        if (params.containsKey("page_values_num") && !params.get("page_values_num").isEmpty()) {
            pageValuesNum = Integer.parseInt(params.get("page_values_num"));
        }
        String sortBy = "id";
        if (params.containsKey("sort_by") && !params.get("sort_by").isEmpty()) {
            sortBy = params.get("sort_by");
        }

        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageValuesNum, Sort.Direction.ASC, sortBy);
        ProductFilter productFilter = new ProductFilter(params);
        Page<Product> page = productService.findAll(productFilter.getSpec(), pageRequest);

        createContent(page, model);
        model.addAttribute("basket", basket);
        model.addAttribute("filtersDef", productFilter.getFilterDefinition());
        model.addAttribute("filtersDefWP", productFilter.getFilterDefinitionWithoutPaging());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "products_list";
    }

    @PostMapping("/cart")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Integer addProduct(@RequestParam(value = "id") Integer id, @RequestParam(value = "price") Long price) {
        System.out.println("Product added with id: " + id);
        basket.add(id, price);
        return id;
    }

    @PostMapping("/cart_count_request")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Integer getCartCount() {
        return basket.size();
    }

    @PostMapping("/cart-del")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Integer delProduct(@RequestParam(value = "id") Integer id) {
        System.out.println("Product deleted with id: " + id);
        basket.del(id);
        return id;
    }

    private void createContent(Page<Product> page, Model model) {
        model.addAttribute("products", page.getContent());
        model.addAttribute("productsCount", page.getTotalElements());
        model.addAttribute("pagesCount", page.getTotalPages());
        model.addAttribute("next", page.nextOrLastPageable().getPageNumber());
        model.addAttribute("previous", page.previousOrFirstPageable().getPageNumber());
        model.addAttribute("page", page);
    }
}
