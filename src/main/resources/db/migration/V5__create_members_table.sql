CREATE TABLE members (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     ministry_id BIGINT NOT NULL,
     role VARCHAR(50) NOT NULL,

     CONSTRAINT fk_member_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

     CONSTRAINT fk_member_ministry
        FOREIGN KEY (ministry_id)
        REFERENCES ministries(id)
);
