package org.example.expense_tracker_bx.dto.request;


import org.example.expense_tracker_bx.entity.enums.CategoryType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Category type is required (INCOME or EXPENSE)")
    private CategoryType type;

    @Size(max = 50, message = "Icon name is too long")
    private String icon;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid Hex code (e.g. #FF5733)")
    private String color;
}