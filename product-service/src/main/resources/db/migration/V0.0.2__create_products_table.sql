CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(255) UNIQUE NOT NULL,
    balance DECIMAL(19,4) DEFAULT 0.0000,
    product_type VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_products_account_number ON products(account_number);
CREATE INDEX idx_products_product_type ON products(product_type);