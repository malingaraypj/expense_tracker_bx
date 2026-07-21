package org.example.expense_tracker_bx.controller;


import org.example.expense_tracker_bx.dto.request.WalletRequestDTO;
import org.example.expense_tracker_bx.dto.response.WalletResponseDTO;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(
            @Valid @RequestBody WalletRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {
        return new ResponseEntity<>(walletService.createWallet(request, authenticatedUser.getId()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WalletResponseDTO>> getAllWallets(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(walletService.getAllUserWallets(authenticatedUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getWalletById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(walletService.getWalletById(id, authenticatedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletRequestDTO request,
            @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(walletService.updateWallet(id, request, authenticatedUser.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        walletService.deleteWallet(id, authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }
}