CREATE TABLE known_keywords
(
    id          BIGSERIAL PRIMARY KEY,
    keyword     VARCHAR(100) NOT NULL UNIQUE,
    aliases     TEXT[],
    category    VARCHAR(50),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE job_postings
(
    id              BIGSERIAL PRIMARY KEY,
    adzuna_id       VARCHAR(50) NOT NULL UNIQUE,
    title           VARCHAR(255),
    company         VARCHAR(255),
    description     TEXT,
    source          VARCHAR(50) NOT NULL DEFAULT 'adzuna',
    scraped_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE job_posting_keywords
(
    id          BIGSERIAL PRIMARY KEY,
    posting_id  BIGINT NOT NULL REFERENCES job_postings(id),
    keyword     VARCHAR(100) NOT NULL,
    scraped_at  TIMESTAMP NOT NULL DEFAULT NOW()
);