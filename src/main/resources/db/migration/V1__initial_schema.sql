-- USERS
CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(100) UNIQUE,
   name VARCHAR(150),
   email VARCHAR(150) UNIQUE,
   password VARCHAR(250),
   birth DATE,
   created_at TIMESTAMP,
   updated_at TIMESTAMP
);

-- MINISTRIES
CREATE TABLE ministries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    invite_code VARCHAR(36) UNIQUE,
    avatar_url VARCHAR(255)
);

-- FUNCTIONS
CREATE TABLE functions (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL
);

-- MEMBERS
CREATE TABLE members (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     ministry_id BIGINT NOT NULL,
     role VARCHAR(50),

     CONSTRAINT fk_member_user
         FOREIGN KEY (user_id)
             REFERENCES users(id),

     CONSTRAINT fk_member_ministry
         FOREIGN KEY (ministry_id)
             REFERENCES ministries(id)
);

-- MUSICS
CREATE TABLE musics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    artist VARCHAR(100),
    tone VARCHAR(10),
    video_link VARCHAR(255),
    chord_sheet_link VARCHAR(255),
    ministry_id BIGINT NOT NULL,

    CONSTRAINT fk_music_ministry
        FOREIGN KEY (ministry_id)
            REFERENCES ministries(id)
);

-- SCALES
CREATE TABLE scales (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    date TIMESTAMP NOT NULL,
    ministry_id BIGINT NOT NULL,
    minister_id BIGINT,

    CONSTRAINT fk_scale_ministry
        FOREIGN KEY (ministry_id)
            REFERENCES ministries(id),

    CONSTRAINT fk_scale_minister
        FOREIGN KEY (minister_id)
            REFERENCES members(id)
);

-- MEMBER_FUNCTIONS (ManyToMany)
CREATE TABLE member_functions (
    member_id BIGINT NOT NULL,
    function_id BIGINT NOT NULL,

    PRIMARY KEY (member_id, function_id),

    CONSTRAINT fk_member_function_member
      FOREIGN KEY (member_id)
          REFERENCES members(id)
          ON DELETE CASCADE,

    CONSTRAINT fk_member_function_function
      FOREIGN KEY (function_id)
          REFERENCES functions(id)
          ON DELETE CASCADE
);

-- SCALE_MEMBERS (agora é entidade própria)
CREATE TABLE scale_members (
   id BIGSERIAL PRIMARY KEY,
   scale_id BIGINT NOT NULL,
   member_id BIGINT NOT NULL,

   CONSTRAINT fk_scale_members_scale
       FOREIGN KEY (scale_id)
           REFERENCES scales(id)
           ON DELETE CASCADE,

   CONSTRAINT fk_scale_members_member
       FOREIGN KEY (member_id)
           REFERENCES members(id)
           ON DELETE CASCADE
);

-- SCALE_MEMBER_FUNCTIONS
CREATE TABLE scale_member_functions (
    scale_member_id BIGINT NOT NULL,
    function_id BIGINT NOT NULL,

    PRIMARY KEY (scale_member_id, function_id),

    CONSTRAINT fk_smf_scale_member
        FOREIGN KEY (scale_member_id)
            REFERENCES scale_members(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_smf_function
        FOREIGN KEY (function_id)
            REFERENCES functions(id)
            ON DELETE CASCADE
);

-- SCALE_MUSICS
CREATE TABLE scale_musics (
      scale_id BIGINT NOT NULL,
      music_id BIGINT NOT NULL,

      PRIMARY KEY (scale_id, music_id),

      CONSTRAINT fk_scale_music_scale
          FOREIGN KEY (scale_id)
              REFERENCES scales(id)
              ON DELETE CASCADE,

      CONSTRAINT fk_scale_music_music
          FOREIGN KEY (music_id)
              REFERENCES musics(id)
              ON DELETE CASCADE
);

-- DEFAULT FUNCTIONS
INSERT INTO functions (name) VALUES
('VOCALISTA'),
('BACKING'),
('BATERIA'),
('VIOLÃO'),
('MESA DE SOM'),
('MÍDIA'),
('GUITARRA'),
('TECLADO'),
('BAIXO');