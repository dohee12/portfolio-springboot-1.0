package com.example.portfoliospring1.service;

import com.example.portfoliospring1.domain.dto.request.AddUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    public String getUser() {
        return "user";
    }
    public String addUser(AddUserDto addUserDto) {
        return "addUser";
    }
}
