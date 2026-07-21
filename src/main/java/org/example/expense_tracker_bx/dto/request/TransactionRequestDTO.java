package org.example.expense_tracker_bx.dto.request;


import org.example.expense_tracker_bx.entity.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    @NotNull(message = "Transaction type is required (INCOME, EXPENSE, TRANSFER)")
    private TransactionType type;

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    // Optional for TRANSFERS, required for INCOME/EXPENSE
    private Long categoryId;

    // Only required when type == TRANSFER
    private Long destinationWalletId;
}
