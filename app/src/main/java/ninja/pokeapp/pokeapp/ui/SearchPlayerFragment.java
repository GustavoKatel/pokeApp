package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.net.InetAddress;
import java.util.ArrayList;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.remote_player.RemotePlayerClient;
import ninja.pokeapp.pokeapp.model.user.Usuario;

public class SearchPlayerFragment extends AbstractFragment implements NsdManager.DiscoveryListener, AdapterView.OnItemClickListener {

    private SearchPlayerFragmentListener mListener;

    private ProgressBar progressBar;
    private SearchPlayerListViewAdapter adapter;
    private ArrayList<RemotePlayerClient> remotePlayers;
    private String mServiceName;

    private NsdManager.ResolveListener resolveListener;

    public SearchPlayerFragment() {
        super("searchplayer-fragment");
    }

    public static SearchPlayerFragment newInstance(String serviceName) {
        SearchPlayerFragment fragment = new SearchPlayerFragment();
        Bundle args = new Bundle();
        args.putString("serviceName", serviceName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mServiceName = getArguments().getString("serviceName");
        }

        initResolveListener();

        initDiscovery();

        remotePlayers = new ArrayList<>();
        adapter = new SearchPlayerListViewAdapter(getContext(), remotePlayers);
    }

    private void initResolveListener() {
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                int port = nsdServiceInfo.getPort();
                InetAddress host = nsdServiceInfo.getHost();
                Log.d("NDS client resolved", host.getHostName());
                RemotePlayerClient remoteClient = new RemotePlayerClient(host, port);
                remotePlayers.add(remoteClient);
                ((ListView)getView().findViewById(R.id.search_player_list)).setAdapter(adapter);
            }
        };
    }

    private void initDiscovery() {
//        ((NsdManager) getContext().getSystemService(Context.NSD_SERVICE)).discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_player, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.search_player_pb);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        ((NsdManager) getContext().getSystemService(Context.NSD_SERVICE)).discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, this);

        ((ListView)view.findViewById(R.id.search_player_list)).setAdapter(adapter);
        ((ListView)view.findViewById(R.id.search_player_list)).setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchPlayerFragmentListener) {
            mListener = (SearchPlayerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ((NsdManager) getContext().getSystemService(Context.NSD_SERVICE)).stopServiceDiscovery(this);
    }

    @Override
    public void onStartDiscoveryFailed(String s, int i) {

    }

    @Override
    public void onStopDiscoveryFailed(String s, int i) {

    }

    @Override
    public void onDiscoveryStarted(String s) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDiscoveryStopped(String s) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
        // A service was found!  Do something with it.
        if (!nsdServiceInfo.getServiceType().equals("_http._tcp.")) {
            // "Unknown Service Type
        } else if (nsdServiceInfo.getServiceName().equals(mServiceName)) {
            // "Same machine
        } else if (nsdServiceInfo.getServiceName().contains("PokeAppBattleService")){
            Log.d("NDS found cliente", nsdServiceInfo.getServiceName());
            ((NsdManager) getContext().getSystemService(Context.NSD_SERVICE)).resolveService(nsdServiceInfo, resolveListener);
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo nsdServiceInfo) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
        RemotePlayerClient remoteClient = remotePlayers.get(index);
        if(mListener!=null) {
            mListener.onRemotePlayerSelected(remoteClient);
        }
    }

    public interface SearchPlayerFragmentListener {
        public void onRemotePlayerSelected(RemotePlayerClient remotePlayerClient);
    }
}
