insert into users (name, email, password_hash, active, created_at, updated_at)
values
    (
        'Bear Flow Admin',
        'admin@bearflow.local',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOhiJz2nIkCCwSYNlTUTa6R/Lm7JCq6aO',
        true,
        current_timestamp,
        current_timestamp
    ),
    (
        'Bear Flow User',
        'user@bearflow.local',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOhiJz2nIkCCwSYNlTUTa6R/Lm7JCq6aO',
        true,
        current_timestamp,
        current_timestamp
    );
