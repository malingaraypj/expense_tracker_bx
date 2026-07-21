package org.example.expense_tracker_bx.entity;


import org.example.expense_tracker_bx.entity.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryType type; // Prevents mixing INCOME categories with EXPENSE transactions

    // Icon identifier string (e.g., "Utensils", "Briefcase", "Car") to dynamically render Lucide icons in React
    @Column(length = 50)
    private String icon;

    // Hex color code (e.g., "#FF5733") used to color-code Recharts/Chart.js widgets consistently
    @Column(length = 7)
    private String color;

    // True for system-provided defaults (available to everyone). False for user-specific custom ones.
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    // If isDefault == true, user can be NULL (shared globally).
    // If isDefault == false, this points to the specific User who created it.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}