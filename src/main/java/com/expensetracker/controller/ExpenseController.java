package com.expensetracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseController(
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository) {

        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    @PostMapping
    public Expense addExpense(@RequestBody ExpenseRequest request) {

        Category category =
                categoryRepository.findByName(request.getCategory());

        if (category == null) {

            category = categoryRepository.save(
                    new Category(request.getCategory())
            );
        }

        Expense expense = new Expense(
                request.getTitle(),
                request.getAmount(),
                request.getDate(),
                category
        );

        return expenseRepository.save(expense);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found"));

        Category category =
                categoryRepository.findByName(request.getCategory());

        if (category == null) {

            category = categoryRepository.save(
                    new Category(request.getCategory())
            );
        }

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {

        expenseRepository.deleteById(id);
    }

    public static class ExpenseRequest {

        private String title;

        private BigDecimal amount;

        private LocalDate date;

        private String category;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}