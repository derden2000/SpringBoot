package pro.antonshu.market;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import pro.antonshu.market.controllers.*;
import pro.antonshu.market.utils.Basket;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing for session bean @Basket
 * and notNull other Controllers.
 */

@SpringBootTest
@WebAppConfiguration
class MarketApplicationTests {
    @Autowired
    private MainController mainController;
    @Autowired
    private OrderController orderController;
    @Autowired
    private CartController cartController;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private ProductController productController;
    @Autowired
    private UserController userController;

    @Autowired
    private Basket basket;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    MockHttpSession session;

    @Test
    void contextLoads() {
        assertThat(mainController)
                .isNotNull();
        assertThat(basket)
                .isSameAs(wac.getBean("basket", Basket.class));

        assertThat(orderController)
                .isNotNull();
        assertThat(cartController)
                .isNotNull();
        assertThat(paymentController)
                .isNotNull();
        assertThat(productController)
                .isNotNull();
        assertThat(userController)
                .isNotNull();
    }
}