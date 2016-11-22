package ninja.pokeapp.pokeapp.business.control.pokemon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.utils.AssetsReader;

/**
 * Created by gustavokatel on 11/20/16.
 */

public class PokemonController {

    private HashMap<String, Pokemon> pokemonMap;
    private HashMap<String, Pokemon> pokemonIdMap;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PokemonController(Context context) {

        this.context = context;
        this.pokemonIdMap = new HashMap<>();
        this.pokemonMap = new HashMap<>();

        // load pokemons
        InputStream is = context.getResources().openRawResource(R.raw.pokemon_data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = writer.toString();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray data = json.getJSONArray("data");

            Pokemon pokemon;
            JSONObject pokemonRef;
            String locale = Locale.getDefault().toString();
            String name;
            Drawable img;
            int drawableId;
            for(int i=0;i<data.length();i++) {
                pokemonRef = data.getJSONObject(i);
                if(pokemonRef.getJSONObject("name").has(locale)) {
                    name = pokemonRef.getJSONObject("name").getString(locale);
                } else {
                    name = pokemonRef.getJSONObject("name").getString("default");
                }

                img = AssetsReader.getDrawableFromAssets(context, "pokemon-imgs/pokemon-"+pokemonRef.getString("id")+".png");

                pokemon = new Pokemon(
                        pokemonRef.getString("id"),
                        name,
                        pokemonRef.getDouble("hp"),
                        pokemonRef.getDouble("cp"),
                        pokemonRef.getDouble("ev"),
                        img
                        );

                this.pokemonMap.put(name, pokemon);
                this.pokemonIdMap.put(pokemon.getId(), pokemon);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Pokemon getPokemonByName(String name) throws Exception {
        if(pokemonMap.containsKey(name)) {
            return pokemonMap.get(name);
        }

        throw new Exception("Pokemon not found");
    }

    public Pokemon getPokemonById(String id) throws Exception {
        if(pokemonIdMap.containsKey(id)) {
            return pokemonIdMap.get(id);
        }

        throw new Exception("Pokemon not found");
    }

    public Pokemon getRandomPokemon() {
        Random random = new Random(System.currentTimeMillis());
        int pos = random.nextInt(pokemonMap.keySet().size());
        Pokemon pokemon = pokemonMap.get(pokemonMap.keySet().toArray()[pos]);
        Pokemon newPokemon = new Pokemon(pokemon.getId(), pokemon.getNome(), pokemon.getHp(), pokemon.getCp(), pokemon.getEv(), pokemon.getImg());
        return newPokemon;
    }

}
