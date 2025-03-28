package org.skillfactory.sf_bank_system.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skillfactory.sf_bank_system.model.dto.CreateBudgetDto;
import org.skillfactory.sf_bank_system.model.entity.Budget;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.entity.Wallet;
import org.skillfactory.sf_bank_system.repository.BudgetRepository;
import org.skillfactory.sf_bank_system.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BudgetService budgetService;

    private static Budget budget;
    private static User user;

    @BeforeAll
    public static void setUp() {
        budget = new Budget();
        budget.setCategory("Test Category");
        budget.setCurrentAmount(1000);
        budget.setId(1L);
        budget.setLimitAmount(10000);

        user = User.builder()
                .id(1L)
                .username("nikita")
                .password("123")
                .email("test@ya.ru")
                .build();

    }

    @Test
    public void createBudget_whenUserNotFound_shouldThrowNoSuchElementException() {

        Long userId = 123L;
        CreateBudgetDto dto = CreateBudgetDto.builder()
                .category("Food")
                .limitAmount(1000)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            budgetService.createBudget(userId, dto);
        });

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(budgetRepository);
    }

    @Test
    public void createBudget_whenWalletMissing_shouldThrowNoSuchElementException(){
        Long userId = 123L;
        CreateBudgetDto dto = CreateBudgetDto.builder()
                .category("Food")
                .limitAmount(1000)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(123L,
                "user",
                "pass",
                "em@gam.com",
                List.of(),
                List.of()))
        );

        assertThrows(NoSuchElementException.class, () -> {
            budgetService.createBudget(userId, dto);
        });
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(budgetRepository);
    }

    @Test
    public void createBudget_whenBudgetExistsButNotFound_shouldThrowNoSuchElementException() {
        Long userId = 123L;
        Long walletId = 3L;

        Wallet wallet = Wallet.builder().id(walletId).build();

        User user = User.builder()
                .id(userId)
                .username("user")
                .password("pass")
                .email("em@gam.com")
                .wallets(List.of(wallet))
                .notifications(List.of())
                .build();

        CreateBudgetDto dto = CreateBudgetDto.builder()
                .category("Food")
                .limitAmount(1000)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(budgetRepository.existsBudgetByCategory("Food")).thenReturn(true);
        when(budgetRepository.findByWalletIdAndCategory(walletId, "Food")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            budgetService.createBudget(userId, dto);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(budgetRepository, times(1)).existsBudgetByCategory("Food");
        verify(budgetRepository, times(1)).findByWalletIdAndCategory(walletId, "Food");
        verify(budgetRepository, never()).save(any());
    }

    @Test
    public void createBudget_whenBudgetExists_shouldUpdateAndSaveBudget(){
        Long userId = 123L;
        Long walletId = 3L;

        Wallet wallet = Wallet.builder().id(walletId).build();

        User user = User.builder()
                .id(userId)
                .username("user")
                .password("pass")
                .email("em@gam.com")
                .wallets(List.of(wallet))
                .notifications(List.of())
                .build();

        CreateBudgetDto dto = CreateBudgetDto.builder()
                .category("Food")
                .limitAmount(1000)
                .build();

        Budget budget = Budget.builder()
                .id(1L)
                .wallet(wallet)
                .category("Food")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(budgetRepository.existsBudgetByCategory("Food")).thenReturn(true);
        when(budgetRepository.findByWalletIdAndCategory(walletId, "Food")).thenReturn(Optional.of(budget));
        budgetService.createBudget(userId, dto);

        verify(budgetRepository, times(1)).save(any(Budget.class));

    }

    @Test
    public void createBudget_whenBudgetDoesNotExist_shouldCreateNewAndSave(){
        Long userId = 123L;
        Long walletId = 3L;

        Wallet wallet = Wallet.builder().id(walletId).build();

        User user = User.builder()
                .id(userId)
                .username("user")
                .password("pass")
                .email("em@gam.com")
                .wallets(List.of(wallet))
                .notifications(List.of())
                .build();

        CreateBudgetDto dto = CreateBudgetDto.builder()
                .category("Food")
                .limitAmount(1000)
                .build();

        Budget budget = Budget.builder()
                .id(1L)
                .wallet(wallet)
                .category("Food")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(budgetRepository.existsBudgetByCategory("Food")).thenReturn(false);




        budgetService.createBudget(userId, dto);

        verify(budgetRepository, times(1)).save(any(Budget.class));
    }






}
