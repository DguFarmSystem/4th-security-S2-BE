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

    private String socialId;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User(String socialId, String imageUrl, UserStatus userStatus) {
        this.socialId = socialId;
        this.imageUrl = imageUrl;
        this.userStatus = userStatus;
    }
}

