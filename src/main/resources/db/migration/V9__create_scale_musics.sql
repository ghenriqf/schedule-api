CREATE TABLE scale_musics (
    scale_id BIGINT NOT NULL,
    music_id BIGINT NOT NULL,
    PRIMARY KEY (scale_id, music_id),

    CONSTRAINT fk_scale_music_scale
        FOREIGN KEY (scale_id)
        REFERENCES scales(id),

    CONSTRAINT fk_scale_music_music
        FOREIGN KEY (music_id)
        REFERENCES musics(id)
);
