package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;
import ninja.pokeapp.pokeapp.model.relatorio.RelatorioBatalha;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 11/20/16.
 */

public class RelatoriosListViewAdapter extends BaseAdapter {

    private Usuario user;
    private PokemonCaptured pokemonCaptured;
    private ArrayList<Relatorio> relatorios;

    private LayoutInflater mInflater;


    public RelatoriosListViewAdapter(Context context, ArrayList<Relatorio> relatorios, Usuario highlightUser, PokemonCaptured highlightPokemon) {
        super();

        this.user = highlightUser;
        this.pokemonCaptured = highlightPokemon;
        this.relatorios = relatorios;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return relatorios.size();
    }

    @Override
    public Relatorio getItem(int i) {
        return relatorios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RelatorioHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.relatorios_listview_item, null);

            holder = new RelatorioHolder();
            holder.mImgView = (ImageView) view.findViewById(R.id.relatorios_listview_item_icon);
            holder.mCaption = (TextView) view.findViewById(R.id.relatorios_listview_item_caption);
            holder.mContent = (TextView) view.findViewById(R.id.relatorios_listview_item_content);

            view.setTag(holder);
        } else {
            holder = (RelatorioHolder) view.getTag();
        }

        Relatorio relatorio = relatorios.get(i);

        String caption = view.getResources().getString(R.string.report);
        Drawable icon = view.getResources().getDrawable(R.drawable.ic_assignment_black_24dp, null);

        if(relatorio instanceof RelatorioBatalha) {
            RelatorioBatalha batalha = (RelatorioBatalha) relatorio;

            icon = view.getResources().getDrawable(R.drawable.dr_pokeball, null);

            if(user != null) {

                if (batalha.getGanhador().getId().equals(user.getId())) {
                    caption = view.getResources().getString(R.string.win);
                } else if(batalha.getPerdedor().getId().equals(user.getId())) {
                    caption = view.getResources().getString(R.string.lose);
                }
            }

            if(pokemonCaptured != null) {
                if (batalha.getPokemonGanhador().getId().equals(pokemonCaptured.getId())) {
                    caption = view.getResources().getString(R.string.win);
                } else if(batalha.getPokemonPerdedor().getId().equals(pokemonCaptured.getId())) {
                    caption = view.getResources().getString(R.string.lose);
                }
            }

        }

        holder.mCaption.setText(caption);
        holder.mContent.setText(relatorio.toString());

        return view;

    }

    private class RelatorioHolder {

        ImageView mImgView;
        TextView mCaption;
        TextView mContent;

    }
}
