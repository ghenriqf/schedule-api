ALTER TABLE musics
    ADD COLUMN artist VARCHAR(100),
    ADD COLUMN tone VARCHAR(10),
    ADD COLUMN chord_link TEXT;

ALTER TABLE musics RENAME COLUMN link TO video_link;
ALTER TABLE musics RENAME COLUMN name TO title;