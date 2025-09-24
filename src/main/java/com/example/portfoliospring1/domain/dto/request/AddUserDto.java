package com.example.portfoliospring1.domain.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddUserDto {
    private String nickname;
    private String email;
    private String password;
}
