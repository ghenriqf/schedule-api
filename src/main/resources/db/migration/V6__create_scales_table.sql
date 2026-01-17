CREATE TABLE scales (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ministry_id BIGINT NOT NULL,

    CONSTRAINT fk_scale_ministry
        FOREIGN KEY (ministry_id)
        REFERENCES ministries(id)
);
