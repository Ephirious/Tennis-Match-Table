package com.ephirious.entity;

import com.ephirious.model.value.PlayerName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    public PlayerJpaEntity(UUID id, String name) {
        ensureId(id);
        this.id = id;
        this.name = name;
    }

    public PlayerJpaEntity(UUID id) {
        ensureId(id);
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

    private void ensureId(UUID id) {
        if (id == null) {
            throw new IllegalStateException("PlayerJpaEntity requires not null id");
        }
    }
}
