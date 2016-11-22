package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.business.memento.Memento;
import ninja.pokeapp.pokeapp.business.memento.MementoIterator;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;

import static ninja.pokeapp.pokeapp.utils.FormatUtils.formatPokemonStats;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PokemonCapturedFragment.PokemonCapturedFragmentListener} interface
 * to handle interaction events.
 * Use the {@link PokemonCapturedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonCapturedFragment extends AbstractFragment implements TextWatcher {
    private static final String ARG_PARAM1 = "pokemonCaptured";

    private PokemonCaptured pokemonCaptured;
    private PokemonCapturedFragmentListener mListener;
    private boolean processingMemento;
    private boolean removingPokemon;
    private MementoIterator mementoIterator;

    public PokemonCapturedFragment() {
        super("pokemoncaptured-fragment");
        processingMemento = false;
        removingPokemon = false;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pokemon Parameter 1.
     * @return A new instance of fragment PokemonCapturedFragment.
     */
    public static PokemonCapturedFragment newInstance(PokemonCaptured pokemon) {
        PokemonCapturedFragment fragment = new PokemonCapturedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, pokemon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            pokemonCaptured = (PokemonCaptured) getArguments().getSerializable(ARG_PARAM1);
            mementoIterator = AppFacade.getInstancia().getMementoIterator(pokemonCaptured.getMementoId());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_captured, container, false);

        ((EditText)view.findViewById(R.id.pokemoncaptured_name)).setText(pokemonCaptured.getNome());
        ((EditText)view.findViewById(R.id.pokemoncaptured_name)).addTextChangedListener(this);
        ((EditText)view.findViewById(R.id.pokemoncaptured_name)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        ((TextView)view.findViewById(R.id.pokemoncaptured_cp)).setText( formatPokemonStats(pokemonCaptured.getCp(), pokemonCaptured.getBasePokemon().getCp()) );
        ((TextView)view.findViewById(R.id.pokemoncaptured_hp)).setText( formatPokemonStats(pokemonCaptured.getHp(), pokemonCaptured.getBasePokemon().getHp()) );
        ((TextView)view.findViewById(R.id.pokemoncaptured_ev)).setText( formatPokemonStats(pokemonCaptured.getEv(), pokemonCaptured.getBasePokemon().getEv()) );
        ((ImageView)view.findViewById(R.id.pokemoncaptured_img)).setImageDrawable(pokemonCaptured.getBasePokemon().getImg());

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(pokemonCaptured.getCapturedDate().getTime());

        ((TextView)view.findViewById(R.id.pokemoncaptured_date)).setText( date );

        RelatoriosListViewAdapter adapter = new RelatoriosListViewAdapter(view.getContext(), AppFacade.getInstancia().buscaRelatorioBatalhaPorPokemon(pokemonCaptured), null, pokemonCaptured);
        ((ListView)view.findViewById(R.id.pokemoncaptured_relatorios_lv)).setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PokemonCapturedFragmentListener) {
            mListener = (PokemonCapturedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(!removingPokemon)
            save();
        mementoIterator.commit();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pokemoncaptured_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pokemoncaptured_menu_undo:
                restoreMemento();
                return true;
            case R.id.pokemoncaptured_menu_redo:
                redoMemento();
                return true;
            case R.id.pokemoncaptured_menu_remove:
                removePokemon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restoreMemento() {
        if(!mementoIterator.hasPrev()) return;
        processingMemento = true;
        Memento memento = mementoIterator.prev();

        AppFacade.getInstancia().savePokemonCapturedMemento(pokemonCaptured);
        mementoIterator = AppFacade.getInstancia().getMementoIterator(pokemonCaptured.getMementoId());

        pokemonCaptured.setMemento(memento);
        ((EditText)getView().findViewById(R.id.pokemoncaptured_name)).setText(pokemonCaptured.getNome());
        save();
        processingMemento = false;
    }

    private void redoMemento() {
        if(!mementoIterator.hasNext()) return;
        processingMemento = true;
        Memento memento = mementoIterator.next();
        pokemonCaptured.setMemento(memento);
        ((EditText)getView().findViewById(R.id.pokemoncaptured_name)).setText(pokemonCaptured.getNome());
        save();
        processingMemento = false;
    }

    private void removePokemon() {
        AppFacade.getInstancia().removePokemonCaptured(pokemonCaptured);
        removingPokemon = true;
        getActivity().onBackPressed();
    }

    private void save() {
        AppFacade.getInstancia().savePokemonCaptured(pokemonCaptured);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(processingMemento) return;
        AppFacade.getInstancia().savePokemonCapturedMemento(pokemonCaptured);
        mementoIterator = AppFacade.getInstancia().getMementoIterator(pokemonCaptured.getMementoId());
        pokemonCaptured.setNome(((EditText)getView().findViewById(R.id.pokemoncaptured_name)).getText().toString());
        save();
        Snackbar.make(getView(), R.string.pokemon_saved, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreMemento();
            }
        }).show();
    }

    public interface PokemonCapturedFragmentListener {

    }


}
