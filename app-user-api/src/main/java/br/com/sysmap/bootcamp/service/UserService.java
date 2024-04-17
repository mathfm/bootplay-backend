package br.com.sysmap.bootcamp.service;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;


    public UserEntity save(UserEntity user) {
        log.info("Saving user: {}", user);
        return this.userRepository.save(user);
    }

    public List<UserEntity> getAllUser() {
        return  this.userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public UserEntity updateUser(UserEntity user, Long id) {

        return userRepository.save(user);
    }

    public String deleteUser(Long id) {
        this.userRepository.deleteById(id);
        return "Deletado com sucesso.";
    }



}
