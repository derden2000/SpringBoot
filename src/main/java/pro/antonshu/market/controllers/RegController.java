package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pro.antonshu.market.entities.Role;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.RoleRepository;
import pro.antonshu.market.services.UserService;
import pro.antonshu.market.services.UserServiceImpl;
import pro.antonshu.market.utils.Basket;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegController {

    private UserService userService;
    private Basket basket;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public String regNewUser(Model model, @ModelAttribute(name = "user") @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findOneByName("ROLE_CUSTOMER")));
        userService.regNewUser(user);
        return "redirect:products";
    }

    @GetMapping("/register")
    public String registration(Model model) {
        User user_new = new User();
        model.addAttribute("user", user_new);
        model.addAttribute(basket);
        return "register";
    }
}
