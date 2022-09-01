package com.kanxz.movie.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Character status;
    private String gender;
    private Integer age;
    private String roleKey;

}
