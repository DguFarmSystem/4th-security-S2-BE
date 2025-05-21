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
    private Role role;

    public User(String socialId, String imageUrl, Role role) {
        this.socialId = socialId;
        this.imageUrl = imageUrl;
        this.role = role;
    }


}

