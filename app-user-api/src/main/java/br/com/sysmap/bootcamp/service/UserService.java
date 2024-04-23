package br.com.sysmap.bootcamp.service;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.domain.entities.WalletEntity;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.domain.repository.WalletRepository;
import br.com.sysmap.bootcamp.dto.AuthDto;
import br.com.sysmap.bootcamp.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity save(UserDto userDto) {
        if (this.userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        UserEntity user = createUser(userDto);
        createWalletUser(user);
        return user;
    }

    private UserEntity createUser(UserDto userCreatedDto) {
        UserEntity user = UserEntity.builder()
            .password(passwordEncoder.encode(userCreatedDto.getPassword()))
            .name(userCreatedDto.getName())
            .email(userCreatedDto.getEmail())
            .build();
        return userRepository.save(user);
    }

    private void createWalletUser(UserEntity user) {
        WalletEntity wallerUser = WalletEntity.builder()
                .balance(new BigDecimal("99.99"))
                .points(0L)
                .lastUpdated(LocalDateTime.now())
                .user(user)
                .build();

        log.info("Saving user: {}", user);
        walletRepository.save(wallerUser);
    }


    public List<UserEntity> getAllUser() {
        return this.userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public UserEntity updateUser(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> userExist = this.userRepository.findByEmail(authentication.getName());
        if (userExist.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity userUpdateEntity = userExist.get();

        userUpdateEntity = userUpdateEntity.toBuilder().name(userDto.getName()).email(userDto.getEmail()).password(this.passwordEncoder.encode(userDto.getPassword())).build();

        return userRepository.save(userUpdateEntity);

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = this.userRepository.findByEmail(username);
        return optionalUser.map(user -> new User(user.getEmail(), user.getPassword(),
                        new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    public UserEntity findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthDto auth(AuthDto authDto) {
        UserEntity user = findByEmail(authDto.getEmail());
        if (!this.passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String password = user.getEmail() + ":" + user.getPassword();

        return AuthDto.builder().email(user.getEmail()).token(
                Base64.getEncoder().withoutPadding().encodeToString(password.getBytes())
        ).id(user.getId()).build();
    }

}
