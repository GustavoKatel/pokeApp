package ninja.pokeapp.pokeapp.business.facade;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Player;

import ninja.pokeapp.pokeapp.business.commands.*;
import ninja.pokeapp.pokeapp.business.control.db.DbHelper;
import ninja.pokeapp.pokeapp.business.control.db.DbVisitor;
import ninja.pokeapp.pokeapp.business.control.pokemon.PokemonController;
import ninja.pokeapp.pokeapp.business.control.relatorio.RelatorioBatalhaController;
import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioPokemonController;
import ninja.pokeapp.pokeapp.business.memento.Caretaker;
import ninja.pokeapp.pokeapp.business.memento.Memento;
import ninja.pokeapp.pokeapp.business.memento.MementoIterator;
import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.Usuario;
import ninja.pokeapp.pokeapp.business.relatorio.ExportRelatoriosObjectFile;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created on 10/6/16.
 */
public class AppFacade {

    // atributos singleton
    private static AppFacade instancia = null;

    public static void init(Context context) {
        if(instancia==null) {
            instancia = new AppFacade(context);
        }
    }

     public static AppFacade getInstancia() {
        if(instancia==null) {
//            instancia = new AppFacade();
        }

        return instancia;
    }

    // atributos de classe

    private RelatorioBatalhaController relatorioBatalhaController;
    // relatorioCapturaController e relatorioLogin seguem o mesmo esquema de relatorioBatalhaController

    private UsuarioController usuarioController;
    private PokemonController pokemonController;
    private UsuarioPokemonController usuarioPokemonController;

    private Caretaker caretaker;

    private Context context;

    private Usuario currentUser;

    private DbHelper dbHelper;

    private GoogleApiClient googleApiClient;

    protected AppFacade(Context context) {

        this.context = context;

        relatorioBatalhaController = new RelatorioBatalhaController(context, new ExportRelatoriosObjectFile("/tmp/relatorios.data"));
//        relatorioBatalhaController = new RelatorioBatalhaController(new ExportRelatoriosCSV("/tmp/relatorios.csv"));
//        relatorioBatalhaController = new RelatorioBatalhaController(new ExportRelatoriosJSON("/tmp/relatorios.json"));

        usuarioController = new UsuarioController(context);

        pokemonController = new PokemonController(context);
        usuarioPokemonController = new UsuarioPokemonController(context);

        caretaker = new Caretaker();

        dbHelper = new DbHelper(context);
    }

    public AppFacade setContext(Context c) {
        this.context = c;
        return this;
    }


    public void visitWritableDb(DbVisitor visitor) {
        if(visitor==null) return;

        visitor.visit(dbHelper.getWritableDatabase());
    }

    public void visitReadableDb(DbVisitor visitor) {
        if(visitor==null) return;

        visitor.visit(dbHelper.getReadableDatabase());
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;

        try {
            this.saveUsuario(currentUser);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void registraBatalha(Usuario ganhador, Usuario perdedor, PokemonCaptured pokemonGanhador, PokemonCaptured pokemonPerdedor) {
        relatorioBatalhaController.createRelatorio((GregorianCalendar) GregorianCalendar.getInstance(), ganhador, perdedor, pokemonGanhador, pokemonPerdedor);
    }

    public ArrayList<Relatorio> buscaRelatorioBatalhaPorPokemon(PokemonCaptured pokemonCaptured) {
        return relatorioBatalhaController.buscaRelatorioPorPokemon(pokemonCaptured);
    }

    public void saveUsuario(Usuario user) throws Exception {
        CommandInvoker invoker = new CommandInvoker();
        invoker.invoke( new SaveUserCmd(usuarioController), user );
    }

    public Usuario createUsuario(Player gplayer) throws Exception {
        CommandInvoker invoker = new CommandInvoker();
        return (Usuario) invoker.invoke( new CreateUserCmd(usuarioController), gplayer );
    }

    public boolean deleteUsuario(String id) throws Exception {
        CommandInvoker invoker = new CommandInvoker();
        return (boolean) invoker.invoke( new DeleteUserCmd(usuarioController), id );
    }

    public Usuario getUsuario(String id) throws Exception {
        CommandInvoker invoker = new CommandInvoker();
        return (Usuario) invoker.invoke( new GetUserCmd(usuarioController), id );
    }

    public ArrayList<Usuario> buscaNome(String nome) throws NomeInvalidoException {
        return usuarioController.buscaNome(nome);
    }

    public void saveMemento(String id, Memento memento) {
        caretaker.saveMemento(id, memento);
    }

    public void clearMementos(String id) {
        caretaker.clear(id);
    }

    public MementoIterator getMementoIterator(String id) {
        return caretaker.getIterator(id);
    }

    public Pokemon getRandomBasePokemon() {
        return pokemonController.getRandomPokemon();
    }

    public ArrayList<PokemonCaptured> getUserPokemons(String userId) {
        return this.usuarioPokemonController.getPokemons(this.pokemonController, userId);
    }

    public void savePokemonCaptured(PokemonCaptured pokemonCaptured) {
        usuarioPokemonController.savePokemon(pokemonCaptured, currentUser);
    }

    public void removePokemonCaptured(PokemonCaptured pokemonCaptured) {
        usuarioPokemonController.removePokemon(pokemonCaptured, currentUser);
    }

    public void savePokemonCapturedMemento(PokemonCaptured pokemonCaptured) {
        Memento m = pokemonCaptured.createMemento();
        this.saveMemento(pokemonCaptured.getMementoId(), m);
    }

    public void addTestData() {
        Pokemon pokemon = null;
        GregorianCalendar date = (GregorianCalendar) GregorianCalendar.getInstance();
        try {
            pokemon = pokemonController.getPokemonById("001");

            PokemonCaptured pokemonCaptured = new PokemonCaptured(String.valueOf(System.currentTimeMillis()), "bulba3", pokemon.getHp(), pokemon.getCp(), pokemon.getEv(), date, pokemon);
            usuarioPokemonController.savePokemon(pokemonCaptured, currentUser);

            pokemon = pokemonController.getPokemonById("004");

            pokemonCaptured = new PokemonCaptured(String.valueOf(System.currentTimeMillis()), "char char", pokemon.getHp(), pokemon.getCp(), pokemon.getEv(), date, pokemon);
            usuarioPokemonController.savePokemon(pokemonCaptured, currentUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTestData2() {

    }

}
