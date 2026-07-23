package org.example.expense_tracker_bx.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.expense_tracker_bx.dto.response.*;
import org.example.expense_tracker_bx.entity.enums.TransactionType;
import org.example.expense_tracker_bx.repository.TransactionRepository;
import org.example.expense_tracker_bx.repository.WalletRepository;
import org.example.expense_tracker_bx.repository.CategorySummaryProjection;
import org.example.expense_tracker_bx.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public DashboardSummaryDTO getDashboardSummary(Long userId) {
        // Determine the start of the current month
        LocalDate startOfMonth = YearMonth.now().atDay(1);

        // 1. Total Balance
        BigDecimal rawTotalBalance = walletRepository.getTotalBalanceByUserId(userId);
        BigDecimal totalBalance = rawTotalBalance != null ? rawTotalBalance : BigDecimal.ZERO;

        // 2 & 3. Monthly Income & Expenses
        BigDecimal rawIncome = transactionRepository.sumAmountByUserIdAndTypeAndDateAfter(userId, TransactionType.INCOME, startOfMonth);
        BigDecimal rawExpenses = transactionRepository.sumAmountByUserIdAndTypeAndDateAfter(userId, TransactionType.EXPENSE, startOfMonth);

        BigDecimal totalMonthlyIncome = rawIncome != null ? rawIncome : BigDecimal.ZERO;
        BigDecimal totalMonthlyExpenses = rawExpenses != null ? rawExpenses : BigDecimal.ZERO;

        // 4. Net Savings
        BigDecimal netSavings = totalMonthlyIncome.subtract(totalMonthlyExpenses);

        // 5 & 6. Category Breakdown
        List<CategorySummaryProjection> expenseProjections = transactionRepository.findCategorySummary(userId, TransactionType.EXPENSE, startOfMonth);
        List<CategorySummaryProjection> incomeProjections = transactionRepository.findCategorySummary(userId, TransactionType.INCOME, startOfMonth);

        // 7. Wallet Balances
        // 7. Wallet Balances
        List<WalletResponseDTO> walletBalances = walletRepository.findByUserId(userId).stream()
                .map(w -> WalletResponseDTO.builder()
                        .id(w.getId())
                        .name(w.getName())
                        .balance(w.getBalance())
                        .type(w.getType())
                        .build())
                .collect(Collectors.toList());

        // 8. Recent Transactions
        // 8. Recent Transactions
        List<TransactionResponseDTO> recentTransactions = transactionRepository.findTop10ByUserIdOrderByTransactionDateDesc(userId).stream()
                .map(t -> TransactionResponseDTO.builder()
                        .id(t.getId())
                        .amount(t.getAmount())
                        .type(t.getType())
                        .createdAt(t.getCreatedAt())
                        .categoryName(t.getCategory() != null ? t.getCategory().getName() : null)
                        .walletName(t.getWallet() != null ? t.getWallet().getName() : null)
                        .build())
                .collect(Collectors.toList());

        // Construct and return the DTO using the Builder pattern
        return DashboardSummaryDTO.builder()
                .totalBalance(totalBalance)
                .totalMonthlyIncome(totalMonthlyIncome)
                .totalMonthlyExpenses(totalMonthlyExpenses)
                .netSavings(netSavings)
                .expensesByCategory(mapToDTO(expenseProjections))
                .incomeBySource(mapToDTO(incomeProjections))
                .walletBalances(walletBalances)
                .recentTransactions(recentTransactions)
                .build();
    }

    private List<CategorySummaryDTO> mapToDTO(List<CategorySummaryProjection> projections) {
        return projections.stream()
                .map(p -> new CategorySummaryDTO(p.getCategoryName(), p.getTotalAmount()))
                .collect(Collectors.toList());
    }
}