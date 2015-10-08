package com.checkers.services.impl;

import com.checkers.domain.server.GameThread;
import com.checkers.domain.server.Server;
import com.checkers.services.IServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KutsykV on 06.10.2015.
 */
@Service
public class ServerServiceImpl implements IServerService{

    @Autowired
    private Server server;

    @Override
    public boolean serverStarted() {
        return server.active();
    }

    @Override
    public void startServer() {
        if(!server.active()) {
            server.registerServer();
            server.startServer();
        }
    }

    @Override
    public List<GameThread> getGames() {
        return server.getGames();
    }

    @Override
    public int getGamesAmount() {
        return server.getGamesAmount();
    }
}
