package com.ephirious.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_player_id", nullable = false)
    private PlayerJpaEntity firstPlayer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_player_id", nullable = false)
    private PlayerJpaEntity secondPlayer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winner_id", nullable = false)
    private PlayerJpaEntity winner;

    public MatchJpaEntity(UUID id, PlayerJpaEntity first, PlayerJpaEntity second, PlayerJpaEntity winner) {
        if (id == null) {
            throw new IllegalStateException("MatchJpaEntity requires not null id");
        }
        if (Objects.equals(first, second)) {
            throw new IllegalStateException("The match entity can't create, because first player equal second player");
        }

        this.firstPlayer = first;
        this.secondPlayer = second;
        this.winner = winner;
    }

}
