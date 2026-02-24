CREATE TYPE offence_status AS ENUM (
    'NEW', 'FAILED_TO_SEND', 'SENT_TO_COURT', 'PENALTY_RULED_OUT', 'PAID', 'OVERDUE', 'CANCELLED'
);

CREATE TYPE penalty_type AS ENUM (
    'FINE',
    'CONFISCATION',
    'PAID_WITHDRAWAL',
    'DEPRIVATION_OF_DRIVING_RIGHT',
    'DEPRIVATION_OF_OTHER_RIGHT',
    'ADMINISTRATIVE_ARREST',
    'EXPULSION',
    'COMPULSORY_WORKS'
);

CREATE TYPE penalty_status AS ENUM (
    'PENDING',
    'PAID',
    'OVERDUE',
    'CANCELLED',
    'ENFORCED'
);

CREATE TYPE user_role AS ENUM (
    'USER',
    'INSPECTOR',
    'ADMIN'
);

CREATE TABLE code_articles (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reference VARCHAR(255) NOT NULL UNIQUE,
    title text NOT NULL,
    content text NOT NULL,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- constraints
    CONSTRAINT uq_code_articles_reference UNIQUE (reference)
);

CREATE INDEX idx_code_articles_reference ON code_articles (reference);

CREATE TABLE inspectors (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    personnel_number VARCHAR(255) NOT NULL UNIQUE,
    pinfl varchar(14) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    date_of_birth date NOT NULL,
    is_active boolean NOT NULL DEFAULT TRUE,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inspectors_personnel_number ON inspectors (personnel_number);

CREATE TABLE users (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    age int NOT NULL,
    phone VARCHAR(12) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    pinfl varchar(14) NOT NULL UNIQUE,
    password TEXT DEFAULT NULL,
    is_active boolean NOT NULL DEFAULT TRUE,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_phone ON users (phone);

CREATE INDEX idx_users_pinfl ON users (pinfl);


CREATE TABLE legal_offenses (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    court_offense_id bigint default null,
    court_case_number varchar(255) default null,
    inspector_id bigint NOT NULL,
    user_id bigint NOT NULL,
    code_article_id bigint NOT NULL,
    offense_location text NOT NULL,
    offender_explanation text,
    description text NOT NULL,
    status offence_status NOT NULL,
    protocol_number VARCHAR(255) NOT NULL UNIQUE,
    offense_date_time timestamptz NOT NULL,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- relations
    CONSTRAINT fk_legal_offense_inspector FOREIGN KEY (inspector_id) REFERENCES inspectors (id) ON DELETE RESTRICT,
    CONSTRAINT fk_legal_offense_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_legal_offense_code_article FOREIGN KEY (code_article_id) REFERENCES code_articles (id) ON DELETE RESTRICT
);

CREATE INDEX idx_legal_offenses_inspector_id ON legal_offenses (inspector_id);

CREATE INDEX idx_legal_offenses_user_id ON legal_offenses (user_id);

CREATE INDEX idx_legal_offenses_id_user_id ON legal_offenses (id, user_id);

CREATE INDEX idx_legal_offenses_id_status ON legal_offenses (id, status);


CREATE TABLE penalties (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    offense_id bigint NOT NULL UNIQUE,
    type penalty_type NOT NULL,
    status penalty_status NOT NULL,
    bhm_amount_at_time bigint NOT NULL,
    bhm_multiplier numeric(10, 1) NOT NULL,
    due_date timestamptz NOT NULL,
    court_decision_text text NOT NULL,
    court_penalty_id bigint NOT NULL,
    qualification varchar(255) NOT NULL,
    deprivation_duration_months integer NOT NULL,
    court_decision_date timestamptz NOT NULL,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- relations
    CONSTRAINT fk_penalty_offense FOREIGN KEY (offense_id) REFERENCES legal_offenses (id) ON DELETE RESTRICT
);

CREATE INDEX idx_penalties_offense_id ON penalties (offense_id);

CREATE TABLE refresh_tokens (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token text NOT NULL UNIQUE,
    expiry_date timestamptz NOT NULL,
    subject text NOT NULL,
    user_role user_role NOT NULL
);

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens (token);