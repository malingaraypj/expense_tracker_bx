package org.example.expense_tracker_bx.dto.response;


import org.example.expense_tracker_bx.entity.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private TransactionType type;
    private Long walletId;
    private String walletName;
    private Long destinationWalletId;
    private String destinationWalletName;
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private LocalDateTime createdAt;
}