create table players (
    id uuid primary key default uuidv7(),
    name varchar(32) not null ,

    constraint unique_name unique (name)
);

create table matches (
    id uuid primary key default uuidv7(),
    first_player_id uuid not null,
    second_player_id uuid not null,
    winner_id uuid not null,

    constraint fk_first_player foreign key (first_player_id) references players(id),
    constraint fk_second_player foreign key (second_player_id) references players(id),
    constraint fk_winner foreign key (winner_id) references players(id),
    constraint check_different_players check (first_player_id <> second_player_id),
    constraint check_winner check (winner_id is null or winner_id = first_player_id or winner_id = second_player_id)
);