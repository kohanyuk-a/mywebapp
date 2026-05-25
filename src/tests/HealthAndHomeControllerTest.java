package com.example.mywebapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HealthAndHomeControllerTest {


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private HealthController healthController;

    private MockMvc healthMvc;

    @BeforeEach
    void setUp() {
        healthMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    @DisplayName("GET /health/alive → 200 OK")
    void alive_returns200() throws Exception {
        healthMvc.perform(get("/health/alive"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verifyNoInteractions(jdbcTemplate);
    }

    @Test
    @DisplayName("GET /health/ready → 200 OK, виконує SELECT 1")
    void ready_returns200_whenDbUp() throws Exception {
        when(jdbcTemplate.queryForObject(eq("SELECT 1"), eq(Integer.class))).thenReturn(1);

        healthMvc.perform(get("/health/ready"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verify(jdbcTemplate).queryForObject("SELECT 1", Integer.class);
    }

    @Test
    @DisplayName("GET /health/ready → 500, якщо БД недоступна")
    void ready_returns500_whenDbDown() throws Exception {
        when(jdbcTemplate.queryForObject(eq("SELECT 1"), eq(Integer.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        healthMvc.perform(get("/health/ready"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET / → 200 з HTML-сторінкою та всіма ендпоінтами")
    void home_returns200WithHtml() throws Exception {
        MockMvc homeMvc = MockMvcBuilders
                .standaloneSetup(new HomeController())
                .build();

        homeMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(containsString("MyWebApp")))
                .andExpect(content().string(containsString("GET /notes")))
                .andExpect(content().string(containsString("POST /notes")))
                .andExpect(content().string(containsString("GET /notes/{id}")))
                .andExpect(content().string(containsString("GET /health/alive")))
                .andExpect(content().string(containsString("GET /health/ready")));
    }
}