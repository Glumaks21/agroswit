package ua.com.agroswit.authservice.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@RedisHash("refresh_session")
public class RefreshSession {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private UUID refreshToken;

    private Integer userId;

    private String fingerPrint;

    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;

}
