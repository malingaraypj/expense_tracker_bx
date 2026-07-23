package org.example.expense_tracker_bx.repository;


import org.example.expense_tracker_bx.entity.Wallet;
import org.example.expense_tracker_bx.entity.enums.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Fetches all wallets belonging to the authenticated user.
     * Used to render the wallet cards/carousel on the React dashboard.
     *
     * Generates SQL: SELECT * FROM wallets WHERE user_id = ?1;
     */
    List<Wallet> findAllByUserId(Long userId);

    /**
     * Securely fetches a single wallet by its ID and user ID.
     * Explicitly used in TransactionServiceImpl to verify ownership before deducting money!
     *
     * Generates SQL: SELECT * FROM wallets WHERE wallet_id = ?1 AND user_id = ?2;
     */
    Optional<Wallet> findByIdAndUserId(Long id, Long userId);

    /**
     * Finds the user's default wallet (if one exists).
     * Useful for pre-selecting a wallet in the "Add Transaction" modal on React.
     *
     * Generates SQL: SELECT * FROM wallets WHERE user_id = ?1 AND is_default = true;
     */
    Optional<Wallet> findByUserIdAndIsDefaultTrue(Long userId);

    /**
     * Fetches all wallets of a specific type (e.g., BANK or CASH) for a user.
     *
     * Generates SQL: SELECT * FROM wallets WHERE user_id = ?1 AND type = ?2;
     */
    List<Wallet> findAllByUserIdAndType(Long userId, WalletType type);

    /**
     * Custom JPQL query to calculate the user's Total Net Worth across all wallets.
     * (Sums positive bank/cash balances and subtracts credit card debts).
     */
    List<Wallet> findByUserId(Long userId);

    @Query("SELECT SUM(w.balance) FROM Wallet w WHERE w.user.id = :userId")
    BigDecimal getTotalBalanceByUserId(@Param("userId") Long userId);
}
