package org.farmsystem.sotserver.domain.user.entity;

public enum Role {
    ROLE_USER, // 동국대 이메일 인증 된 회원
    ROLE_PENDING,  // 동국대 이메일 인증 전
    ROLE_ADMIN // 관리자 권한
}
