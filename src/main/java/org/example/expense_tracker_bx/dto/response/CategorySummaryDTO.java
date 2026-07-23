package org.example.expense_tracker_bx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategorySummaryDTO {
    private String categoryName;
    private BigDecimal totalAmount;
}