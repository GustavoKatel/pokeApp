package ninja.pokeapp.pokeapp.business.remote_player;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 11/22/16.
 */

public class RemotePlayerServer {

    private ServerSocket server;
    private Thread serverThread;
    private HashMap<Socket, Thread> clients;

    private boolean running;

    public RemotePlayerServer() throws IOException {
        this.server = new ServerSocket(0);
        this.clients = new HashMap<>();
        running = true;
        startServerSocket();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void stop() {
        running = false;
    }

    private void startServerSocket() {

        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning()) {
                    try {
                        Socket client = server.accept();
                        Thread th = newClientThread(client);
                        clients.put(client, th);
                        th.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serverThread.start();

    }

    private Thread newClientThread(Socket socket) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());

                    while(isRunning()) {
                        RemotePlayerProtocolMessages msg = (RemotePlayerProtocolMessages) oIn.readObject();
                        switch (msg) {
                            case GET_USER_INFO:
                                oOut.writeObject(AppFacade.getInstancia().getCurrentUser());
                                break;
                            case BATTLE_REQUEST:
                            default:
                                break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ServerSocket getServerSocket() {
        return server;
    }

    public interface RemotePlayerServerListener {

    }

}
