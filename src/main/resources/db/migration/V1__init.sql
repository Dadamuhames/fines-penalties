CREATE TYPE offence_status AS ENUM (
    'NEW',
    'PENDING',
    'PAID',
    'OVERDUE',
    'CANCELLED'
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
    reference text NOT NULL UNIQUE,
    title text NOT NULL,
    content text NOT NULL,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- constraints
    CONSTRAINT uq_code_articles_reference UNIQUE (reference)
);

CREATE TABLE inspectors (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name text NOT NULL,
    personnel_number text NOT NULL UNIQUE,
    pinfl varchar(14) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    date_of_birth date NOT NULL,
    is_active boolean NOT NULL DEFAULT TRUE,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inspectors ON inspectors (personnel_number);

CREATE TABLE users (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name text NOT NULL,
    date_of_birth date NOT NULL,
    phone text NOT NULL UNIQUE,
    pinfl varchar(14) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    is_active boolean NOT NULL DEFAULT TRUE,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_phone ON users (phone);

CREATE TABLE legal_offenses (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    inspector_id bigint NOT NULL,
    code_article_id bigint NOT NULL,
    offender_pinfl varchar(14) NOT NULL,
    offender_full_name text NOT NULL,
    offense_location text NOT NULL,
    offender_explanation text,
    description text NOT NULL,
    status offence_status NOT NULL,
    protocol_number text NOT NULL UNIQUE,
    offense_date_time timestamptz NOT NULL,
    -- timestamps
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- relations
    CONSTRAINT fk_legal_offense_inspector FOREIGN KEY (inspector_id) REFERENCES inspectors (id) ON DELETE RESTRICT,
    CONSTRAINT fk_legal_offense_code_article FOREIGN KEY (code_article_id) REFERENCES code_articles (id) ON DELETE RESTRICT
);

CREATE INDEX idx_legal_offenses_offender_pinfl ON legal_offenses (offender_pinfl);

CREATE INDEX idx_legal_offenses_id_offender_pinfl ON legal_offenses (id, offender_pinfl);

CREATE TABLE penalties (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    offense_id bigint NOT NULL UNIQUE,
    type penalty_type NOT NULL,
    status penalty_status NOT NULL,
    bhm_amount_at_time bigint NOT NULL,
    bhm_multiplier numeric(10, 1) NOT NULL,
    due_date timestamptz NOT NULL,
    court_decision_text text NOT NULL,
    court_decision_date timestamptz NOT NULL,
    court_case_number text NOT NULL UNIQUE,
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
