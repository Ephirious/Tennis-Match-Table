package com.ephirious.model.value.score.game;

import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;

public abstract class AbstractGameScore<T> implements Score<T> {
    @Override
    public abstract AbstractGameScore<?> pointTo(PlayerSide side);
}
