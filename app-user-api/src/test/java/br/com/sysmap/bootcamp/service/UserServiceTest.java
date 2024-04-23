package br.com.sysmap.bootcamp.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.UserDto;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private WalletRepository walletRepository;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;

  @InjectMocks
  private UserService userService;

  @Test
  void saveUserSucess() {
    UserDto userDto = new UserDto("matheus", "matheus@gmail.com", "12345678");
    userService.save(userDto);
  }

  @Test
  void saveUserFail() {
    UserDto user = new UserDto("matheus", "matheus@gmail.com", "12345678");
    UserEntity userEntity = UserEntity
        .builder()
        .id(1L)
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .build();
    when(userRepository.findByEmail("matheus@gmail.com")).thenReturn(Optional.of(userEntity));

    assertThrows(RuntimeException.class, () -> userService.save(user), "User already exists");
  }

  @Test
  void userReturnListSucess() {
    when(userRepository.findAll()).thenReturn(new ArrayList<>());
    userService.getAllUser();
  }

  @Test
  void userReturnByIdSucess() {
    UserEntity userEntity = UserEntity
        .builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    assertNotNull(userService.getUserById(userEntity.getId()));
  }

  @Test
  void updateUserSucess() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(authentication.getName()).thenReturn("matheus");

    UserEntity existingUser = UserEntity.builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();

    when(userRepository.findByEmail("matheus")).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserDto newInfoUser = new UserDto("fernando", "fernandos@gmail.com", "1234567844");

    UserEntity updatedUser = userService.updateUser(newInfoUser);
    assertEquals(updatedUser.getId(), existingUser.getId());

  }

  @Test
  void updateUserFail() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(authentication.getName()).thenReturn("matheus");
    UserDto user = new UserDto("fernando", "fernandos@gmail.com", "1234567844");
    assertThrows(RuntimeException.class, () -> userService.updateUser(user));
  }

  @Test
  void findByEmailSucess() {
    UserEntity userEntity = UserEntity
        .builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

    UserEntity foundUser = userService.findByEmail(userEntity.getEmail());

    assertEquals(userEntity, foundUser);
  }

  @Test
  void findByEmailFail() {
    assertThrows(RuntimeException.class, () -> userService.findByEmail("matheus@gmail.com"));
  }



}