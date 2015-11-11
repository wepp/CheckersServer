package com.checkers.services;

import com.checkers.domain.vo.Check;
import com.checkers.domain.vo.Game;

import java.util.List;
import java.util.Set;

/**
 * Created by KutsykV on 06.10.2015.
 */
public interface IServerService {
    boolean serverStarted();
    void startServer();

    void stopServer();

    List<Game> getGames();
    int getGamesAmount();

    Game getGame(String gameId, String fieldNum);

    boolean finished(String gameId);

    String getWinner(String gameId);
}
