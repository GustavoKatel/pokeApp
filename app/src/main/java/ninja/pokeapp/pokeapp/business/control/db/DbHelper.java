package ninja.pokeapp.pokeapp.business.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioPokemonController;
import ninja.pokeapp.pokeapp.utils.Config;

/**
 * Created by gustavokatel on 11/21/16.
 */

public class DbHelper extends SQLiteOpenHelper {

    private ArrayList<String> createTables;
    private ArrayList<String> deleteTables;

    public DbHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);

        createTables = new ArrayList<>();
        deleteTables = new ArrayList<>();

        // Usuario
        createTables.add("CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserEntry.COLUMN_NAME_USER_ID + " TEXT" + "," +
                UserEntry.COLUMN_NAME_USER_NAME + " TEXT" + "," +
                UserEntry.COLUMN_NAME_USER_GOOGLE_ID + " TEXT" + " )");
        deleteTables.add("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);

        // UsuarioPokemon
        createTables.add("CREATE TABLE " + UserPokemonEntry.TABLE_NAME + " (" +
                UserPokemonEntry._ID + " INTEGER PRIMARY KEY," +
                UserPokemonEntry.COLUMN_NAME_USER_ID + " TEXT" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_CP + " REAL" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_HP + " REAL" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_EV + " REAL" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_NAME + " TEXT" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_DATE + " TEXT" + "," +
                UserPokemonEntry.COLUMN_NAME_BASE_POKEMON_ID + " TEXT" + "," +
                UserPokemonEntry.COLUMN_NAME_POKEMON_ID + " TEXT" + " )");
        deleteTables.add("DROP TABLE IF EXISTS " + UserPokemonEntry.TABLE_NAME);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        for(String query : createTables) {
            sqLiteDatabase.execSQL(query);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        for(String query : deleteTables) {
            sqLiteDatabase.execSQL(query);
        }

        onCreate(sqLiteDatabase);

    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        public static final String COLUMN_NAME_USER_GOOGLE_ID = "google_id";
    }

    public static class UserPokemonEntry implements BaseColumns {
        public static final String TABLE_NAME = "userpokemon";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_POKEMON_ID = "pokemon_id";
        public static final String COLUMN_NAME_BASE_POKEMON_ID = "base_pokemon_id";
        public static final String COLUMN_NAME_POKEMON_CP = "pokemon_cp";
        public static final String COLUMN_NAME_POKEMON_HP = "pokemon_hp";
        public static final String COLUMN_NAME_POKEMON_EV = "pokemon_ev";
        public static final String COLUMN_NAME_POKEMON_NAME = "pokemon_name";
        public static final String COLUMN_NAME_POKEMON_DATE = "pokemon_date";
    }

}
