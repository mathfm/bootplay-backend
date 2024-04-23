package br.com.sysmap.bootcamp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter

public class UserDto {
  @NotBlank
  private String name;

  @Email
  @NotNull
  private String email;
  @NotBlank
  @Size(min = 8)
  private String password;
}
