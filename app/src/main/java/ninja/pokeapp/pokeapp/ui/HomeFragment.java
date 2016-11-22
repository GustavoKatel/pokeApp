package ninja.pokeapp.pokeapp.ui;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Player;

import ninja.pokeapp.pokeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends AbstractFragment implements View.OnClickListener {

    private HomeFragmentListener mListener;
    private FloatingActionButton fab;

    private Player currentPlayer;

    public HomeFragment() {
        super("home-fragment");
    }

    public static HomeFragment newInstance(Player currentPlayer) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("player", currentPlayer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPlayer = getArguments().getParcelable("player");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.home_fab);
        fab.setOnClickListener(this);

        ((TextView)v.findViewById(R.id.home_tv_user_name)).setText(currentPlayer.getDisplayName());

        Uri imgUri = null;
        if(currentPlayer.hasIconImage()) {
            imgUri = currentPlayer.getIconImageUri();
        } else if(currentPlayer.hasHiResImage()) {
            imgUri = currentPlayer.getHiResImageUri();
        }
        ImageManager imgManager = ImageManager.create(getContext());
        imgManager.loadImage(((ImageView)v.findViewById(R.id.home_img)), imgUri, R.drawable.ic_menu_camera);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            mListener = (HomeFragmentListener) context;
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
        if(view.getId() == R.id.home_fab && mListener != null) {
            mListener.onFloatIconClicked();
        }
    }

    public interface HomeFragmentListener {
        public void onFloatIconClicked();
    }

}
