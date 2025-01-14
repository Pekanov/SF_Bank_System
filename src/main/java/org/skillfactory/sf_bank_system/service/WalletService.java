package org.skillfactory.sf_bank_system.service;

import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.entity.Wallet;
import org.skillfactory.sf_bank_system.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public void createWallet(User user) {

        try {
            Wallet wallet = Wallet.builder()
                    .user(user)
                    .balance(0)
                    .build();
            walletRepository.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wallet for user with id " + user.getId() ,e);
        }
    }




}
