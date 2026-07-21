package com.ephirious.junit.model;

import com.ephirious.model.value.match.PlayerSide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

final class EventGameBuilder {
    private EventGameBuilder() {

    }

    @SafeVarargs
    static List<PlayerSide> sequence(Object ... seq) {
        List<PlayerSide> result = new ArrayList<>(seq.length);
        for (int i = 0; i < seq.length; i += 2) {
            int count = (int) seq[i + 1];
            PlayerSide currentSide = (PlayerSide) seq[i];
            result.addAll(Collections.nCopies(count, currentSide));
        }
        return result;
    }
}
