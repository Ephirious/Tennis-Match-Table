package com.ephirious.entity;

import com.ephirious.model.value.PlayerName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = PlayerName.MAX_LENGTH, unique = true)
    private String name;


    public PlayerJpaEntity(@NonNull UUID id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public PlayerJpaEntity(@NonNull UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PlayerJpaEntity entity = (PlayerJpaEntity) obj;

        return this.id.equals(entity.id) &&
               Objects.equals(name, entity.name);
    }
}
