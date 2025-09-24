package com.example.portfoliospring1.controller;

import com.example.portfoliospring1.domain.dto.request.AddUserDto;
import com.example.portfoliospring1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-user")
    public String getUser(@RequestParam String name) {
        return userService.getUser();
    }

    @PostMapping("/add-User")
    public String addUser(@RequestBody AddUserDto addUserDto) {
        return userService.addUser(addUserDto);
    }

}
