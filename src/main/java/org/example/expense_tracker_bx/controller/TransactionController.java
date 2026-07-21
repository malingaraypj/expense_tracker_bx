package org.example.expense_tracker_bx.controller;


import org.example.expense_tracker_bx.dto.request.TransactionRequestDTO;
import org.example.expense_tracker_bx.dto.response.TransactionResponseDTO;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {

        TransactionResponseDTO created = transactionService.createTransaction(request, authenticatedUser.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User authenticatedUser) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(transactionService.getAllTransactions(authenticatedUser.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {

        return ResponseEntity.ok(transactionService.getTransactionById(id, authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {

        return ResponseEntity.ok(transactionService.updateTransaction(id, request, authenticatedUser.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {

        transactionService.deleteTransaction(id, authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }
}