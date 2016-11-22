package ninja.pokeapp.pokeapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gustavokatel on 11/9/16.
 */

public interface SerializableJson {

    public JSONObject toJson() throws JSONException;

}
