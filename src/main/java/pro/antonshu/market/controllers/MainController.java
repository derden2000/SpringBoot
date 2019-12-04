package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.services.ProductService;

@Controller
public class MainController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/products")
    public String getProducts(Model model,
                              @RequestParam(required = false, name = "min_price", defaultValue = "0") Integer minPrice,
                              @RequestParam(required = false, name = "max_price", defaultValue = "0") Integer maxPrice,
                              @RequestParam(required = false, name = "word") String word,
                              @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                              @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                              @RequestParam(name = "page_values_num", defaultValue = "5") Integer pageValuesNum
    ) {
        if (minPrice <= 0 && maxPrice <= 0) {
            createPageContent(productService.findAll(pageIndex, pageValuesNum, sortBy), model, pageIndex, pageValuesNum, sortBy, minPrice, maxPrice);
        } else if (minPrice > 0 && maxPrice <= 0) {
            createPageContent(productService.findAllByMinPrice(pageIndex, pageValuesNum, sortBy, minPrice),
                    model, pageIndex, pageValuesNum, sortBy, minPrice, maxPrice);
        } else if (minPrice <= 0 && maxPrice > 0) {
            createPageContent(productService.findAllByMaxPrice(pageIndex, pageValuesNum, sortBy, maxPrice),
                    model, pageIndex, pageValuesNum, sortBy, minPrice, maxPrice);
        } else if (minPrice > 0 && maxPrice > 0) {
            createPageContent(productService.findAllBetween(pageIndex, pageValuesNum, sortBy, minPrice, maxPrice),
                    model, pageIndex, pageValuesNum, sortBy, minPrice, maxPrice);
        }
        return "products_list";
    }

    private void createPageContent(Page<Product> page, Model model, int pageIndex, int pageValuesNum, String sortBy, int min_price, int max_price) {
        model.addAttribute("products", page.getContent());
        model.addAttribute("productsCount", page.getTotalElements());
        model.addAttribute("pagesCount", page.getTotalPages());
        model.addAttribute("next", page.nextOrLastPageable().getPageNumber());
        model.addAttribute("previous", page.previousOrFirstPageable().getPageNumber());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("page_values_num", pageValuesNum);
        model.addAttribute("min_price", min_price);
        model.addAttribute("max_price", max_price);
    }
}
