package com.example.securitycore.domain;

import lombok.Data;

@Data
public class AccountDto {
    private String username;
    private String password;
    private String role;
    private String age;
    private String email;
}
