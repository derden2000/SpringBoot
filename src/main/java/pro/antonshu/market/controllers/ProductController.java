package pro.antonshu.market.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.market.entities.Group;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.entities.Review;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.services.*;
import pro.antonshu.market.utils.Basket;
import pro.antonshu.market.utils.ProductFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProductController {

    private CategoryService categoryService;
    private GroupService groupService;
    private ProductService productService;
    private Basket basket;
    private ReviewService reviewService;
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
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
        String groupId = null;
        if (params.containsKey("group") && !params.get("group").isEmpty()) {
            groupId = params.get("group");
        }

        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageValuesNum, Sort.Direction.ASC, sortBy);
        ProductFilter productFilter = new ProductFilter(params);
        Page<Product> page = productService.findAll(productFilter.getSpec(), pageRequest);

        createContent(page, model);
        model.addAttribute("basket", basket);
        model.addAttribute("filtersDef", productFilter.getFilterDefinition());
        model.addAttribute("filtersDefWP", productFilter.getFilterDefinitionWithoutPaging());
        if (groupId != null) {
            model.addAttribute("group", groupService.getGroupById(groupId));
            Group group = groupService.getGroupById(groupId);
            System.out.println();
            System.out.println("Categories of group: " + group.getCategories());
            System.out.println();
            model.addAttribute("categories", group.getCategories());
        } else {
            model.addAttribute("group", groupService.getAllGroups());
        }

        return "products_list";
    }

    @GetMapping("/product")
    private String getCurrentProduct(Model model,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Principal principal,
                                     @RequestParam("productId") Long id) {
        WriteHistoryCookie(request, id);
        Product product = productService.getProductById(id);
        final Boolean[] userCanWriteReview = {false};

        if (principal != null) {
            User user = userService.findByPhone(principal.getName());
            model.addAttribute("user", user);
            orderService.findAllOrdersByUserId(user.getId()).forEach(order -> order.getItems().
                    forEach(orderItem -> {
                        if (orderItem.getProduct().getId() == id && !reviewService.alreadyWrittenByUser(user, product)) {
                            userCanWriteReview[0] = true;
                        }
                    }));
        }
        List<Review> reviews = reviewService.findAllByProductId(product.getId());
        double avMark = reviews.stream().mapToDouble(Review::getScore).sum() / reviews.size();
        model.addAttribute("averageMark", avMark);
        model.addAttribute("userCanWriteReview", userCanWriteReview[0]);
        model.addAttribute("product", product);
        model.addAttribute(basket);
        model.addAttribute("reviews", reviewService.findAllByProductId(product.getId()));
        return "product";
    }


    @PostMapping("/reviews")
    public String savePassword(@RequestParam("review") String reviewText,
                               @RequestParam("userId") Long userId,
                               @RequestParam("productId") Long productId,
                               @RequestParam("score") Integer score,
                               Model model) {
        Review review = new Review(productService.findOneById(productId), userService.findById(userId), reviewText, score);
        reviewService.save(review);
        String message = "Yor review was successfully posted";
        model.addAttribute("message", message);
        model.addAttribute(basket);
        return "blank";
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
//        model.addAttribute("groups", page.getContent());
        model.addAttribute("productsCount", page.getTotalElements());
        model.addAttribute("pagesCount", page.getTotalPages());
        model.addAttribute("next", page.nextOrLastPageable().getPageNumber());
        model.addAttribute("previous", page.previousOrFirstPageable().getPageNumber());
        model.addAttribute("page", page);
    }
}
