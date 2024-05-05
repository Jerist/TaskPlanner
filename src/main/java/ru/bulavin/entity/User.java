package ru.bulavin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class User {
    private Long idUser;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String salt;
}
