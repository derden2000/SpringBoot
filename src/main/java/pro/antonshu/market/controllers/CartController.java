package pro.antonshu.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.utils.Basket;

@Controller
public class CartController {

    private ProductService productService;
    private Basket basket;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setBasket(Basket basket) {
        this.basket = basket;
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
}
