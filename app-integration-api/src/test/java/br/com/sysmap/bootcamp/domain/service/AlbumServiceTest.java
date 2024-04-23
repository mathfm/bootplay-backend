package br.com.sysmap.bootcamp.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

  @Mock
  Queue queue;

  @Mock
  SpotifyApi spotifyApi;

  @Mock
  UserRepository userRepository;

  @Mock
  RabbitTemplate template;

  @Mock
  AlbumRepository albumRepository;

  @Mock
  UserService userService;

  @InjectMocks
  AlbumService albumService;

  @Mock
  SecurityContext securityContext;

  @Mock
  Authentication authentication;


  @Test
  void getUserSucess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    SecurityContextHolder.setContext(securityContext);
    when(userService.findByEmail("matheus@gmail.com")).thenReturn(user);
    assertEquals(userService.findByEmail("matheus@gmail.com").getEmail(), user.getEmail());
  }



  @Test
  void getMyCollection() {
  }

  @Test
  void deleteAlbum() {
  }
}