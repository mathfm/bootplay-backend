package br.com.sysmap.bootcamp.domain.repository;

import br.com.sysmap.bootcamp.domain.entities.AlbumEntity;
import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

  List<AlbumEntity> findAllByUsers(UserEntity user);

  Optional<AlbumEntity> findByIdSpotify(String idSpotify);

  Optional<AlbumEntity> findByUsersAndIdSpotify(UserEntity user, String idSpotify);

  void deleteByUsersAndIdSpotify(UserEntity user, String idSpotify);

}
