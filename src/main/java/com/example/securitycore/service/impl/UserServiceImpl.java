package com.example.securitycore.service.impl;

import com.example.securitycore.domain.Account;
import com.example.securitycore.repository.UserRepository;
import com.example.securitycore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void create(Account account) {
        userRepository.save(account);
    }
}
