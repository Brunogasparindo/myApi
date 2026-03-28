CREATE TABLE refresh_tokens (
    id          UUID                     NOT NULL,
    user_id     UUID                     NOT NULL,
    token_hash  VARCHAR(64)              NOT NULL,
    expires_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked     BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT uq_refresh_tokens_hash UNIQUE (token_hash),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
);
