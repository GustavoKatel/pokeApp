package ninja.pokeapp.pokeapp.business.control.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by gustavokatel on 11/22/16.
 */

public interface DbVisitor {

    public void visit(SQLiteDatabase db);

}
