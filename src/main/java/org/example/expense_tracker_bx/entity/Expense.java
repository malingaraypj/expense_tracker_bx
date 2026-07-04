package org.example.expense_tracker_bx.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
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
}