package ninja.pokeapp.pokeapp.business.remote_player;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.user.Usuario;

import static ninja.pokeapp.pokeapp.business.remote_player.RemotePlayerProtocolMessages.GET_USER_INFO;

/**
 * Created by gustavokatel on 11/22/16.
 */

public class RemotePlayerClient {

    private InetAddress host;
    private int port;

    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;
    private Socket socket;

    private Usuario usuario;
    private ArrayList<RemotePlayerListener> listeners;

    public RemotePlayerClient(InetAddress host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.oIn = new ObjectInputStream(socket.getInputStream());
            this.oOut = new ObjectOutputStream(socket.getOutputStream());

            this.usuario = null;
            this.listeners = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void askForBattle() {

    }

    public void addListener(RemotePlayerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RemotePlayerListener listener) {
        listeners.remove(listener);
    }

    public void getUsuarioAsync() {
        new AsyncIO(new Runnable() {
            @Override
            public void run() {
                try {
                    if(usuario!=null) return;
                    oOut.writeObject(GET_USER_INFO);
                    usuario = (Usuario) oIn.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                for(RemotePlayerListener l : listeners) {
                    l.onUserInfo(usuario);
                }
            }
        });
    }

    private class AsyncIO extends AsyncTask<Void, Void, Void> {

        private Runnable bg, fg;

        public AsyncIO(Runnable bg, Runnable fg) {
            super();
            this.bg = bg;
            this.fg = fg;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bg.run();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fg.run();
        }
    }

    public interface RemotePlayerListener {
        public void onUserInfo(Usuario usuario);
    }

}
