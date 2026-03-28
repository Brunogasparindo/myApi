CREATE TABLE orders (
    id          UUID            NOT NULL,
    customer_id VARCHAR(255)    NOT NULL,
    amount      DECIMAL(19, 2)  NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);
