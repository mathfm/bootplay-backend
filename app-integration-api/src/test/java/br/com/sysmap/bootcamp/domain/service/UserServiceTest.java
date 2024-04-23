package br.com.sysmap.bootcamp.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @DisplayName("Should return user email success")
  @Test
  void findByEmailUserSuccess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    assertEquals(userService.findByEmail(user.getEmail()), user);
  }

  @DisplayName("Should user not found return RunTimeExecption")
  @Test
  void loadUserByUsernameSuccess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    userService.loadUserByUsername(user.getEmail());
  }

  @DisplayName("Should find success use")
  @Test
  void loadUserByUsernameFail() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class,
        () -> userService.loadUserByUsername(user.getEmail()));

  }

}