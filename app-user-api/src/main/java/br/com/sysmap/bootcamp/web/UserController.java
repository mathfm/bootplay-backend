package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user) {
        return ResponseEntity.ok(this.userService.save(user));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> list() {
        return ResponseEntity.ok(this.userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserEntity>> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user, @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.updateUser(user, id));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.deleteUser(id));
    }

}
