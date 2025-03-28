package org.skillfactory.sf_bank_system.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.entity.Wallet;
import org.skillfactory.sf_bank_system.repository.WalletRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatCode;



@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletService walletService;

    private static User user;

    @BeforeAll
    public static void setUp() {
        user = User.builder()
                .id(1L)
                .username("nikita")
                .password("123")
                .email("test@ya.ru")
                .build();
    }

    @Test
    void createWallet_and_saveWallet() {
        walletService.createWallet(user);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }




    @Test
    void createWallet_WhenRepoThrows() {
        //asserts
        doThrow(new RuntimeException())
                .when(walletRepository)
                .save(any(Wallet.class));


        //TODO: разобраться с этой хуйней
        assertThatCode(() -> walletService.createWallet(user));

        RuntimeException ex = Assertions.assertThrows(
                RuntimeException.class, () -> walletService.createWallet(user)
        );
        assertTrue(ex.getMessage().contains(String.format("Failed to create wallet for user with id %s", user.getId())));




    }

}
