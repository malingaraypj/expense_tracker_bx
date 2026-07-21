package org.example.expense_tracker_bx.dto.response;


import org.example.expense_tracker_bx.entity.enums.CategoryType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private CategoryType type;
    private String icon;
    private String color;
    private Boolean isDefault;
}