package ninja.pokeapp.pokeapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.user.Usuario;
import ninja.pokeapp.pokeapp.ui.UserPokemonsFragment.UserPokemonsFragmentListener;

import java.util.List;

import static ninja.pokeapp.pokeapp.utils.FormatUtils.formatPokemonStats;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured} and makes a call to the
 * specified {@link UserPokemonsFragmentListener}.
 */
public class MyPokemonCapturedItemRecyclerViewAdapter extends RecyclerView.Adapter<MyPokemonCapturedItemRecyclerViewAdapter.ViewHolder> {

    private final List<PokemonCaptured> mValues;
    private final UserPokemonsFragmentListener mListener;

    public MyPokemonCapturedItemRecyclerViewAdapter(Usuario user, UserPokemonsFragmentListener listener) {
        mValues = AppFacade.getInstancia().getUserPokemons(user.getId());
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pokemoncaptureditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nImgView.setImageDrawable(mValues.get(position).getBasePokemon().getImg());
        holder.mNameView.setText(mValues.get(position).getNome());
        holder.mHpView.setText( formatPokemonStats(mValues.get(position).getHp(), mValues.get(position).getBasePokemon().getHp()) );
        holder.mCpView.setText( formatPokemonStats(mValues.get(position).getCp(), mValues.get(position).getBasePokemon().getCp()) );
        holder.mEvView.setText( formatPokemonStats(mValues.get(position).getEv(), mValues.get(position).getBasePokemon().getEv()) );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPokemonCapturedSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView nImgView;
        public final TextView mNameView;
        public final TextView mHpView;
        public final TextView mCpView;
        public final TextView mEvView;
        public PokemonCaptured mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nImgView = (ImageView) view.findViewById(R.id.pokemoncaptured_item_img);
            mNameView = (TextView) view.findViewById(R.id.pokemoncaptured_item_name);
            mHpView = (TextView) view.findViewById(R.id.pokemoncaptured_item_hp);
            mCpView = (TextView) view.findViewById(R.id.pokemoncaptured_item_cp);
            mEvView = (TextView) view.findViewById(R.id.pokemoncaptured_item_ev);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
