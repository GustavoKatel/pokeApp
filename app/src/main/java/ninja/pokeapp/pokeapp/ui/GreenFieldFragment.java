package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.Random;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.control.wild_pokemon.EqIvWildPokemonBuilder;
import ninja.pokeapp.pokeapp.business.control.wild_pokemon.WildPokemonBuilder;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.utils.FormatUtils;

import static ninja.pokeapp.pokeapp.R.string.name;

public class GreenFieldFragment extends AbstractFragment implements View.OnClickListener {

    private GreenFieldFragmentListener mListener;
    private Pokemon basePokemon;
    private WildPokemonBuilder pokemonBuilder;
    private PokemonCaptured pokemonCaptured;

    public GreenFieldFragment() {
        super("greenfield-fragment");
    }

    public static GreenFieldFragment newInstance() {
        GreenFieldFragment fragment = new GreenFieldFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void generateWildPokemon() {
        Random random = new Random(System.currentTimeMillis());
        basePokemon = AppFacade.getInstancia().getRandomBasePokemon();
        pokemonBuilder = new EqIvWildPokemonBuilder(basePokemon);
        pokemonBuilder.buildCp();
        pokemonBuilder.buildEv();
        pokemonBuilder.buildHp();
        pokemonCaptured = pokemonBuilder.getWildPokemon();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        generateWildPokemon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_green_field, container, false);

        ((ImageView)view.findViewById(R.id.greenfield_pokemon_img)).setImageDrawable(basePokemon.getImg());
        ((TextView)view.findViewById(R.id.greenfield_pokemon_name)).setText(basePokemon.getNome());
        ((TextView)view.findViewById(R.id.greenfield_pokemon_hp)).setText( FormatUtils.formatPokemonStats(pokemonCaptured.getHp(), basePokemon.getHp()) );
        ((TextView)view.findViewById(R.id.greenfield_pokemon_cp)).setText( FormatUtils.formatPokemonStats(pokemonCaptured.getCp(), basePokemon.getCp()) );
        ((TextView)view.findViewById(R.id.greenfield_pokemon_ev)).setText( FormatUtils.formatPokemonStats(pokemonCaptured.getEv(), basePokemon.getEv()) );
        ((ImageButton)view.findViewById(R.id.greenfield_capture_bt)).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg = String.format(getResources().getString(R.string.a_wild_pokemon_appears), basePokemon.getNome());
        builder.setMessage(msg)
                .setTitle(R.string.app_name)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GreenFieldFragmentListener) {
            mListener = (GreenFieldFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.greenfield_capture_bt) {
            createCaptured();
        }
    }

    private void createCaptured() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_name);

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setText(basePokemon.getNome());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if(name.trim().isEmpty()) {
                    createCaptured();
                    return;
                }

                pokemonCaptured.setNome(name);
                AppFacade.getInstancia().savePokemonCaptured(pokemonCaptured);
                if(mListener != null) {
                    mListener.onPokemonCaptured(pokemonCaptured);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public interface GreenFieldFragmentListener {
        void onPokemonCaptured(PokemonCaptured pokemonCaptured);
    }
}
