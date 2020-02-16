package pro.antonshu.market.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import pro.antonshu.market.utils.Basket;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CartControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CartController cartController;

    @MockBean
    private Basket basket;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void addProductPost() throws Exception {
        String idValue = "1";
        String quantityValue = "2";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", idValue);
        params.add("quantity", quantityValue);

        mockMvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(content().string(containsString(idValue)))
                .andDo(print());

        Mockito.verify(basket, Mockito.times(1)).add(Long.parseLong(idValue), Integer.parseInt(quantityValue));
    }

    @Test
    public void cartCount() throws Exception {

        final MockHttpServletResponse response = mockMvc.perform(post("/cart_count_request")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(Integer.parseInt(response.getContentAsString()), greaterThanOrEqualTo(0));

        Mockito.verify(basket, Mockito.times(1)).size();
    }
}
