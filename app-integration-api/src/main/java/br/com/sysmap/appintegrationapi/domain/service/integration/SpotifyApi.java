package br.com.sysmap.appintegrationapi.domain.service.integration;

import br.com.sysmap.appintegrationapi.domain.entities.AlbumEntity;
import br.com.sysmap.appintegrationapi.domain.mapper.AlbumMapper;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SpotifyApi {

    private se.michaelthelin.spotify.SpotifyApi spotifyApi = new se.michaelthelin.spotify.SpotifyApi
            .Builder()
            .setClientId("8a1b85de62334e918b5b0261365977f4")
            .setClientSecret("ca986dd021364908957b6341de4a5308")
            .build();

    public List<AlbumEntity> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        spotifyApi.setAccessToken((getToken()));

        return AlbumMapper.INSTANCE.toEntity(spotifyApi.searchAlbums(search)
                .market(CountryCode.BR)
                .limit(25)
                .build()
                .execute()
                .getItems()).stream().peek(album -> album.setValue(BigDecimal.valueOf((Math.random() * ((100.00 - 12.00) + 1)) + 12.00)
                .setScale(2, BigDecimal.ROUND_HALF_UP))).toList();
    }

    public  String getToken() throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest credentialsRequest = spotifyApi.clientCredentials().build();
        return credentialsRequest.execute().getAccessToken();
    }

}
