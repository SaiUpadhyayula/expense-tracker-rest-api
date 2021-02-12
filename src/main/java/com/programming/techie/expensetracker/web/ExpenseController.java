package com.programming.techie.expensetracker.web;

import com.programming.techie.expensetracker.dto.ExpenseDto;
import com.programming.techie.expensetracker.exception.ExpenseNotFoundException;
import com.programming.techie.expensetracker.model.Expense;
import com.programming.techie.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@RequestBody ExpenseDto expenseDto) {
        String expenseId = expenseService.addExpense(expenseDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(expenseId)
                .toUri();
        return ResponseEntity.created(location)
                .build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(@RequestBody Expense expense) {
        expenseService.updateExpense(expense);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseDto getExpenseByName(@PathVariable String name) {
        return expenseService.getExpense(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
    }

    @ExceptionHandler({ExpenseNotFoundException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot Find Expense with the given data")
    public void handleException() {
        // Do Nothing
    }

}
