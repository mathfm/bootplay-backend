package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;

import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userDetail = userRepository.findByEmail(username);
        return userDetail.map(user -> new User(user.getEmail(), user.getPassword(),
                        new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
