package com.ephirious.dto.response;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.match.*;

import java.util.Objects;

public record MatchStatusDto(
        PlayerPointsMatchDto firstPlayer,
        PlayerPointsMatchDto secondPlayer,
        String winnerName
) {

    public static MatchStatusDto fromMatch(Match match, Player first, Player second, Player winner) {
        return new MatchStatusDto(
                map(match, first),
                map(match, second),
                winner != null ? winner.name().value() : null
        );
    }

    private static PlayerPointsMatchDto map(Match match, Player player) {
        MatchScore matchScore = match.score();
        SetScore setScore = matchScore.currentSet();
        GameScore gameScore = setScore.currentGame();
        boolean isFirst = Objects.equals(match.firstPlayerId(), player.id());

        int sets = isFirst ? matchScore.firstPlayerSetPoint() : matchScore.secondPlayerSetPoint();
        int games = isFirst ? setScore.firstPlayerGamePoints() : setScore.secondPlayerGamePoints();

        String pointsAsString = switch (gameScore) {
            case FinalStandardGame _, StandardGame _ -> isFirst
                    ? gameScore.firstPlayerPoints()
                    : gameScore.secondPlayerPoints();
            case  TieBreakGame _ -> null;
            default -> throw new IllegalStateException("Unavailable GameScore implementation");
        };

        Integer pointsAsInt = switch (gameScore) {
            case TieBreakGame _ -> isFirst
                    ? Integer.parseInt(gameScore.firstPlayerPoints())
                    : Integer.parseInt(gameScore.secondPlayerPoints());
            case FinalStandardGame _, StandardGame _ -> null;
            default -> throw new IllegalStateException("Unavailable GameScore implementation");
        };

        return new PlayerPointsMatchDto(
                player.name().value(),
                pointsAsString,
                games,
                sets,
                pointsAsInt
        );
    }
}
