package pro.antonshu.market.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pro.antonshu.market.controllers.ControlPanelController;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SecuritySpringBootIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(authorities="ROLE_ADMIN")
    @Test
    public void authRequestOnSecuredUrlsWithRoleAdmin_thenOk() throws Exception {
        mvc.perform(get("/admin")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isOk());
        mvc.perform(get("/edit/2")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities="ROLE_MANAGER")
    @Test
    public void authRequestOnSecuredUrlWithRoleManager_thenOk() throws Exception {
        mvc.perform(get("/admin")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isOk());
        mvc.perform(get("/edit/2")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities="ROLE_CUSTOMER")
    @Test
    public  void authRequestOnSecuredUrlWithRoleCustomer_thenFalse() throws Exception {
        mvc.perform(get("/admin")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isForbidden());
        mvc.perform(get("/edit/2")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().isForbidden());
    }

    @Test
    public  void unAuthRequestOnSecuredUrl_thenRedirect() throws Exception {
        mvc.perform(get("/profile")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().is3xxRedirection());
        mvc.perform(get("/order")/*.contentType(MediaType.APPLICATION_JSON)*/)
                .andExpect(status().is3xxRedirection());
    }
}
