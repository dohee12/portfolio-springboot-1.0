package com.example.portfoliospring1.service;

import com.example.portfoliospring1.Util.JwtUtil;
import com.example.portfoliospring1.controller.response.BaseException;
import com.example.portfoliospring1.controller.response.BaseResponseStatusEnum;
import com.example.portfoliospring1.domain.dto.LoginByKakaoDto;
import com.example.portfoliospring1.domain.dto.UserDto;
import com.example.portfoliospring1.domain.dto.infra.KapiUserMeDto;
import com.example.portfoliospring1.domain.dto.infra.KauthTokenDto;
import com.example.portfoliospring1.domain.dto.request.AddUserDto;
import com.example.portfoliospring1.domain.dto.request.LoginByEmailDto;
import com.example.portfoliospring1.domain.entity.User;
import com.example.portfoliospring1.infra.feign.KapiFeignClient;
import com.example.portfoliospring1.infra.feign.KauthFeignClient;
import com.example.portfoliospring1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Value("${kakao.api}")
    private String KAKAO_API_KEY;
    @Value("${kakao.secret}")
    private String KAKAO_SECRET_KEY;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final KauthFeignClient kauthFeignClient;
    private final KapiFeignClient  kapiFeignClient;

    public UserDto getUser(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user == null) {
            return null;
        }

        return new UserDto(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> new UserDto(user)).collect(Collectors.toList());
//        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public String addUser(AddUserDto addUserDto, String providerId) {
        if (!userRepository.findAllByNickname(addUserDto.getNickname()).isEmpty()) {
            throw new BaseException(BaseResponseStatusEnum.DUPLICATED_NICKNAME);
        }

        if (!userRepository.findAllByEmail(addUserDto.getEmail()).isEmpty()) {
            throw new BaseException(BaseResponseStatusEnum.DUPLICATED_EMAIL);
        }

        User user = new User();
        user.setNickname(addUserDto.getNickname());
        user.setEmail(addUserDto.getEmail());
        user.setPassword(addUserDto.getPassword());
        user.setProviderId(providerId);

        userRepository.save(user);
        return jwtUtil.generateToken(user.getId(), user.getNickname(), user.getEmail(), user.getProviderId());
    }

    public Boolean isValidNickname(String nickname) {
        // 1. 문자열 길이 3글자 이상 체크
        if (nickname == null || nickname.length() < 3) {
            throw new BaseException(BaseResponseStatusEnum.INVALID_NICKNAME_LENGTH);
        }

        // 2. 사용할 수 없는 문자 체크 '바보', '멍청이' 단어 들어가면 불가
        if (nickname.contains("바보") || nickname.contains("멍청이")) {
            throw new BaseException(BaseResponseStatusEnum.INVALID_NICKNAME_WORD);
        }

        // 3. 중복되었는지 확인
        if (!userRepository.findAllByNickname(nickname).isEmpty()) {
            throw new BaseException(BaseResponseStatusEnum.DUPLICATED_NICKNAME);
        }

        // 4. 위에 해당 안 하면 true
        return true;
    }

    public String login(LoginByEmailDto loginByEmailDto ) {
        try {
            User user = userRepository.findByEmailAndPassword(
                            loginByEmailDto.getEmail(), loginByEmailDto.getPassword()
                    )
                    .orElseThrow();
            return jwtUtil.generateToken(user.getId(), user.getNickname(), user.getEmail(), user.getProviderId());

        } catch (Exception e) {
            throw new BaseException(BaseResponseStatusEnum.FAILED_LOGIN);
        }
    }

    public String loginByKakao(LoginByKakaoDto loginByKakaoDto ) {
        try {
            KauthTokenDto kauthTokenDto = kauthFeignClient.getKakaoToken(
                    "authorization_code",
                    KAKAO_API_KEY,
                    loginByKakaoDto.getOrigin() + "/login/kakao",
                    loginByKakaoDto.getCode(),
                    KAKAO_SECRET_KEY);

            System.out.println("잘 성공함. " + kauthTokenDto);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + kauthTokenDto.access_token());

            KapiUserMeDto kapiUserMeDto = kapiFeignClient.userMe(headers);

            // 카카오 ID 까지는 찾음

            // 카카오 ID로 내 유저 디비 검색
            Optional<User> userOptional = userRepository.findByProviderIdAndDeleted(kapiUserMeDto.id(), false);

            // 1. 있음 -> 찾은 유저로 로그인 시킴
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return jwtUtil.generateToken(user.getId(), user.getNickname(), user.getEmail(), user.getProviderId());
            }

            // 2. 없음 -> 회원가입 시키기
            // 아래 두 정보가 킇라이언트한테 필요함. 우리는 두 정보를 jwt 토큰으로 클라이언트에게 알려줄거임.
            // 회원가입이 필요 => 토큰의 userId가 pull이면 회원가입이 필요함
            // provider id
            return jwtUtil.generateToken(null, null, null, kapiUserMeDto.id());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatusEnum.FAILED_LOGIN);
        }
    }

    public UserDto me(Long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            return new UserDto(optionalUser.get());
        }

        return null;
    }
}