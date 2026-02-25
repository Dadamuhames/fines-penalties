package com.uzumtech.finespenalties.entity.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "otpCheckLockout")
public class OtpCheckLockoutEntity {

    @Id
    private Long id;

    @Indexed
    private String phone;

    private Integer attempt;

    private OffsetDateTime unblockAt;
}
