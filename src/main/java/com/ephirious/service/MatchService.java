package com.ephirious.service;

import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import com.ephirious.repository.CompletedMatchRepository;
import com.ephirious.repository.OngoingMatchRepository;
import com.ephirious.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final OngoingMatchRepository ongoing;
    private final CompletedMatchRepository completed;
    private final PlayerRepository players;

    public CreatedMatchDto createMatch(PlayerName first, PlayerName second) {
        Player firstPlayer = players.add(new Player(first));
        Player secondPlayer = players.add(new Player(second));

        Match match = new Match(firstPlayer.id(), secondPlayer.id());

        ongoing.add(match);

        return new CreatedMatchDto(match.id());
    }
}
