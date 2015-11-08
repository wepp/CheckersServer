package com.checkers.services;

import com.checkers.domain.vo.Game;

import java.util.List;

/**
 * Created by KutsykV on 06.10.2015.
 */
public interface IServerService {
    boolean serverStarted();
    void startServer();
    List<Game> getGames();
    int getGamesAmount();
}
