package com.expensetracker.expense_tracker.service;

import com.expensetracker.expense_tracker.model.Expense;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
