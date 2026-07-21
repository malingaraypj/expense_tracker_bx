package org.example.expense_tracker_bx.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
}
