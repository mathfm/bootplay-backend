package br.com.sysmap.bootcamp.domain.repository;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
