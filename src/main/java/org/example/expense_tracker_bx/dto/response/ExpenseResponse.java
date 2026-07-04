package org.example.expense_tracker_bx.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Builder
public class ExpenseResponse {
    private long id;
    private LocalDate date;
    private String expense;
    private BigDecimal amount;
    private String category;
    private String account;
    private String invoice;
    private String note;
    private LocalDate updatedAt;
}



