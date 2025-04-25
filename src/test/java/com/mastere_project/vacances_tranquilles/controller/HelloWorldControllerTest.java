package com.mastere_project.vacances_tranquilles.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.mastere_project.vacances_tranquilles.controller.HelloWorldController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitaire pour le contrôleur HelloWorldController.
 */
@WebMvcTest(HelloWorldController.class)
@AutoConfigureMockMvc
public class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sayHello_shouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/api/test/hello"))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello World!!"));
    }
}
