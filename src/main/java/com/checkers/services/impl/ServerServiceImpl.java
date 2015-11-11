package com.checkers.services.impl;

import com.checkers.domain.server.GameThread;
import com.checkers.domain.server.Server;
import com.checkers.domain.vo.Game;
import com.checkers.services.IServerService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Game> getGames() {
        List<Game> games = Lists.newArrayList();
        for(Map.Entry<Integer, GameThread> integerGameThreadEntry: server.getGames().entrySet()){
            games.add(new Game(integerGameThreadEntry.getKey(),
                    integerGameThreadEntry.getValue().getWhite().getWhiteName(),
                    integerGameThreadEntry.getValue().getBlack().getWhiteName(),
                    integerGameThreadEntry.getValue().gameFinished(),
                    integerGameThreadEntry.getValue().gameFinished() ?
                            integerGameThreadEntry.getValue().getWinner().getWhiteName()
                            : null));
        }
        return games;
    }

    @Override
    public int getGamesAmount() {
        return server.getGamesAmount();
    }

    @Override
    public Game getGame(String gameId, String fieldNum) {
        final Integer id = Integer.valueOf(gameId);
        final Integer fieldNumInt = Integer.valueOf(fieldNum);
        if(!server.getGames().containsKey(id))
            return new Game();
        GameThread game = server.getGames().get(id);
        if(game.getGameStoryStrings().size() > fieldNumInt) {
            return new Game(id,
                    game.getWhite().getWhiteName(),
                    game.getBlack().getWhiteName(),
                    game.gameFinished(),
                    game.gameFinished() ?
                            game.getWinner().getWhiteName()
                            :null,
                    game.getGameStoryStrings().get(fieldNumInt).getAllChecks());
        } else {
            return new Game(id,
                    game.getWhite().getWhiteName(),
                    game.getBlack().getWhiteName(),
                    game.gameFinished(),
                    game.gameFinished() ?
                            game.getWinner().getWhiteName()
                            :null,
                    null);
        }
    }

    @Override
    public boolean finished(String gameId) {
        final Integer id = Integer.valueOf(gameId);
        GameThread game = server.getGames().get(id);
        return game.getGameStory().isEmpty();
    }


    @Override
    public String getWinner(String gameId){
        final Integer id = Integer.valueOf(gameId);
        GameThread game = server.getGames().get(id);
        return game.getWinner().getWhiteName();
    }
}
