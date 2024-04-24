package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entities.UserEntity;
import br.com.sysmap.bootcamp.dto.AuthDto;
import br.com.sysmap.bootcamp.dto.UserDto;
import br.com.sysmap.bootcamp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"$2a$10$/CzClzOLs6UUQk82TuepIeZIIorsn0u8Bu06ASVC2z8USfej63HBG\"}")))
    })
    @PostMapping("/create")
    public ResponseEntity<UserEntity> save(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(this.userService.save(userDto));
    }

    @Operation(summary = "Buscar usuário lista de usuário ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserEntity.class),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\",\"password\": \"$2a$10$/CzClzOLs6UUQk82TuepIeZIIorsn0u8Bu06ASVC2z8USfej63HBG\" }, {\"id\": 2, \"name\": \"Jane Smith\", \"email\": \"jane.smith@example.com\", \"password\": \"$2a$10$/CzClzOLs6UUQk82TuepIe546Iorsn0u8Bu06ASVC2z8USfej63H41\"}]"
                            )))
                                })

    @GetMapping("/")
    public ResponseEntity<List<UserEntity>> list() {
        return ResponseEntity.ok(this.userService.getAllUser());
    }

    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserEntity.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"$2a$10$/CzClzOLs6UUQk82TuepIeZIIorsn0u8Bu06ASVC2z8USfej63HBG\"}")))
    })
    public ResponseEntity<Optional<UserEntity>> getUserById(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @Operation(summary = "Atualizar um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "201", description = "Usuário atualizado com sucesso")
    })
    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody @Valid UserDto user) {
        return ResponseEntity.ok(this.userService.updateUser(user));
    }
    @Operation(summary = "Autenticar um usuário")
    @PostMapping("/auth")
    public ResponseEntity<AuthDto> auth (@RequestBody AuthDto user) {
        return ResponseEntity.ok(this.userService.auth(user));
    }

}
