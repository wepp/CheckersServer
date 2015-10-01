package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class Server {

    public static void main (String[] args){
        ServerSocket servsock = null;
        try {
            servsock = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server registered at port "+args[0]);
            while (true) {
                Socket s1 = servsock.accept();
                System.out.println("White gamer connected");
                Socket s2 = servsock.accept();
                System.out.println("Black gamer connected");
                new Thread(new GameThread(s1,s2)).start();
                System.out.println("Two gamers connected! Start!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Server error! "+e);
        } catch (IOException e) {
            System.err.println("Server error! "+e);
        }finally {
            assert servsock != null;
            try {
                servsock.close();
            } catch (IOException e) {
                System.err.println("Server error! "+e);
            }
        }
    }
}
