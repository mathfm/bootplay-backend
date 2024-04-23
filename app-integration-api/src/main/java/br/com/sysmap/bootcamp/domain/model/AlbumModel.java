package br.com.sysmap.bootcamp.domain.model;

import lombok.Data;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;

import java.math.BigDecimal;

@Data
public class AlbumModel {
    private String id;
    private AlbumType albumType;
    private String name;
    private ArtistSimplified[] artists;
    private Image[] images;
    private String releaseDate;
    private ModelObjectType type;
    private BigDecimal value;
}
