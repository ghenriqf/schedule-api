CREATE TABLE member_functions (
    member_id BIGINT NOT NULL,
    function_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, function_id),

    CONSTRAINT fk_member_function_member
        FOREIGN KEY (member_id)
        REFERENCES members(id),

    CONSTRAINT fk_member_function_function
        FOREIGN KEY (function_id)
        REFERENCES functions(id)
);
