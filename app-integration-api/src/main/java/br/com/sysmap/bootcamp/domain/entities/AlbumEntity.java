package br.com.sysmap.bootcamp.domain.entities;

import lombok.Data;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;

import java.math.BigDecimal;

@Data
public class AlbumEntity {
    private AlbumType albumType;
    private ArtistSimplified[] artists;
    private String id;
    private Image[] images;
    private String releaseDate;
    private ModelObjectType type;
    private BigDecimal value;
}
