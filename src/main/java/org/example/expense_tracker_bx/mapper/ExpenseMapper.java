package org.example.expense_tracker_bx.mapper;

import jakarta.persistence.Entity;
import lombok.Builder;
import org.example.expense_tracker_bx.dto.request.ExpenseRequest;
import org.example.expense_tracker_bx.dto.response.ExpenseResponse;
import org.example.expense_tracker_bx.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponse toDto(Expense expense) {
        if (expense == null) {
            return null;
        }

        return ExpenseResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .updatedAt(expense.getUpdatedAt())
                .expense(expense.getExpense()) // the name of the expense
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .account(expense.getAccount())
                .invoice(expense.getInvoice())
                .note(expense.getNote())
                .build();
    }

    public Expense toEntity(ExpenseRequest request){
        if(request == null) return null;

        Expense expense = new Expense();
        expense.setExpense(request.getExpense());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setAccount(request.getAccount());
        expense.setInvoice(request.getInvoice());
        expense.setNote(request.getNote());

        return expense;
    }

}
