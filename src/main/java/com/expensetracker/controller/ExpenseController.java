
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseController(
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Get only the logged-in user's expenses
    @GetMapping
    public List<Expense> getExpenses(
            @RequestHeader("X-User-Id") Long userId) {

        User user = getUser(userId);

        return expenseRepository.findByUser(user);
    }

    // Add expense for the logged-in user
    @PostMapping
    public Expense addExpense(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ExpenseRequest request) {

        User user = getUser(userId);

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

        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    // Update only the logged-in user's expense
    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ExpenseRequest request) {

        User user = getUser(userId);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found"));

        if (expense.getUser() == null ||
                !expense.getUser().getId().equals(user.getId())) {

            throw new RuntimeException("You cannot edit this expense");
        }

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

    // Delete only the logged-in user's expense
    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        User user = getUser(userId);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found"));

        if (expense.getUser() == null ||
                !expense.getUser().getId().equals(user.getId())) {

            throw new RuntimeException("You cannot delete this expense");
        }

        expenseRepository.delete(expense);
    }

    // Find user
    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
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

