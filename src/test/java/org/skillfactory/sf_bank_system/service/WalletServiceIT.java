package org.skillfactory.sf_bank_system.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.entity.Wallet;
import org.skillfactory.sf_bank_system.repository.UserRepository;
import org.skillfactory.sf_bank_system.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WalletServiceIT {

    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("nikita")
                .password("123")
                .email("test@ya.ru")
                .build();
        userRepository.save(user);
    }

    @Test
    public void createWalletAndSaveTest() {

        walletService.createWallet(user);

        List<Wallet> wallets = walletRepository.findAll();
        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        Wallet wallet = wallets.get(0);
        assertEquals(0, wallet.getBalance());
        assertEquals(user.getId(), wallet.getUser().getId());



    }


}
