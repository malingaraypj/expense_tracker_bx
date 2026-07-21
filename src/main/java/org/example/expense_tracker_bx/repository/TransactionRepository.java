package org.example.expense_tracker_bx.repository;


import org.example.expense_tracker_bx.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Fetch paginated transactions for a specific user, sorted by date
    Page<Transaction> findAllByUserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);

    // Fetch a single transaction ensuring it belongs to the authenticated user
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    // Check if any transactions exist for a category (useful when deleting categories)
    boolean existsByCategoryId(Long categoryId);
}