package org.example.expense_tracker_bx.service.impl;


import org.example.expense_tracker_bx.dto.request.WalletRequestDTO;
import org.example.expense_tracker_bx.dto.response.WalletResponseDTO;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.entity.Wallet;
import org.example.expense_tracker_bx.repository.UserRepository;
import org.example.expense_tracker_bx.repository.WalletRepository;
import org.example.expense_tracker_bx.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletResponseDTO createWallet(WalletRequestDTO request, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If this wallet is set as default, unset the existing default wallet
        handleDefaultWalletLogic(request.getIsDefault(), userId);

        Wallet wallet = Wallet.builder()
                .name(request.getName())
                .type(request.getType())
                .balance(request.getBalance())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .user(user)
                .build();

        return mapToDTO(walletRepo.save(wallet));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WalletResponseDTO> getAllUserWallets(Long userId) {
        return walletRepo.findAllByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDTO getWalletById(Long id, Long userId) {
        Wallet wallet = walletRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return mapToDTO(wallet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletResponseDTO updateWallet(Long id, WalletRequestDTO request, Long userId) {
        Wallet wallet = walletRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (request.getIsDefault() != null && request.getIsDefault() && !wallet.getIsDefault()) {
            handleDefaultWalletLogic(true, userId);
        }

        wallet.setName(request.getName());
        wallet.setType(request.getType());
        wallet.setBalance(request.getBalance());
        if (request.getIsDefault() != null) {
            wallet.setIsDefault(request.getIsDefault());
        }

        return mapToDTO(walletRepo.save(wallet));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWallet(Long id, Long userId) {
        Wallet wallet = walletRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Note: If transactions exist for this wallet, MySQL will block deletion due to FK constraints.
        // In a production app, you would either soft-delete or check transactionRepo.existsByWalletId(id) first.
        walletRepo.delete(wallet);
    }

    private void handleDefaultWalletLogic(Boolean isNewDefault, Long userId) {
        if (Boolean.TRUE.equals(isNewDefault)) {
            walletRepo.findByUserIdAndIsDefaultTrue(userId).ifPresent(existingDefault -> {
                existingDefault.setIsDefault(false);
                walletRepo.save(existingDefault);
            });
        }
    }

    private WalletResponseDTO mapToDTO(Wallet w) {
        return WalletResponseDTO.builder()
                .id(w.getId())
                .name(w.getName())
                .type(w.getType())
                .balance(w.getBalance())
                .isDefault(w.getIsDefault())
                .createdAt(w.getCreatedAt())
                .build();
    }
}