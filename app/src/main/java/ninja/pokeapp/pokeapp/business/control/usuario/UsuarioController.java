package ninja.pokeapp.pokeapp.business.control.usuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.android.gms.games.Player;

import ninja.pokeapp.pokeapp.business.control.db.DbHelper;
import ninja.pokeapp.pokeapp.business.control.db.DbHelper.UserEntry;
import ninja.pokeapp.pokeapp.business.control.db.DbVisitor;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.user.GooglePlayerInvalidoException;
import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.Usuario;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.UsuarioNaoExisteException;
import ninja.pokeapp.pokeapp.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gustavokatel on 10/17/16.
 */
public class UsuarioController {

    private Context context;

    public UsuarioController(Context context) {

        this.context = context;

    }

    public void saveUsuario(Usuario user) throws NomeInvalidoException, IdInvalidoException {

        if(user.getNome()==null || user.getNome().isEmpty()) {
            throw new NomeInvalidoException();
        }

        if(user.getId()==null || user.getId().isEmpty()) {
            throw new IdInvalidoException();
        }

        String selection = UserEntry.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = { user.getId() };

        String[] projection = {
                UserEntry._ID
        };

        AppFacade.getInstancia().visitWritableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {

                Cursor cursor = db.query(
                        UserEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                ContentValues values = new ContentValues();
                values.put(UserEntry.COLUMN_NAME_USER_ID, user.getId());
                values.put(UserEntry.COLUMN_NAME_USER_NAME, user.getNome());
                values.put(UserEntry.COLUMN_NAME_USER_GOOGLE_ID, user.getGoogleId());

                if(cursor.getCount()>0) {
                    db.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
                } else {
                    db.insert(UserEntry.TABLE_NAME, null, values);
                }
            }
        });

    }

    public Usuario createUsuario(Player gplayer) throws GooglePlayerInvalidoException {

        if(gplayer==null) {
            throw new GooglePlayerInvalidoException();
        }

        Usuario user = new Usuario(gplayer.getPlayerId(), gplayer.getDisplayName(), gplayer.getPlayerId());

        AppFacade.getInstancia().visitWritableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {
                ContentValues values = new ContentValues();
                values.put(UserEntry.COLUMN_NAME_USER_ID, user.getId());
                values.put(UserEntry.COLUMN_NAME_USER_NAME, user.getNome());
                values.put(UserEntry.COLUMN_NAME_USER_GOOGLE_ID, user.getGoogleId());

                db.insert(UserEntry.TABLE_NAME, null, values);
            }
        });

        return user;

    }

    public boolean deleteUsuario(String id) throws IdInvalidoException, UsuarioNaoExisteException {

        if(id==null || id.isEmpty()) {
            throw new IdInvalidoException();
        }

        AppFacade.getInstancia().visitWritableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {
                db.delete(UserEntry.TABLE_NAME, UserEntry.COLUMN_NAME_USER_ID + " = ?", new String[]{id});
            }
        });

        return false;

    }

    public Usuario getUsuario(String id) throws IdInvalidoException, UsuarioNaoExisteException {

        if(id==null || id.isEmpty()) {
            throw new IdInvalidoException();
        }

        String[] projection = {
            UserEntry._ID,
            UserEntry.COLUMN_NAME_USER_ID,
            UserEntry.COLUMN_NAME_USER_NAME,
            UserEntry.COLUMN_NAME_USER_GOOGLE_ID
        };

        // Filter results WHERE "user_id" = '<id>'
        String selection = UserEntry.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = { id};

        final Cursor[] cursor = {null};

        AppFacade.getInstancia().visitReadableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {
                cursor[0] = db.query(
                        UserEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );
            }
        });

        if(cursor[0] !=null && cursor[0].moveToFirst()) {
            return new Usuario(
                    cursor[0].getString(cursor[0].getColumnIndex(UserEntry.COLUMN_NAME_USER_ID)),
                    cursor[0].getString(cursor[0].getColumnIndex(UserEntry.COLUMN_NAME_USER_NAME)),
                    cursor[0].getString(cursor[0].getColumnIndex(UserEntry.COLUMN_NAME_USER_GOOGLE_ID))
            );
        }

        throw new UsuarioNaoExisteException();

    }

    public ArrayList<Usuario> buscaNome(String nome) throws NomeInvalidoException {
        if(nome==null || nome.isEmpty()) {
            throw new NomeInvalidoException();
        }

        ArrayList<Usuario> results = new ArrayList<>();

        String[] projection = {
                UserEntry._ID,
                UserEntry.COLUMN_NAME_USER_ID,
                UserEntry.COLUMN_NAME_USER_NAME,
                UserEntry.COLUMN_NAME_USER_GOOGLE_ID
        };

        // Filter results WHERE "user_id" = '<id>'
        String selection = UserEntry.COLUMN_NAME_USER_NAME + " LIKE \"%?%\"";
        String[] selectionArgs = { nome };

        AppFacade.getInstancia().visitReadableDb(new DbVisitor() {
            @Override
            public void visit(SQLiteDatabase db) {
                Cursor cursor = db.query(
                        UserEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                if(cursor.moveToFirst()) {

                    do {
                        results.add(new Usuario(
                                cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID)),
                                cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_NAME)),
                                cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_GOOGLE_ID))
                        ));
                    } while(cursor.moveToNext());
                }
            }
        });

        return results;

    }

}
