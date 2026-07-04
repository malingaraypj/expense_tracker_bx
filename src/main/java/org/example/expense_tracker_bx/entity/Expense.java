package org.example.expense_tracker_bx.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Data
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate date;

    @Column(name = "expense_name", nullable = false)
    private String expense;

    // Precision 10, Scale 2 allows for amounts up to 99,999,999.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String category;

    private String account;

    private String invoice;

    // Explicitly giving the note field more characters (default is usually 255)
    @Column(length = 1000)
    private String note;

    @UpdateTimestamp
    private LocalDate updatedAt;
}