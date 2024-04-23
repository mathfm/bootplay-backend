package br.com.sysmap.bootcamp.service;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.entities.WalletEntity;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.WalletDto;
import br.com.sysmap.bootcamp.utils.PointsUtils;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class WalletService {

  private static final Logger log = LoggerFactory.getLogger(WalletService.class);
  private final UserService userService;
  private final WalletRepository walletRepository;

  public void debit(WalletDto walletDto) {

    UserEntity user = userService.findByEmail(walletDto.getEmail());
    WalletEntity wallet = walletRepository.findByUser(user).orElseThrow();
    BigDecimal newBalance = wallet.getBalance().subtract(walletDto.getValue());
    if (newBalance.signum() < 0) {
      throw new RuntimeException("Insufficient balance");
    }
    wallet.setLastUpdated(LocalDateTime.now());
    DayOfWeek dayOfWeek = wallet.getLastUpdated().getDayOfWeek();
    wallet.setBalance(wallet.getBalance().subtract(walletDto.getValue()));
    wallet.setPoints(PointsUtils.getPoints(dayOfWeek) + wallet.getPoints());
    walletRepository.save(wallet);
  }

  public WalletEntity getUserWallet() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity user = userService.findByEmail(authentication.getName());
    return walletRepository.findByUser(user).orElseThrow();
  }

}
