package br.com.sysmap.bootcamp.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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


  @DisplayName("Should return user sucess")
  @Test
  void getUserSuccess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    SecurityContextHolder.setContext(securityContext);
    when(userService.findByEmail("matheus@gmail.com")).thenReturn(user);
    assertEquals(userService.findByEmail("matheus@gmail.com").getEmail(), user.getEmail());
  }

  @DisplayName("Should register album successfully")
  @Test
  void createAlbumSuccess() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    AlbumDto album = new AlbumDto("Sobrevivendo no Inferno",
        "14NT8Mm0TNta2Tlaf0wC4j",
        "Racionais",
        "https://i.scdn.co/image/ab67616d0000b273dc04f429698834d0736ddb0a",
        new BigDecimal("15"));
    AlbumEntity entity = AlbumEntity.builder().idSpotify(album.getIdSpotify())
        .id(1L)
        .name(album.getName())
        .imageUrl(album.getImageUrl())
        .value(album.getValue())
        .artistName(album.getArtistName())
        .users(user).build();
    when(albumRepository.save(any(AlbumEntity.class))).thenReturn(entity);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        "matheus@gmail.com", "12345678");
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    albumService.saveAlbum(album);
  }

  @DisplayName("Should register album failed return RuntimeException")
  @Test
  void createAlbumFail() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();
    AlbumDto album = new AlbumDto("Sobrevivendo no Inferno",
        "14NT8Mm0TNta2Tlaf0wC4j",
        "Racionais",
        "https://i.scdn.co/image/ab67616d0000b273dc04f429698834d0736ddb0a",
        new BigDecimal("15"));

    AlbumEntity existingAlbum = AlbumEntity.builder()
        .id(2L)  // Id diferente do álbum que será salvo
        .idSpotify(album.getIdSpotify())
        .name(album.getName())
        .imageUrl(album.getImageUrl())
        .value(album.getValue())
        .artistName(album.getArtistName())
        .users(user)
        .build();

    when(albumRepository.findByUsersAndIdSpotify(any(UserEntity.class), eq(album.getIdSpotify())))
        .thenReturn(Optional.of(existingAlbum));

    when(albumRepository.save(any(AlbumEntity.class))).thenAnswer(invocation -> {
      AlbumEntity savedAlbum = invocation.getArgument(0);
      savedAlbum.setId(3L);
      return savedAlbum;
    });

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        "matheus@gmail.com", "12345678");
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    assertThrows(RuntimeException.class, () -> albumService.saveAlbum(album));
  }

  @DisplayName("Should return collection of albums")
  @Test
  void getMyCollection() {
    UserEntity user = UserEntity.builder()
        .id(1L)
        .email("matheus@gmail.com")
        .password("12345678")
        .build();

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        "matheus@gmail.com", "12345678");
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    albumService.getMyCollection();
  }

//  @Test
//  void deleteAlbumSuccess() {
//    UserEntity user = UserEntity.builder()
//        .id(1L)
//        .email("matheus@gmail.com")
//        .password("12345678")
//        .build();
//    AlbumDto album = new AlbumDto("Sobrevivendo no Inferno",
//        "14NT8Mm0TNta2Tlaf0wC4j",
//        "Racionais",
//        "https://i.scdn.co/image/ab67616d0000b273dc04f429698834d0736ddb0a",
//        new BigDecimal("15"));
//
//    AlbumEntity entity = AlbumEntity.builder()
//        .id(1L)
//        .idSpotify(album.getIdSpotify())
//        .name(album.getName())
//        .imageUrl(album.getImageUrl())
//        .value(album.getValue())
//        .artistName(album.getArtistName())
//        .users(user)
//        .build();
//
//    Authentication authentication = new UsernamePasswordAuthenticationToken(
//        "matheus@gmail.com", "12345678");
//    SecurityContext securityContext = new SecurityContextImpl();
//    securityContext.setAuthentication(authentication);
//    SecurityContextHolder.setContext(securityContext);
//
//    // Configuração do mock com os mesmos argumentos esperados na chamada real
//    when(albumRepository.findByUsersAndIdSpotify(user, album.getIdSpotify()))
//        .thenReturn(Optional.of(entity));
//
//    // Chama o método a ser testado
//    albumService.deleteAlbum(album.getIdSpotify());
//  }
}