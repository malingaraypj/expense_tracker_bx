package org.example.expense_tracker_bx.service;

import org.example.expense_tracker_bx.dto.request.ExpenseRequest;
import org.example.expense_tracker_bx.dto.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    public ExpenseResponse createExpense(ExpenseRequest expenseRequest);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpense(long expenseId);

    ExpenseResponse updateExpense(ExpenseRequest request);

    void deleteExpense(long expenseId);
}
