package com.uzumtech.finespenalties.entity.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RedisHash(value = "otpLockout")
public class OtpLockoutEntity {
    @Id
    private Long id;

    @Indexed
    private String phone;

    private OffsetDateTime unlockAt;
}
