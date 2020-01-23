package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.RoleRepository;
import pro.antonshu.market.services.OrderItemService;
import pro.antonshu.market.services.OrderService;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.services.UserService;
import pro.antonshu.market.utils.Basket;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;

@Controller
public class OrderController {

    private UserService userService;
    private OrderService orderService;
    private OrderItemService orderItemService;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private Basket basket;

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
    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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

    @PostMapping("/fast_order_do")
    public String startFastOrder(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "email") String email,
                                 Model model) {
        User user = new User();
        user.setFirstName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123"));
        user.setRoles(Collections.singleton(roleRepository.findOneByName("ROLE_CUSTOMER")));
        Order order = new Order(user, basket);
        order.setDate(new Date());
        order.setAddress("address");
        order.setItems(basket.getContent());
        userService.save(user);
        orderService.saveOrder(order);
        orderItemService.saveOrderItemList(basket.getContent(), order);
        basket.clear();
        model.addAttribute("order", order);
        model.addAttribute(basket);
        model.addAttribute("user", user);
        return "fast_order_do";
    }
}
