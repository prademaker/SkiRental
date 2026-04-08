package nl.miwnn.ch19.paul.skirental.controller;

import nl.miwnn.ch19.paul.skirental.config.SkiRentalSecurityConfig;
import nl.miwnn.ch19.paul.skirental.model.Ski;
import nl.miwnn.ch19.paul.skirental.service.RentalUserService;
import nl.miwnn.ch19.paul.skirental.service.SkiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkiController.class)
@Import(SkiRentalSecurityConfig.class) // Importeer je echte security instellingen
public class SkiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SkiService skiService;

    // Omdat je SkiRentalSecurityConfig een RentalUserService injecteert in de constructor,
    // MOET je deze hier mocken, anders start de context niet op.
    @MockitoBean
    private RentalUserService rentalUserService;

    // Voeg deze inner class toe om Thymeleaf te helpen met de sec: tags
    @TestConfiguration
    static class TestConfig {
        @Bean
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    void testShowSkiOverview() throws Exception {
        // GIVEN
        List<Ski> mockSkis = List.of(
                new Ski("Rossignol", "Hero"),
                new Ski("Völkl", "Racetiger")
        );
        Mockito.when(skiService.getSkis(null)).thenReturn(mockSkis);

        // WHEN & THEN
        mockMvc.perform(get("/ski/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("ski"))
                .andExpect(model().attributeExists("skis"))
                .andExpect(model().attribute("skis", hasSize(2)));
    }
}