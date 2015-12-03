package com.checkers.domain.server;

import com.checkers.domain.vo.InOutObjectStreams;
import com.checkers.domain.vo.Player;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Isaiev on 01.10.2015.
 * Update  by Kutsyk
 */

@Service
public class Server {

    private ServerSocket recieverSocket = null;
    private Map<Integer, GameThread> games;
    private ArrayList<Socket> sockets = new ArrayList<Socket>();
    private volatile int gameId = 0;
    private volatile boolean started;

    Server() {
    }

    public boolean active() {
        return recieverSocket != null && games != null && !games.isEmpty() && started;
    }

    public void registerServer() {
    }

    public void startServer() {
        try {
            if (!active()) {
                stop();
                recieverSocket = new ServerSocket(8282);
                games = Maps.newConcurrentMap();
                started = true;
                while (started) {
                    createNewBoard();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            stop();
        }
    }

    public Map<Integer, GameThread> getGames() {
        return games;
    }

    private Socket rememberSocket(Socket socket) {
        sockets.add(socket);
        return socket;
    }

    public void createNewBoard() throws IOException, ClassNotFoundException {
        Player white = null;
        Player black = null;
        while (white == null) {
            InOutObjectStreams whiteStreams = new InOutObjectStreams(rememberSocket(recieverSocket.accept()));
            String whiteName = whiteStreams.waitForName();
            white = new Player(whiteStreams, whiteName);
            System.out.println("white player nae is " + whiteName);
        }
        while (black == null) {
            InOutObjectStreams blackStreams = new InOutObjectStreams(rememberSocket(recieverSocket.accept()));
            String blackName = blackStreams.waitForName();
            black = new Player(blackStreams, blackName);
            System.out.println("black player nae is " + blackName);
        }
        gameId++;
        System.out.println("New game with id " + gameId);
        GameThread game = new GameThread(white, black);
        games.put(Integer.valueOf(gameId), game);
        new Thread(game).start();
    }

    public int getGamesAmount() {
        if(active())
            return games.size();
        return 0;
    }

    public void stop() {
        try {
            started = false;
            for (Socket s : sockets)
            if(s != null)
                s.close();
            if(sockets != null)
                sockets.clear();
            if(recieverSocket != null)
                recieverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recieverSocket = null;
    }
}
