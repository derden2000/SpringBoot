package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.entities.Mail;
import pro.antonshu.market.entities.PasswordDto;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.RoleRepository;
import pro.antonshu.market.services.MailService;
import pro.antonshu.market.services.SecurityService;
import pro.antonshu.market.services.UserService;
import pro.antonshu.market.services.UserServiceImpl;
import pro.antonshu.market.utils.Basket;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@Controller
public class UserController {

    private UserService userService;
    private Basket basket;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private MailService mailService;
    private SecurityService securityService;

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

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
    public String regNewUser(@ModelAttribute(name = "user") @Valid User user) {
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/check_login")
    public Boolean checkLogin(@RequestParam(name = "phone") String phone) {
        return userService.isUserExist(phone);
    }

    @GetMapping("/recovery_password")
    public String passRecovery(Model model) {
        model.addAttribute(basket);
        return "pass_recovery";
    }

    @PostMapping("/recovery_password_do")
    public String passRecoveryDo(Model model, @RequestParam(name = "phone") String phone) {
        User user = userService.findByPhone(phone);
        Mail mail = new Mail();
        mail.setMailFrom("Интернет-магазин Anton.Shu <shop@antonshu.pro>");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("antonshu.pro - password recovery");
        String token = UUID.randomUUID().toString();
        String url = "/user/changePassword?id=" +
                user.getId() + "&token=" + token;
        StringBuilder mailBody = new StringBuilder();
        mailBody.append("Learn How to reset password using Spring Boot!!!\n\n")
                .append("For reset you pass go to: https://antonshu.pro:8023/app")
                .append(url);
        userService.createPasswordResetTokenForUser(user, token);
        mail.setMailContent(mailBody.toString());
        mailService.sendEmail(mail);
        model.addAttribute(basket);
        return "pass_ok";
    }

    @GetMapping("/user/changePassword")
    public String showChangePasswordPage(Model model,
                                         @RequestParam("id") long id,
                                         @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute(basket);
            model.addAttribute("reason", result);
            return "redirect:pass_fail?reason=" + result;
        }
        return "redirect:updatePassword";
    }

    @GetMapping("/user/updatePassword")
    public String updatePasswordPage(Model model) {
            model.addAttribute(basket);
        return "updatePassword";
    }

    @GetMapping("/user/pass_fail")
    public String updatePasswordFailPage(Model model, @RequestParam("reason") String reason) {
        model.addAttribute(basket);
        model.addAttribute("reason", reason);
        return "pass_fail";
    }

    @PostMapping("/user/savePassword")
    public String savePassword(@Valid PasswordDto passwordDto,
                               Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        model.addAttribute(basket);
        return "change_pass_ok";
    }
}
