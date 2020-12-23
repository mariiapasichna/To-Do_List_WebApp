package com.mariiapasichna.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class LoginIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "mike@mail.com", password = "Qwerty11")
    public void getLoginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/form-login"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"))
                .andExpect(authenticated());

        mockMvc.perform(formLogin().user("mike@mail.com").password("invalid"))
                .andExpect(unauthenticated());

        mockMvc.perform(logout())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(unauthenticated());
    }
}