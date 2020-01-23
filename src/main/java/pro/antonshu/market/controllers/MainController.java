package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.services.CategoryService;
import pro.antonshu.market.services.OrderService;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.services.UserService;
import pro.antonshu.market.utils.Basket;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private ProductService productService;
    private CategoryService categoryService;
    private UserService userService;
    private OrderService orderService;

    private Basket basket;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(basket);
        return "index";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute(basket);
        List<Order> orderList = orderService.findAllOrdersByUserId(user.getId());
        System.out.println("orderList: " + orderList);
        model.addAttribute("orders_list", orderList);
        return "profile";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(Model model, @PathVariable Long id) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "edit_product";
    }

    @PostMapping("/edit")
    public String saveItem(@ModelAttribute(name = "product") Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }
}
