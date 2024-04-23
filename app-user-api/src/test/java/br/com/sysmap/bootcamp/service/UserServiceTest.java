package br.com.sysmap.bootcamp.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.AuthDto;
import br.com.sysmap.bootcamp.dto.UserDto;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


  @DisplayName("Should save user success")
  @Test
  void saveUserSuccess() {
    UserDto userDto = new UserDto("matheus", "matheus@gmail.com", "12345678");
    userService.save(userDto);
  }

  @DisplayName("Should user fail return RunTimeExecption")
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

  @DisplayName("Should return list user success")
  @Test
  void userReturnListSuccess() {
    when(userRepository.findAll()).thenReturn(new ArrayList<>());
    userService.getAllUser();
  }

  @DisplayName("Should return user by id")
  @Test
  void userReturnByIdSuccess() {
    UserEntity userEntity = UserEntity
        .builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    assertNotNull(userService.getUserById(userEntity.getId()));
  }

  @DisplayName("Should update info user in success")
  @Test
  void updateUserSuccess() {
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
    when(userRepository.save(any(UserEntity.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    UserDto newInfoUser = new UserDto("fernando", "fernandos@gmail.com", "1234567844");

    UserEntity updatedUser = userService.updateUser(newInfoUser);
    assertEquals(updatedUser.getId(), existingUser.getId());

  }

  @DisplayName("Should update failed return RunTimeException")
  @Test
  void updateUserFail() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(authentication.getName()).thenReturn("matheus");
    UserDto user = new UserDto("fernando", "fernandos@gmail.com", "1234567844");
    assertThrows(RuntimeException.class, () -> userService.updateUser(user));
  }

  @DisplayName("Should return user email success")
  @Test
  void findByEmailSuccess() {
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
  @DisplayName("Should not found email and return RunTimeException")
  void findByEmailFail() {
    assertThrows(RuntimeException.class, () -> userService.findByEmail("matheus@gmail.com"));
  }

  @Test

  @DisplayName("Should find success user")
  void loadUserByUsernameSuccess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    userService.loadUserByUsername(user.getEmail());
  }

  @DisplayName("Should user not found return RunTimeExecption")
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

  @Test
  @DisplayName("Should auth user in success")
  void authUserSuccess() {

    UserEntity userEntity = UserEntity.builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    AuthDto authDto = AuthDto.builder()
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .id(1L)
        .token("tokenauth")
        .build();

    AuthDto result = userService.auth(authDto);

    assertNotNull(result);
    assertEquals(userEntity.getEmail(), result.getEmail());
    assertNotNull(result.getToken());
    assertEquals(userEntity.getId(), result.getId());
  }

  @DisplayName("Should auth user in failed")
  @Test
  void authUserFail() {
    UserEntity userEntity = UserEntity
        .builder()
        .id(1L)
        .name("matheus")
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    AuthDto authDto = AuthDto
        .builder()
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .id(1L)
        .token("senhaErrada")
        .build();
    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    assertThrows(RuntimeException.class, () -> userService.auth(authDto));
  }


}