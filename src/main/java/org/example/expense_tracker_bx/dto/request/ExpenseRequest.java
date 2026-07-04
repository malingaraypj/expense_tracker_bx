package org.example.expense_tracker_bx.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {
    private String expense;
    private BigDecimal amount;
    private String category;
    private String account;
    private String invoice;
    private String note;
}
