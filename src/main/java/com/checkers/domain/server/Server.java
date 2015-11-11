package com.checkers.domain.server;

import com.checkers.domain.vo.InOutObjectStreams;
import com.checkers.domain.vo.Player;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

/**
 * Created by Isaiev on 01.10.2015.
 * Update  by Kutsyk
 */

@Service
public class Server {

    private ServerSocket recieverSocket = null;
    private Map<Integer, GameThread> games;
    private volatile int gameId = 0;
    
    Server(){
    }

    public boolean active(){
        return recieverSocket != null;
    }

    public void registerServer() {
        try {
            recieverSocket = new ServerSocket(8282);
            games = Maps.newConcurrentMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
        while (true) {
                createNewBoard();
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, GameThread> getGames() {
        return games;
    }

    public void createNewBoard() throws IOException, ClassNotFoundException {
        Player white = null;
        Player black = null;
        while(white == null) {
            InOutObjectStreams whiteStreams = new InOutObjectStreams(recieverSocket.accept());
            String whiteName = whiteStreams.waitForName();
            white = new Player(whiteStreams, whiteName);
            System.out.println("white player nae is "+whiteName);
        }
        while(black == null) {
            InOutObjectStreams blackStreams = new InOutObjectStreams(recieverSocket.accept());
            String blackName = blackStreams.waitForName();
            black = new Player(blackStreams, blackName);
            System.out.println("black player nae is "+blackName);
        }
        gameId++;
        System.out.println("New game with id "+gameId);
        GameThread game = new GameThread(white, black);
        games.put(Integer.valueOf(gameId), game);
        new Thread(game).start();
    }

    public int getGamesAmount(){
        return games.size();
    }
}
