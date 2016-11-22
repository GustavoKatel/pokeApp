package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.Players;

import java.util.ArrayList;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.business.remote_player.RemotePlayerClient;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 11/20/16.
 */

public class SearchPlayerListViewAdapter extends BaseAdapter {

    private ArrayList<RemotePlayerClient> remotePlayers;

    private LayoutInflater mInflater;


    public SearchPlayerListViewAdapter(Context context, ArrayList<RemotePlayerClient> remotePlayers) {
        super();

        this.remotePlayers = remotePlayers;

        mInflater = LayoutInflater.from(context);
    }

    public ArrayList<RemotePlayerClient> getRemotePlayers() {
        return remotePlayers;
    }

    public void setRemotePlayers(ArrayList<RemotePlayerClient> remotePlayers) {
        this.remotePlayers = remotePlayers;
    }

    @Override
    public int getCount() {
        return remotePlayers.size();
    }

    @Override
    public RemotePlayerClient getItem(int i) {
        return remotePlayers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final RemotePlayerHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.search_player_listview_item, null);

            holder = new RemotePlayerHolder();
            holder.mImgView = (ImageView) view.findViewById(R.id.search_player_listview_item_icon);
            holder.mCaption = (TextView) view.findViewById(R.id.search_player_listview_item_caption);
            holder.mContent = (TextView) view.findViewById(R.id.search_player_listview_item_content);

            view.setTag(holder);
        } else {
            holder = (RemotePlayerHolder) view.getTag();
        }

        final RemotePlayerClient remotePlayer = remotePlayers.get(i);
        View finalView = view;
        remotePlayer.addListener(new RemotePlayerClient.RemotePlayerListener() {
            @Override
            public void onUserInfo(Usuario usuario) {
                remotePlayer.removeListener(this);

                usuario.getGooglePlayer(AppFacade.getInstancia().getGoogleApiClient()).setResultCallback(new ResultCallback<Players.LoadPlayersResult>() {
                    @Override
                    public void onResult(@NonNull Players.LoadPlayersResult loadPlayersResult) {
                        if(loadPlayersResult.getPlayers().getCount()<=0) {
                            return;
                        }
                        Player player = loadPlayersResult.getPlayers().get(0);
                        ImageManager imm = ImageManager.create(finalView.getContext());
                        Uri imgUri = null;
                        if(player.hasIconImage()) {
                            imgUri = player.getIconImageUri();
                        } else if(player.hasHiResImage()) {
                            imgUri = player.getHiResImageUri();
                        }
                        imm.loadImage(holder.mImgView, imgUri);
                    }
                });

                holder.mCaption.setText(usuario.getNome());

            }
        });
        remotePlayer.getUsuarioAsync();

        return view;

    }

    private class RemotePlayerHolder {

        ImageView mImgView;
        TextView mCaption;
        TextView mContent;

    }
}
