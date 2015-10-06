package com.checkers.domain.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class Server {

    ServerSocket serverSocket = null;

    public void registerServer() {
        try {
            serverSocket = new ServerSocket(8181);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            while (true) {
                createNewBoard(serverSocket.accept(), serverSocket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewBoard(Socket whitePlayer, Socket blackPlayer) {
        new Thread(new GameThread(whitePlayer, blackPlayer)).start();
    }
}
