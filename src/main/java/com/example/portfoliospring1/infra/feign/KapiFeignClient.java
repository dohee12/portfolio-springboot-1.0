package com.example.portfoliospring1.infra.feign;

import com.example.portfoliospring1.domain.dto.infra.KapiUserMeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name="kapi", url="https://kapi.kakao.com")
public interface KapiFeignClient {

    @GetMapping("/v2/user/me")
    KapiUserMeDto userMe(@RequestHeader Map<String, String> headers);

    // number
    // string
    // array 배열 - [], list, arraylist, vector

    // 순서가 존재
    // 배열의 요소가 100개 일 때, 특정한 값 A를 찾고 싶다.
    // 1~n

    // 트리, 리스트, 맵, 해쉬, set
    // 해쉬 값이 100개 존재할 때, 특정한 값 A를 찾고 싶다
    // 1
    // key - value
}
