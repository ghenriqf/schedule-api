CREATE TABLE scale_members (
    scale_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (scale_id, member_id),

    CONSTRAINT fk_scale_member_scale
        FOREIGN KEY (scale_id)
        REFERENCES scales(id),

    CONSTRAINT fk_scale_member_member
        FOREIGN KEY (member_id)
        REFERENCES members(id)
);