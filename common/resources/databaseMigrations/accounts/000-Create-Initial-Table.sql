CREATE TABLE IF NOT EXISTS ultimatehammer_accounts
(
    id CHAR(36) NOT NULL PRIMARY KEY,
    playername VARCHAR(16),
    language CHAR(5) NOT NULL,
    creation_time TIMESTAMP NOT NULL DEFAULT NOW()
);
