package br.com.sysmap.bootcamp.web;


import static org.mockito.Mockito.when;


import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.entities.WalletEntity;
import br.com.sysmap.bootcamp.service.UserService;
import br.com.sysmap.bootcamp.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)
public class WalletControllerTest {

  @Mock
  private WalletService walletService;

  @InjectMocks
  private WalletController walletController;

  @Mock
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("Should return wallet user")
  @Test
  void testGetUserWallet() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .name("teste")
        .email("test@gmail.com")
        .password("147258")
        .build();
    WalletEntity walletUser = new WalletEntity();
    walletUser.setBalance(new BigDecimal("99.99"));
    walletUser.setPoints(0L);
    walletUser.setLastUpdated(LocalDateTime.now());
    walletUser.setUser(user);
    when(walletService.getUserWallet()).thenReturn(walletUser);
  }
}