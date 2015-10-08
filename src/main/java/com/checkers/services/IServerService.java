package com.checkers.services;

import com.checkers.domain.server.GameThread;

import java.util.List;

/**
 * Created by KutsykV on 06.10.2015.
 */
public interface IServerService {
    boolean serverStarted();
    void startServer();
    List<GameThread> getGames();
    int getGamesAmount();
}
