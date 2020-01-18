package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.services.UserService;
import pro.antonshu.market.utils.Basket;

import java.security.Principal;
import java.util.List;

@Controller
public class ControlPanelController {

    private UserService userService;
    private Basket basket;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBasket(Basket basket) {
        this.basket = basket;
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
}
