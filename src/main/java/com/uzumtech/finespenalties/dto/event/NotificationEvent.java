package com.uzumtech.finespenalties.dto.event;

import java.util.UUID;

public record NotificationEvent(UUID requestId, String text, String receiver) {}
