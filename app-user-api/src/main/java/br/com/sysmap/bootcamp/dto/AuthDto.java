package br.com.sysmap.bootcamp.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String email;
    private String password;
    private Long id;
    private String token;



}
