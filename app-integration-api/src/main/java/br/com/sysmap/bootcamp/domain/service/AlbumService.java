package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyApi;
import br.com.sysmap.bootcamp.dto.WalletDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {
    private final Queue queue;
    private final RabbitTemplate template;
    private final SpotifyApi spotifyApi;
    public void teste() {
        log.info("teste");
        WalletDto walletDto = new WalletDto();
        walletDto.setTeste("Teste Mat");
        this.template.convertAndSend(queue.getName(), walletDto);
    }


    public List<AlbumEntity> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        return spotifyApi.getAlbums(search);
    }


}
