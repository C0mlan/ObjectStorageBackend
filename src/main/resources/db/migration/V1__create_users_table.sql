CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    created_at     TIMESTAMP(6) NOT NULL,
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    auth_provider  VARCHAR(255) NOT NULL
        CHECK (auth_provider IN ('EMAIL', 'GOOGLE')),

    email          VARCHAR(255) NOT NULL UNIQUE,
    first_name     VARCHAR(255) NOT NULL,
    last_name      VARCHAR(255) NOT NULL,
    password       VARCHAR(255),

    role           VARCHAR(255) NOT NULL
        CHECK (role IN ('USER', 'ADMIN')),

    status         VARCHAR(255) NOT NULL
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))

);