package org.example.expense_tracker_bx.repository;


import org.example.expense_tracker_bx.entity.Transaction;
import org.example.expense_tracker_bx.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Fetch paginated transactions for a specific user, sorted by date
    Page<Transaction> findAllByUserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);

    // Fetch a single transaction ensuring it belongs to the authenticated user
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    // Check if any transactions exist for a category (useful when deleting categories)
    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.transactionDate >= :startDate")
    BigDecimal sumAmountByUserIdAndTypeAndDateAfter(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate
    );

    @Query("SELECT c.name as categoryName, SUM(t.amount) as totalAmount FROM Transaction t JOIN t.category c WHERE t.user.id = :userId AND t.type = :type AND t.transactionDate >= :startDate GROUP BY c.id")
    List<CategorySummaryProjection> findCategorySummary(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate
    );

    List<Transaction> findTop10ByUserIdOrderByTransactionDateDesc(Long userId);
}