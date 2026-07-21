package org.example.expense_tracker_bx.dto.response;


import org.example.expense_tracker_bx.entity.enums.WalletType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponseDTO {
    private Long id;
    private String name;
    private WalletType type;
    private BigDecimal balance;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}