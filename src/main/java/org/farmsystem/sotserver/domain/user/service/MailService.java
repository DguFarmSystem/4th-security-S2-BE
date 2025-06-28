package org.farmsystem.sotserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.global.error.ErrorCode;
import org.farmsystem.sotserver.global.error.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    private final String SUBJECT = "[SOT] 이메일 인증번호입니다.";

    public void sendEmailAuthCode(String email) {
        String authCode = generateAuthCode();
        sendMail(email, authCode);
        // TTL 설정
        redisTemplate.opsForValue().set("email:auth:" + email, authCode, Duration.ofMinutes(5));
    }

    private void sendMail(String to, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText("인증번호는 [" + authCode + "] 입니다. 5분 내로 입력해주세요.");
        mailSender.send(message);
    }

    private String generateAuthCode() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6자리 숫자
    }

    public boolean verifyAuthCode(String email, String inputCode) {
        String redisKey = "email:auth:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        if (storedCode == null || !storedCode.equals(inputCode)) {
            throw new BusinessException(ErrorCode.INVALID_AUTH_CODE);
        }
        return true;
    }

}

