package org.example.expense_tracker_bx.repository;

import java.math.BigDecimal;

public interface CategorySummaryProjection {
    String getCategoryName();
    BigDecimal getTotalAmount();
}