package com.expensetracker.expense_tracker.service;

import com.expensetracker.expense_tracker.model.Expense;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void addExpense(Expense expense){
        expenseRepository.save(expense);
    }

    public void deleteExpense(Long id){
        if (!expenseRepository.existsById(id)){
            throw new RuntimeException("Expense with ID " + id + " does not exist!");
        }
        expenseRepository.deleteById(id);
    }

    public Expense updateExpense(Long id, Expense expense) {
        if (!expenseRepository.existsById(id)){
            throw new RuntimeException("Expense with ID " + id + " does not exist!");
        }
        expense.setId(id);
        expenseRepository.save(expense);
        return expense;
    }

    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenses = expenseRepository.findByCategory(category);
        if(expenses.isEmpty()){
           /* expenses.isEmpty()        // true ถ้าว่าง
            expenses.size() == 0      // เช็คจำนวน
            expenses.size() > 0       // มีข้อมูลอยู่
            Collections.isEmpty(expenses) // แบบ utility class */
            throw new RuntimeException("Expense with category " + category + " does not exist!");
        }
        return expenses;
    }

    public Map<String, Double> getExpenseSummary() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getCost)
                ));
    }
}
