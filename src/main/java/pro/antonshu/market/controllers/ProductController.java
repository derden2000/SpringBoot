package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.services.CategoryService;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.utils.Basket;
import pro.antonshu.market.utils.ProductFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

@Controller
public class ProductController {

    private CategoryService categoryService;
    private ProductService productService;
    private Basket basket;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @GetMapping("/products")
    public String getProducts(Model model,
                              @RequestParam Map<String, String> params,
                              @CookieValue(name = "userHistory", required = false) Cookie userHistoryCookie,
                              HttpServletRequest request) {
        ReadHistoryCookies(model, request);

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

    @GetMapping("/product/{id}")
    private String getCurrentProduct(Model model,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Principal principal, @PathVariable Long id) {
        WriteHistoryCookie(request, id);
        Product product = productService.getProductById(id);
        model.addAttribute(product);
        model.addAttribute(basket);
        return "product";
    }

    private void WriteHistoryCookie(HttpServletRequest request, @PathVariable Long id) {
        if (request.getSession().getAttribute("userHistory") != null) {
            Cookie historyCookie = (Cookie) request.getSession().getAttribute("userHistory");
            String[] visitedProducts = new StringBuilder(historyCookie.getValue()).toString().split("--");
            StringBuilder builder = new StringBuilder();
            if (visitedProducts.length > 4) {
                for (int i = 1; i < visitedProducts.length; i++) {
                    builder.append(visitedProducts[i]).append("--");
                }
                builder.append(productService.getProductById(id).getTitle());
            } else {
                for (int i = 0; i < visitedProducts.length; i++) {
                    builder.append(visitedProducts[i]).append("--");
                }
                builder.append(productService.getProductById(id).getTitle());
            }
            historyCookie.setValue(builder.toString());
            request.getSession().setAttribute("userHistory", historyCookie);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(productService.getProductById(id).getTitle());
            Cookie historyCookie = new Cookie("history", builder.toString());
            request.getSession().setAttribute("userHistory", historyCookie);
        }
    }

    private void ReadHistoryCookies(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userHistory") != null) {
            Cookie historyCookie = (Cookie) session.getAttribute("userHistory");
            String input = historyCookie.getValue();
            String[] income = input.split("--");
            model.addAttribute("pageHistory", income);
        }
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
