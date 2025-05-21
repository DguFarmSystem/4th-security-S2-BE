package org.farmsystem.sotserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.farmsystem.sotserver.domain.user.entity.SocialType;
import org.farmsystem.sotserver.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.farmsystem.sotserver.global.error.ErrorCode.OAUTH_TOKEN_REQUEST_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OauthTokenProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public String getOauthToken(String code, SocialType socialType) {
        try {
            if (!socialType.equals(SocialType.KAKAO)) {
                throw new BusinessException(OAUTH_TOKEN_REQUEST_FAILED); // 불필요한 소셜 타입 방어
            }

            String oauthTokenApiUrl = "https://kauth.kakao.com/oauth/token";
            String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("code", decodedCode);
            requestBody.add("client_id", kakaoClientId);
            requestBody.add("client_secret", kakaoClientSecret);
            requestBody.add("redirect_uri", kakaoRedirectUri);
            requestBody.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    oauthTokenApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            log.error("OAuth 토큰 요청 중 오류 발생: ", e);
            throw new BusinessException(OAUTH_TOKEN_REQUEST_FAILED);
        }
    }
}
