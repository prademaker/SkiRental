package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.dto.NewRentalUserDTO;
import nl.miwnn.ch19.paul.skirental.mapper.RentalUserMapper;
import nl.miwnn.ch19.paul.skirental.model.RentalUser;
import nl.miwnn.ch19.paul.skirental.repository.UserRepository;
import nl.miwnn.ch19.paul.skirental.service.RentalUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalUserController.class)
@AutoConfigureMockMvc(addFilters = false) // Schakelt security filters uit (geen 302 naar login)
public class RentalUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RentalUserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private RentalUserService rentalUserService;

    // DE FIX: Deze configuratie zorgt dat Thymeleaf de sec: tags begrijpt
    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }

    @Test
    void testShowUserOverview() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/overview"))
                .andExpect(model().attributeExists("users", "newUser"));
    }

    @Test
    void testAddUserSuccess() throws Exception {
        // Zorg dat de mapper niet null teruggeeft
        Mockito.when(userMapper.toRentalUser(any(NewRentalUserDTO.class), any(PasswordEncoder.class)))
                .thenReturn(new RentalUser());

        mockMvc.perform(post("/users/add")
                        .param("username", "paul")
                        .param("plainPassword", "geheim123")
                        .param("confirmPassword", "geheim123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void testAddUserPasswordsDoNotMatch() throws Exception {
        // We sturen expres twee verschillende wachtwoorden
        mockMvc.perform(post("/users/add")
                        .param("username", "paul")
                        .param("plainPassword", "geheim123")
                        .param("confirmPassword", "foutje"))
                .andExpect(status().isOk()) // Het formulier wordt opnieuw getoond (status 200)
                .andExpect(view().name("users/add-user"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("newUser", "confirmPassword"));
    }
}