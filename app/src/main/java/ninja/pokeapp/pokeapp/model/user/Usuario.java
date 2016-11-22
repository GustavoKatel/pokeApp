package ninja.pokeapp.pokeapp.model.user;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.Players;

import ninja.pokeapp.pokeapp.business.memento.CreatesMemento;
import ninja.pokeapp.pokeapp.business.memento.Memento;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created on 10/6/16.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private String googleId;

    public Usuario(String id, String nome, String googleId) {
        this.id = id;
        this.nome = nome;
        this.googleId = googleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public PendingResult<Players.LoadPlayersResult> getGooglePlayer(GoogleApiClient client) {
        return Games.Players.loadPlayer(client, this.googleId);
    }

    public boolean equals(Usuario ot) {
        return this.nome.equals(ot) && this.id.equals(ot.id);
    }

}
