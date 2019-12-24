package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.services.*;
import pro.antonshu.market.utils.Basket;
import pro.antonshu.market.utils.ProductFilter;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private ProductService productService;
    private CategoryService categoryService;
    private UserService userService;
    private OrderService orderService;
    private OrderItemService orderItemService;
    private BCryptPasswordEncoder passwordEncoder;

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
    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        this.basket = new Basket(productService);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(basket);
        return "index";
    }

    @GetMapping("/register")
    public String getRegPage(Model model) {
        User user_new = new User();
        model.addAttribute("user", user_new);
        model.addAttribute(basket);
        return "register";
    }

    @PostMapping("/register")
    public String regNewUser(Model model, @ModelAttribute(name = "user") @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.regNewUser(user);
        return "redirect:products";
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

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute(basket);
        return "admin";
    }

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        List<User> users = userService.getAllUsers();
        System.out.println("Users List: " + users);
        model.addAttribute("users", users);
        model.addAttribute(basket);
        return "users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(Model model, Principal principal, @PathVariable Long id) {
        if (principal == null) {
            return "redirect:/";
        }
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute(basket);
        return "edit_user";
    }

    @GetMapping("/cart")
    public String Cart(Model model) {
        model.addAttribute("basket", basket);
        model.addAttribute("sr", productService);
        return "cart";
    }

    @PostMapping("/cart")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Long addProduct(@RequestParam(value = "id") Long id, @RequestParam(value = "quantity") Integer quantity) {
        System.out.println("Product added with id: " + id);
        basket.add(id, quantity);
        return id;
    }

    @PostMapping("/cart_add")
    public String addProductQuantity(Model model, @RequestParam(value = "id") Long id, @RequestParam(value = "quantity") Integer quantity) {
        System.out.println("Product quantity changed with id: " + id);
        System.out.println("quantity: " + quantity);
        basket.add(id, quantity);
        System.out.println("Basket: " + basket.getContent());
        model.addAttribute(basket);
        return "redirect:/cart";
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
    public Long delProduct(@RequestParam(value = "id") Long id) {
        System.out.println("Product deleted with id: " + id);
        basket.del(id);
        return id;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/order")
    public String orderPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute(basket);
        return "order";
    }

    @PostMapping("/order_do")
    public String orderDone(@RequestParam(name = "address") String address, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute(basket);
        Order order = new Order(user, basket);
        order.setDate(new Date());
        order.setAddress(address);
        order.setItems(basket.getContent());
        orderService.saveOrder(order);
        model.addAttribute("order", order);
        orderItemService.saveOrderItemList(basket.getContent(), order);
        basket.clear();
        return "order_do";
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

    @GetMapping("/payments")
    public String getPaymentsPage(Model model) {
        model.addAttribute(basket);
        return "payments";
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
