package org.example.expense_tracker_bx.service;


import org.example.expense_tracker_bx.dto.request.TransactionRequestDTO;
import org.example.expense_tracker_bx.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO request, Long userId);
    Page<TransactionResponseDTO> getAllTransactions(Long userId, Pageable pageable);
    TransactionResponseDTO getTransactionById(Long id, Long userId);
    TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO request, Long userId);
    void deleteTransaction(Long id, Long userId);
}