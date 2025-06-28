package org.farmsystem.sotserver.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String nickname;

    private String email;

    private String socialId;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 500)
    private String introduction;

    //파싱해서 사용(임시)
    private String skills;
    private String talents;

    public User(String socialId, String imageUrl, Role role) {
        this.socialId = socialId;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public void updateProfileImage(String imageUrl) {this.imageUrl = imageUrl;}
    public void updateIntroduction(String introduction) {this.introduction = introduction;}
    public void updateSkills(String skills) {this.skills = skills;}
    public void updateTalents(String talents) {this.talents = talents;}
    public void updateRole(Role newRole) {this.role = newRole;}
    public void updateEmail(String email) {this.email = email;}

    // 스트링 파싱 -> 리스트
    public List<String> getParsedList(String value) {
        if (value == null || value.isBlank()) return List.of();
        return List.of(value.split(","));
    }

}

