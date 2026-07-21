package org.example.expense_tracker_bx.dto.request;


import org.example.expense_tracker_bx.entity.enums.WalletType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRequestDTO {

    @NotBlank(message = "Wallet name is required")
    @Size(max = 100, message = "Wallet name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Wallet type is required (BANK, CASH, CREDIT_CARD, DIGITAL_WALLET, INVESTMENT)")
    private WalletType type;

    @NotNull(message = "Initial balance is required")
    private BigDecimal balance;

    private Boolean isDefault;
}