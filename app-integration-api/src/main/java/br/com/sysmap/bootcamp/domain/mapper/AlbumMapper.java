package br.com.sysmap.bootcamp.domain.mapper;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.util.List;

@Named("AlbumMapper")
@Mapper
public interface AlbumMapper {
    AlbumMapper INSTANCE = Mappers.getMapper(AlbumMapper.class);

    List<AlbumEntity> toEntity(AlbumSimplified[] albumSimplifieds);

}
