package org.example.expense_tracker_bx.service.impl;


import org.example.expense_tracker_bx.dto.request.TransactionRequestDTO;
import org.example.expense_tracker_bx.dto.response.TransactionResponseDTO;
import org.example.expense_tracker_bx.entity.*;
import org.example.expense_tracker_bx.entity.enums.TransactionType;
import org.example.expense_tracker_bx.repository.*;
import org.example.expense_tracker_bx.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final WalletRepository walletRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet sourceWallet = walletRepo.findByIdAndUserId(request.getWalletId(), userId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Wallet destWallet = null;
        if (request.getType() == TransactionType.TRANSFER) {
            if (request.getDestinationWalletId() == null) {
                throw new IllegalArgumentException("Destination wallet is required for transfers");
            }
            destWallet = walletRepo.findByIdAndUserId(request.getDestinationWalletId(), userId)
                    .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
        }

        // Apply financial impact to wallets
        applyWalletImpact(request.getType(), request.getAmount(), sourceWallet, destWallet, false);
        walletRepo.save(sourceWallet);
        if (destWallet != null) walletRepo.save(destWallet);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .type(request.getType())
                .wallet(sourceWallet)
                .destinationWallet(destWallet)
                .category(category)
                .user(user)
                .build();

        return mapToDTO(transactionRepo.save(transaction));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getAllTransactions(Long userId, Pageable pageable) {
        return transactionRepo.findAllByUserIdOrderByTransactionDateDesc(userId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDTO(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO request, Long userId) {
        Transaction existingTx = transactionRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // 1. REVERSE old wallet impact
        applyWalletImpact(existingTx.getType(), existingTx.getAmount(), existingTx.getWallet(), existingTx.getDestinationWallet(), true);

        // 2. FETCH new relationships
        Wallet newSourceWallet = walletRepo.findByIdAndUserId(request.getWalletId(), userId)
                .orElseThrow(() -> new RuntimeException("New source wallet not found"));

        Wallet newDestWallet = null;
        if (request.getType() == TransactionType.TRANSFER) {
            newDestWallet = walletRepo.findByIdAndUserId(request.getDestinationWalletId(), userId)
                    .orElseThrow(() -> new RuntimeException("New destination wallet not found"));
        }

        Category newCategory = null;
        if (request.getCategoryId() != null) {
            newCategory = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        // 3. APPLY new wallet impact
        applyWalletImpact(request.getType(), request.getAmount(), newSourceWallet, newDestWallet, false);

        // 4. Save updated wallets and transaction
        walletRepo.save(newSourceWallet);
        if (newDestWallet != null) walletRepo.save(newDestWallet);

        existingTx.setAmount(request.getAmount());
        existingTx.setDescription(request.getDescription());
        existingTx.setTransactionDate(request.getTransactionDate());
        existingTx.setType(request.getType());
        existingTx.setWallet(newSourceWallet);
        existingTx.setDestinationWallet(newDestWallet);
        existingTx.setCategory(newCategory);

        return mapToDTO(transactionRepo.save(existingTx));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransaction(Long id, Long userId) {
        Transaction tx = transactionRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Reverse the wallet impact before deleting
        applyWalletImpact(tx.getType(), tx.getAmount(), tx.getWallet(), tx.getDestinationWallet(), true);
        walletRepo.save(tx.getWallet());
        if (tx.getDestinationWallet() != null) walletRepo.save(tx.getDestinationWallet());

        transactionRepo.delete(tx);
    }

    // --- HELPER METHODS ---

    /**
     * Safely adjusts balances. If isReversal == true, it does the opposite (used for DELETE and UPDATE).
     */
    private void applyWalletImpact(TransactionType type, BigDecimal amount, Wallet source, Wallet dest, boolean isReversal) {
        BigDecimal modifier = isReversal ? amount.negate() : amount;

        if (type == TransactionType.INCOME) {
            source.setBalance(source.getBalance().add(modifier));
        } else if (type == TransactionType.EXPENSE) {
            if (!isReversal && source.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance in wallet: " + source.getName());
            }
            source.setBalance(source.getBalance().subtract(modifier));
        } else if (type == TransactionType.TRANSFER) {
            if (!isReversal && source.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance for transfer in wallet: " + source.getName());
            }
            source.setBalance(source.getBalance().subtract(modifier));
            if (dest != null) {
                dest.setBalance(dest.getBalance().add(modifier));
            }
        }
    }

    private TransactionResponseDTO mapToDTO(Transaction tx) {
        return TransactionResponseDTO.builder()
                .id(tx.getId())
                .amount(tx.getAmount())
                .description(tx.getDescription())
                .transactionDate(tx.getTransactionDate())
                .type(tx.getType())
                .walletId(tx.getWallet().getId())
                .walletName(tx.getWallet().getName())
                .destinationWalletId(tx.getDestinationWallet() != null ? tx.getDestinationWallet().getId() : null)
                .destinationWalletName(tx.getDestinationWallet() != null ? tx.getDestinationWallet().getName() : null)
                .categoryId(tx.getCategory() != null ? tx.getCategory().getId() : null)
                .categoryName(tx.getCategory() != null ? tx.getCategory().getName() : null)
                .categoryColor(tx.getCategory() != null ? tx.getCategory().getColor() : null)
                .createdAt(tx.getCreatedAt())
                .build();
    }
}