package ninja.pokeapp.pokeapp.ui;

import android.support.v4.app.Fragment;

/**
 * Created by gustavokatel on 11/20/16.
 */

public abstract class AbstractFragment extends Fragment {

    private String ident;

    public AbstractFragment(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

}
