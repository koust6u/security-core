package com.example.securitycore.controller;

import com.example.securitycore.domain.Account;
import com.example.securitycore.domain.AccountDto;
import com.example.securitycore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    @GetMapping("/")
    public String home(){
        return "/home";
    }

    @GetMapping("/mypage")
    public String myPage(){
        return "/user/mypage";
    }

    @GetMapping("/messages")
    public String messages(){
        return "/user/messages";
    }
    @GetMapping("/config")
    public String config(){
        return "/admin/config";
    }

    @GetMapping("/users")
    public String createUser(){
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto){
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userService.create(account);

        return "redirect:/";
    }
}
