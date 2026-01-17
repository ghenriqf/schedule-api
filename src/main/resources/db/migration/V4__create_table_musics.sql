CREATE TABLE musics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    link TEXT,
    ministry_id BIGINT NOT NULL,
    CONSTRAINT fk_music_ministry
        FOREIGN KEY (ministry_id)
        REFERENCES ministries(id)
);
