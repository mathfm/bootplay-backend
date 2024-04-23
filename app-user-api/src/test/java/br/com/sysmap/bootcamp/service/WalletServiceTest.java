package br.com.sysmap.bootcamp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.entities.WalletEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;

import br.com.sysmap.bootcamp.dto.WalletDto;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private WalletRepository walletRepository;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;


  @Mock
  private UserRepository userRepository;


  @InjectMocks
  private WalletService walletService;

  @Test
  void debitUserSucess() {

    UserEntity user = UserEntity.builder()
        .id(1L)
        .name("teste")
        .email("email@example.com")
        .password("123456")
        .build();

    WalletEntity wallet = new WalletEntity();
    wallet.setId(1L);
    wallet.setUser(user);
    wallet.setPoints(0L);
    wallet.setBalance(new BigDecimal("100")); // Initial balance
    wallet.setLastUpdated(LocalDateTime.now());

    when(userService.findByEmail(user.getEmail())).thenReturn(user);
    when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

    WalletDto walletDto = new WalletDto();
    walletDto.setEmail("email@example.com");
    walletDto.setValue(new BigDecimal("20")); // Debit value

    walletService.debit(walletDto);

    assertEquals(BigDecimal.valueOf(80), wallet.getBalance());
    assertEquals(DayOfWeek.from(wallet.getLastUpdated()), wallet.getLastUpdated().getDayOfWeek());
    verify(walletRepository, times(1)).save(wallet);

  }

  @Test
  void debitUserFail() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .name("teste")
        .email("email@example.com")
        .password("")
        .build();

    WalletEntity wallet = new WalletEntity();
    wallet.setId(1L);
    wallet.setUser(user);
    wallet.setPoints(0L);
    wallet.setBalance(new BigDecimal("100")); // Initial balance
    wallet.setLastUpdated(LocalDateTime.now());

    when(userService.findByEmail(user.getEmail())).thenReturn(user);
    when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

    WalletDto walletDto = new WalletDto();
    walletDto.setEmail("email@example.com");
    walletDto.setValue(new BigDecimal("200")); // Debit value
    assertThrows(RuntimeException.class, () -> walletService.debit(walletDto));
  }

  @Test
  void getUserWallet() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("email@example.com")
        .name("joao")
        .password("123456789")
        .build();

    WalletEntity walletUser = WalletEntity.builder()
        .id(1L)
        .user(user)
        .points(0L)
        .lastUpdated(LocalDateTime.now())
        .balance(new BigDecimal("99.99"))
        .build();
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn(user.getName());
    when(userService.findByEmail(authentication.getName())).thenReturn(user);
    when(walletRepository.findByUser(user)).thenReturn(Optional.ofNullable(walletUser));

    assertEquals(walletUser, walletService.getUserWallet());

  }
}