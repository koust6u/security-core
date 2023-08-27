package com.example.corespringsecurity.service;

import com.example.corespringsecurity.domain.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  {
    void createUser(Account account);
}
