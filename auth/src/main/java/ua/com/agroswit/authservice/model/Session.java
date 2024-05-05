package ua.com.agroswit.authservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "session")
public class Session implements Serializable {

    @Id
    private Integer id;

    @Indexed
    private UUID refreshToken;

    @Indexed
    private Integer userId;

    private String footprint;

    private String ip;

    @TimeToLive
    private Integer expiresIn;

    private LocalDateTime createdAt;

}
