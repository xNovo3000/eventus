CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255)          NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL,
    active   BIT(1)                NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

CREATE TABLE authority
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    user_id BIGINT                NOT NULL,
    CONSTRAINT pk_authority PRIMARY KEY (id)
);

ALTER TABLE authority
    ADD CONSTRAINT FK_AUTHORITY_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

CREATE TABLE event
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` VARCHAR(2048)         NOT NULL,
    creator_id    BIGINT                NOT NULL,
    creation_date datetime              NOT NULL,
    start         datetime              NOT NULL,
    end           datetime              NOT NULL,
    seats         INT                   NOT NULL,
    approved      BIT(1)                NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES user (id);

CREATE TABLE subscription
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    user_id       BIGINT                NOT NULL,
    event_id      BIGINT                NOT NULL,
    creation_date datetime              NOT NULL,
    rating        INT                   NULL,
    comment       VARCHAR(2048)         NULL,
    CONSTRAINT pk_subscription PRIMARY KEY (id)
);

ALTER TABLE subscription
    ADD CONSTRAINT uc_a8df4c5c2e104fdcd06111190 UNIQUE (user_id, event_id);

ALTER TABLE subscription
    ADD CONSTRAINT FK_SUBSCRIPTION_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE subscription
    ADD CONSTRAINT FK_SUBSCRIPTION_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);