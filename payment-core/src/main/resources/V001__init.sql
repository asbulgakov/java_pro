CREATE TYPE payment_status AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED');

CREATE TABLE payments (
      id BIGSERIAL PRIMARY KEY,
      amount NUMERIC(19,4) NOT NULL,
      description TEXT,
      status payment_status NOT NULL DEFAULT 'PENDING',
      product_id BIGINT NOT NULL,
      user_id BIGINT NOT NULL,
      confirmation_code VARCHAR(6),
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP,

      CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
      CONSTRAINT fk_payment_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_product_id ON payments(product_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_created_at ON payments(created_at);