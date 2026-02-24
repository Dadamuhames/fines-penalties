CREATE TYPE notification_type AS ENUM (
    'SMS',
    'EMAIL',
    'PUSH'
);

CREATE TYPE request_status AS ENUM (
    'NEW',
    'DELIVERED',
    'PROCESSING',
    'SENT_TO_RETRY',
    'FAILED'
);

CREATE TYPE notification_callback_status AS ENUM (
    'CREATED',
    'SENT',
    'FAILED'
);


CREATE TABLE notification_requests (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_id          UUID NOT NULL UNIQUE,
    notification_service_id BIGINT UNIQUE,
    notification_receiver VARCHAR(255) NOT NULL,
    notification_text   TEXT NOT NULL,
    notification_type   notification_type NOT NULL,
    request_status      request_status NOT NULL,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notification_requests_request_id ON notification_requests(request_id);

CREATE TABLE notification_callbacks (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    notification_request_id BIGINT NOT NULL UNIQUE,
    code                INTEGER NOT NULL,
    message             TEXT NOT NULL,
    status              notification_callback_status NOT NULL,

    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_callbacks_request
        FOREIGN KEY (notification_request_id)
        REFERENCES notification_requests(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_notification_callbacks_request_id ON notification_callbacks(notification_request_id);