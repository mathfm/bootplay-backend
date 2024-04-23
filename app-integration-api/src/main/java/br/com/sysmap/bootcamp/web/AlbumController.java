package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.service.AlbumService;
import br.com.sysmap.bootcamp.dto.AlbumDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/albums")
public class AlbumController {

  private final AlbumService albumService;

  @Operation(summary = "Retorna todos os albums encontrados na pesquisa")
  @GetMapping("/all")
  public ResponseEntity<List<AlbumModel>> getAlbum(@RequestParam("search") String search)
      throws IOException, ParseException, SpotifyWebApiException {
    return ResponseEntity.ok(this.albumService.getAlbums(search));
  }

  @Operation(summary = "Realiza a compra de um album")
  @PostMapping("/sale")
  public ResponseEntity<Long> saveAlbum(@RequestBody AlbumDto entity) throws Exception {
    return ResponseEntity.ok(this.albumService.saveAlbum(entity));
  }

  @Operation(summary = "Retorna uma coleção de albuns do usuario")
  @GetMapping("/my-collection")
  public ResponseEntity<List<AlbumEntity>> getMyCollection() {
    return ResponseEntity.ok(this.albumService.getMyCollection());
  }

  @Operation(summary = "Remove o album da coleção do usuario")
  @DeleteMapping("/remove/{id}")
  public ResponseEntity<Void> removeAlbum(@PathVariable("id") String id) {
    this.albumService.deleteAlbum(id);
    return ResponseEntity.ok().build();
  }

}
