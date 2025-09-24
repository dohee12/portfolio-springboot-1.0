package com.example.portfoliospring1.service;

import com.example.portfoliospring1.controller.response.BaseException;
import com.example.portfoliospring1.controller.response.BaseResponseStatusEnum;
import com.example.portfoliospring1.domain.dto.UserDto;
import com.example.portfoliospring1.domain.dto.request.AddUserDto;
import com.example.portfoliospring1.domain.entity.User;
import com.example.portfoliospring1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUser(String nickname) {
        User user = userRepository.findByNickname(nickname);

        return new UserDto(user);
    }

    public List<UserDto> getUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> new UserDto(user)).collect(Collectors.toList());
    }

    public Long addUser(AddUserDto addUserDto) {
        // 회원가입
        // 1. 중복된 이메일이면 가입 못하게
        if (userRepository.findAllByNickname(addUserDto.getNickname()).isEmpty()) {
            throw new BaseException(BaseResponseStatusEnum.DUPLICATED_EMAIL);
        }
        // 2. 중복된 닉네임이면 가입 못하게
        if (!userRepository.findAllByEmail(addUserDto.getEmail()).isEmpty()) {
            throw new BaseException(BaseResponseStatusEnum.DUPLICATE_NICKNAME);
        }

        User user = new User();
        user.setNickname(addUserDto.getNickname());
        user.setPassword(addUserDto.getPassword());
        user.setEmail(addUserDto.getEmail());

        userRepository.save(user);
        return user.getId();
    }
}
