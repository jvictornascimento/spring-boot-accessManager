insert into users (name, email, password_hash, active, created_at, updated_at)
values
    (
        'Bear Flow Admin',
        'admin@bearflow.local',
        '$2a$10$v..bVQ8yOgIY3Aoce.TGxe00WW/ZnPuGwTs6sCeuqppnFQuwWyojm',
        true,
        current_timestamp,
        current_timestamp
    ),
    (
        'Bear Flow User',
        'user@bearflow.local',
        '$2a$10$v..bVQ8yOgIY3Aoce.TGxe00WW/ZnPuGwTs6sCeuqppnFQuwWyojm',
        true,
        current_timestamp,
        current_timestamp
    );
