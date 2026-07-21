create table players (
    id uuid primary key default uuidv7(),
    name varchar(32) not null ,

    constraint unique_name unique (name)
);

create table matches (
    id uuid primary key default uuidv7(),
    first_player uuid not null,
    second_player uuid not null,
    winner uuid,

    constraint fk_first_player foreign key (first_player) references players(id),
    constraint fk_second_player foreign key (second_player) references players(id),
    constraint fk_winner foreign key (winner) references players(id),
    constraint check_different_players check (first_player <> second_player),
    constraint check_winner check (winner is null or winner = first_player or winner = second_player)
);