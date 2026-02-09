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
);

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
);
