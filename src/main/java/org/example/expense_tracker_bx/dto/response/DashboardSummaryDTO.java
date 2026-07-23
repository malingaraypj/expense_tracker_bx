package org.example.expense_tracker_bx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private BigDecimal totalBalance;
    private BigDecimal totalMonthlyIncome;
    private BigDecimal totalMonthlyExpenses;
    private BigDecimal netSavings;
    private List<CategorySummaryDTO> expensesByCategory;
    private List<CategorySummaryDTO> incomeBySource;
    private List<WalletResponseDTO> walletBalances;
    private List<TransactionResponseDTO> recentTransactions;
}
