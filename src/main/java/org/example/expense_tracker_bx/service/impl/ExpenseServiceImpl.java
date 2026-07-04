package org.example.expense_tracker_bx.service.impl;

import org.example.expense_tracker_bx.dto.request.ExpenseRequest;
import org.example.expense_tracker_bx.dto.response.ExpenseResponse;
import org.example.expense_tracker_bx.entity.Expense;
import org.example.expense_tracker_bx.exception.ResouceNotFoundException;
import org.example.expense_tracker_bx.mapper.ExpenseMapper;
import org.example.expense_tracker_bx.repository.ExpenseRepository;
import org.example.expense_tracker_bx.service.ExpenseService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense newExpense = expenseRepository.save(expenseMapper.toEntity(request));
//
        return expenseMapper.toDto(newExpense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream().map(expenseMapper::toDto).toList();
    }

    @Override
    public ExpenseResponse getExpense(long expenseId) {
        Optional<Expense> expense = expenseRepository.findById(expenseId);

        if(expense.isEmpty()){
            throw new ResouceNotFoundException("expense","id",expenseId);
        }
        return expenseMapper.toDto(expense.get());
    }

    @Override
    public ExpenseResponse updateExpense(ExpenseRequest request) {
        Expense newExpense = expenseRepository.save(expenseMapper.toEntity(request));
//
        return expenseMapper.toDto(newExpense);
    }

    @Override
    public void deleteExpense(long expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
