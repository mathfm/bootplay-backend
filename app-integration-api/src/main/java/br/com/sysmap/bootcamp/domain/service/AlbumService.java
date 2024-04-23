package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyApi;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import br.com.sysmap.bootcamp.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {
    private final Queue queue;
    private final RabbitTemplate template;
    private final SpotifyApi spotifyApi;
    private final AlbumRepository albumRepository;
    private final UserService userService;


//    public void teste() {
//        log.info("teste");
//        WalletDto walletDto = new WalletDto();
//        walletDto.setTeste("Teste Mat");
//        this.template.convertAndSend(queue.getName(), walletDto);
//    }


    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi.getAlbums(search);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long saveAlbum(AlbumDto album) throws RuntimeException {
        try {
            Optional<AlbumEntity> verifySpotify = albumRepository.findByUsersAndIdSpotify(getUser(), album.getIdSpotify());
            if (verifySpotify.isPresent()) {
                throw new RuntimeException("Usuario ja tem o album registrado");
            }
            AlbumEntity albumSaved = createAlbum(album);
            sendCreatedAlbumToQueue(albumSaved);
            return albumSaved.getId();
        }catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void sendCreatedAlbumToQueue(AlbumEntity albumSaved) {
        try {
            WalletDto walletDto = new WalletDto(albumSaved
                .getUsers()
                .getEmail(),
                albumSaved.getValue());
            log.info("Enviando dados {}", walletDto);
            this.template.convertAndSend(queue.getName(), walletDto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private AlbumEntity createAlbum(AlbumDto album) {
        AlbumEntity entity = AlbumEntity.builder().idSpotify(album.getIdSpotify())
                .name(album.getName())
                .imageUrl(album.getImageUrl())
                .value(album.getValue())
                .artistName(album.getArtistName())
                .users(getUser()).build();

      return albumRepository.save(entity);
    }

    public List<AlbumEntity> getMyCollection() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByEmail(authentication.getName());
        return albumRepository.findAllByUsers(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAlbum(String albumId) {
        Optional<AlbumEntity> albumUser = albumRepository.findByUsersAndIdSpotify(getUser(), albumId);
        if (albumUser.isEmpty()) {
            throw new RuntimeException("O usuario não possui esse album.");
        }
        albumRepository.deleteByUsersAndIdSpotify(getUser(), albumId);

    }

    private UserEntity getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString();
        return userService.findByEmail(username);
    }


}
