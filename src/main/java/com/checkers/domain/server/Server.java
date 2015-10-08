package com.checkers.domain.server;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Isaiev on 01.10.2015.
 * Update  by Kutsyk
 */

@Service
public class Server {

    private ServerSocket recieverSocket = null;
    private List<GameThread> games;
    
    Server(){
    }

    public boolean active(){
        return recieverSocket != null;
    }

    public void registerServer() {
        try {
            recieverSocket = new ServerSocket(8181);
            games = new LinkedList<GameThread>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            while (true) {
                createNewBoard(recieverSocket.accept(), recieverSocket.accept());
                checkPlayedGames();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GameThread> getGames() {
        return games;
    }

    public void createNewBoard(Socket whitePlayer, Socket blackPlayer) {
        GameThread newGame = new GameThread(whitePlayer, blackPlayer);
        games.add(newGame);
        newGame.start();
    }

    private void checkPlayedGames(){
        for(GameThread game: games){
            if(game.winner() != null){
                game.stop();
                game.destroy();
                games.remove(game);
                System.out.println("Game finished: "+game);
            }
        }
    }

    public int getGamesAmount(){
        return games.size();
    }
}
