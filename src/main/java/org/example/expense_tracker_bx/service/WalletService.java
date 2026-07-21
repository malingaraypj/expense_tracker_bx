package org.example.expense_tracker_bx.service;


import org.example.expense_tracker_bx.dto.request.WalletRequestDTO;
import org.example.expense_tracker_bx.dto.response.WalletResponseDTO;
import java.util.List;

public interface WalletService {
    WalletResponseDTO createWallet(WalletRequestDTO request, Long userId);
    List<WalletResponseDTO> getAllUserWallets(Long userId);
    WalletResponseDTO getWalletById(Long id, Long userId);
    WalletResponseDTO updateWallet(Long id, WalletRequestDTO request, Long userId);
    void deleteWallet(Long id, Long userId);
}