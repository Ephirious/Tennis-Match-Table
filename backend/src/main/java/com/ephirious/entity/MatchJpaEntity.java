package com.ephirious.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public MatchJpaEntity(
            @NonNull UUID id,
            @NonNull PlayerJpaEntity first,
            @NonNull PlayerJpaEntity second,
            @NonNull PlayerJpaEntity winner
    ) {
        if (Objects.equals(first, second)) {
            throw new IllegalStateException("The match entity can't create, because first player equal second player");
        }

        this.id = id;
        this.firstPlayer = first;
        this.secondPlayer = second;
        this.winner = winner;
    }

}
