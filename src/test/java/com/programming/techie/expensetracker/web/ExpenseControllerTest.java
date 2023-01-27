package com.programming.techie.expensetracker.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.techie.expensetracker.dto.ExpenseDto;
import com.programming.techie.expensetracker.exception.ExpenseNotFoundException;
import com.programming.techie.expensetracker.model.ExpenseCategory;
import com.programming.techie.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ExpenseController.class)
class ExpenseControllerTest {

    @MockBean
    private ExpenseService expenseService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should Create Expense")
    void shouldCreateExpense() throws Exception {
        ExpenseDto expenseDto = ExpenseDto.builder()
                .expenseCategory(ExpenseCategory.ENTERTAINMENT)
                .expenseName("Movies")
                .expenseAmount(BigDecimal.TEN)
                .build();

        Mockito.when(expenseService.addExpense(expenseDto)).thenReturn("123");

        MvcResult mvcResult = mockMvc.perform(post("/api/expense")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
                .andReturn();

        assertTrue(Objects.requireNonNull(mvcResult.getResponse().getHeaderValue(HttpHeaders.LOCATION))
                .toString().contains("123"));

    }

    @Test
    @DisplayName("Should Return 404 Not Found Exception when calling expense endpoint with invalid id")
    void shouldReturn404ErrorResponseForGETWithInvalidId() throws Exception {
        Mockito.when(expenseService.getExpense("123")).thenThrow(new ExpenseNotFoundException("Cannot find Expense By id - 123"));

        MvcResult mvcResult = mockMvc.perform(get("/api/expense/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("https://api.expenses.com/errors/not-found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Expense Not Found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCategory").value("Generic"))
                .andReturn();

    }
}
