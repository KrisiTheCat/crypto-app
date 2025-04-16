package com.krisi.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisi.crypto.model.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiTests {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper =  new ObjectMapper();

    @Test
    void returnsAllUsers() throws Exception {
        this.mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void returnsUserInfo() throws Exception {
        this.mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void resetsUser() throws Exception {
        this.mockMvc.perform(post("/api/users/reset/1"))
                .andExpect(status().isOk());
    }

    @Test
    void resetsNonExistentUser() throws Exception {
        this.mockMvc.perform(post("/api/users/reset/-1"))
                .andExpect(status().isBadRequest());
    }

}