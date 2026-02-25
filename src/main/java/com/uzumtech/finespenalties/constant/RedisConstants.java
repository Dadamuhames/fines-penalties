package com.uzumtech.finespenalties.constant;

import java.time.Duration;

public class RedisConstants {
    public static final String COURT_ACCESS_TOKEN = "courtAccessToken";

    public static final Duration COURT_ACCESS_TOKEN_TTL = Duration.ofMinutes(55);

    public static final String COURT_REFRESH_TOKEN = "courtRefreshToken";

    public static final Duration COURT_REFRESH_TOKEN_TTL = Duration.ofMinutes(110);
}
