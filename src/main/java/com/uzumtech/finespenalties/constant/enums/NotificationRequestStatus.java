package com.uzumtech.finespenalties.constant.enums;

public enum NotificationRequestStatus {
    NEW, DELIVERED, PROCESSING, SENT_TO_RETRY, FAILED;

    public boolean isTerminal() {
        return this.equals(DELIVERED) || this.equals(FAILED);
    }
}
