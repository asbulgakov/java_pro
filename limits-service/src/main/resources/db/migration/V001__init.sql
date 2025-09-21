CREATE TABLE user_limits (
     id UUID PRIMARY KEY,
     user_id BIGINT NOT NULL,
     daily_limit DECIMAL(15,2) NOT NULL,
     remaining_limit DECIMAL(15,2) NOT NULL,
     last_reset_date TIMESTAMP NOT NULL,
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_limits_user_id ON user_limits(user_id);
CREATE INDEX idx_user_limits_last_reset_date ON user_limits(last_reset_date);

INSERT INTO user_limits (id, user_id, daily_limit, remaining_limit, last_reset_date, created_at, updated_at)
SELECT
    gen_random_uuid(),
    user_id,
    10000.00,
    10000.00,
    NOW(),
    NOW(),
    NOW()
FROM generate_series(1, 100) AS user_id;

ALTER TABLE user_limits ADD CONSTRAINT uk_user_limits_user_id UNIQUE (user_id);

ALTER TABLE user_limits ADD CONSTRAINT chk_user_limits_positive CHECK (daily_limit >= 0 AND remaining_limit >= 0);