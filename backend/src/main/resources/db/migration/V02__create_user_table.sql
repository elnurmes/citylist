CREATE TABLE "user"
(
    "id"            VARCHAR(50) PRIMARY KEY,
    "status"        VARCHAR(50) NOT NULL,
    CHECK ( upper(status) = status ),
    "role"          VARCHAR(50) NOT NULL,
    CHECK ( upper(role) = role ),
    "first_name"    VARCHAR(50) NOT NULL,
    "last_name"     VARCHAR(50) NOT NULL,
    "email"         VARCHAR(50) NOT NULL,
    CHECK ( lower(email) = email ),
    "password"      VARCHAR(90) NOT NULL
);
-- unique index
CREATE UNIQUE INDEX ON "user" ("email");