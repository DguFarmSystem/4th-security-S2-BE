package org.farmsystem.sotserver.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String nickname;

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

}

