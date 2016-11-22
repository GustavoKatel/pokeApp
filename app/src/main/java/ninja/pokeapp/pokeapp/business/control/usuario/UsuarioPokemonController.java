package ninja.pokeapp.pokeapp.business.control.usuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ninja.pokeapp.pokeapp.business.control.db.DbHelper;
import ninja.pokeapp.pokeapp.business.control.db.DbHelper.UserPokemonEntry;
import ninja.pokeapp.pokeapp.business.control.db.DbVisitor;
import ninja.pokeapp.pokeapp.business.control.pokemon.PokemonController;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.user.Usuario;
import ninja.pokeapp.pokeapp.utils.Config;

/**
 * Created by gustavokatel on 11/20/16.
 */

public class UsuarioPokemonController {

    private Context context;

    public UsuarioPokemonController(Context context) {
        this.context = context;
    }

    public ArrayList<PokemonCaptured> getPokemons(PokemonController pokemonController, String userId) {

        ArrayList<PokemonCaptured> results = new ArrayList<>();

        AppFacade.getInstancia().visitReadableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {

                String[] projection = {
                        UserPokemonEntry._ID,
                        UserPokemonEntry.COLUMN_NAME_BASE_POKEMON_ID,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_ID,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_CP,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_HP,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_EV,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_NAME,
                        UserPokemonEntry.COLUMN_NAME_POKEMON_DATE
                };

                // Filter results WHERE "user_id" = '<id>'
                String selection = UserPokemonEntry.COLUMN_NAME_USER_ID + " = ?";
                String[] selectionArgs = { userId };

                Cursor cursor = db.query(
                        UserPokemonEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                PokemonCaptured pokemon;
                Pokemon pokemonOrig = null;
                GregorianCalendar date;
                if(cursor.moveToFirst()) {

                    do {

                        try {
                            pokemonOrig = pokemonController.getPokemonById(cursor.getString(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_BASE_POKEMON_ID)));
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                        date = new GregorianCalendar();
                        date.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_DATE))));
                        pokemon = new PokemonCaptured(
                                cursor.getString(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_ID)), // id
                                cursor.getString(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_NAME)), // name
                                cursor.getDouble(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_HP)), // hp
                                cursor.getDouble(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_CP)), // cp
                                cursor.getDouble(cursor.getColumnIndex(UserPokemonEntry.COLUMN_NAME_POKEMON_EV)), // ev
                                date, // date
                                pokemonOrig // basePokemon
                        );
                        results.add(pokemon);

                    } while(cursor.moveToNext());

                }

            }
        });

        return results;
    }

    public void savePokemon(PokemonCaptured pokemon, Usuario user) {

        AppFacade.getInstancia().visitWritableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {

                ContentValues values = new ContentValues();
                values.put(UserPokemonEntry.COLUMN_NAME_USER_ID, user.getId());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_ID, pokemon.getId());
                values.put(UserPokemonEntry.COLUMN_NAME_BASE_POKEMON_ID, pokemon.getBasePokemonId());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_CP, pokemon.getCp());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_HP, pokemon.getHp());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_EV, pokemon.getEv());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_NAME, pokemon.getNome());
                values.put(UserPokemonEntry.COLUMN_NAME_POKEMON_DATE, String.valueOf(pokemon.getCapturedDate().getTimeInMillis()));

                String[] projection = {
                    UserPokemonEntry._ID
                };

                // Filter results WHERE "user_id" = '<id>'
                String selection = UserPokemonEntry.COLUMN_NAME_USER_ID + " = ? AND " + UserPokemonEntry.COLUMN_NAME_POKEMON_ID + " = ?";
                String[] selectionArgs = { user.getId(), pokemon.getId() };

                Cursor cursor = db.query(
                        UserPokemonEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );
                if(cursor.getCount()>0) {
                    db.update(UserPokemonEntry.TABLE_NAME, values, selection, selectionArgs);
                } else {
                    db.insert(UserPokemonEntry.TABLE_NAME, null, values);
                }

            }
        });

    }

    public void removePokemon(PokemonCaptured pokemon, Usuario user) {
        AppFacade.getInstancia().visitWritableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {

                String selection = UserPokemonEntry.COLUMN_NAME_USER_ID + " = ? AND " + UserPokemonEntry.COLUMN_NAME_POKEMON_ID + " = ?";
                String[] selectionArgs = {user.getId(), pokemon.getId()};

                db.delete(UserPokemonEntry.TABLE_NAME, selection, selectionArgs);
            }
        });
    }

}
