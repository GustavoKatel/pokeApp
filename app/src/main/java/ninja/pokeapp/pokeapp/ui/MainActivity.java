package ninja.pokeapp.pokeapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ninja.pokeapp.pokeapp.R;
import ninja.pokeapp.pokeapp.business.facade.AppFacade;
import ninja.pokeapp.pokeapp.business.remote_player.RemotePlayerClient;
import ninja.pokeapp.pokeapp.business.remote_player.RemotePlayerServer;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.user.Usuario;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.HomeFragmentListener,
        UserPokemonsFragment.UserPokemonsFragmentListener,
        LoginFragment.LoginFragmentListener,
        PokemonCapturedFragment.PokemonCapturedFragmentListener,
        GreenFieldFragment.GreenFieldFragmentListener,
        SearchPlayerFragment.SearchPlayerFragmentListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NsdManager.RegistrationListener {

    private static int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    NsdServiceInfo battleServiceInfo;
    String battleServiceName = "PokeAppBattleService";
    boolean battleServiceRegistered = false;
    RemotePlayerServer battleServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init the facade
        AppFacade.init(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        AppFacade.getInstancia().setGoogleApiClient(mGoogleApiClient);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            AbstractFragment firstFragment = LoginFragment.newInstance();

            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    private void initBattleService() {

        try {
            battleServer = new RemotePlayerServer();

            battleServiceInfo = new NsdServiceInfo();
            battleServiceInfo.setServiceName(battleServiceName);
            battleServiceInfo.setServiceType("_http._tcp.");
            battleServiceInfo.setPort(battleServer.getServerSocket().getLocalPort());

            battleServiceRegistered = true;
            ((NsdManager) getSystemService(Context.NSD_SERVICE)).registerService(battleServiceInfo, NsdManager.PROTOCOL_DNS_SD, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_user_pokemons) {
            openMyPokemons();
        } else if (id == R.id.drawer_home) {
            openHome();
        } else if (id == R.id.drawer_leaderboard) {
            openLeaderboard();
        } else if (id == R.id.drawer_search_player) {
            openSearchPlayer();
        } else if (id == R.id.drawer_greenfield) {
            openGreenfield();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

     private void openFragment(AbstractFragment newFragment) {

        Fragment frag = getSupportFragmentManager().findFragmentByTag(newFragment.getIdent());
        if(frag!=null && frag.isVisible()) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, (Fragment) newFragment, newFragment.getIdent());
//        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void openMyPokemons() {
        openFragment(UserPokemonsFragment.newInstance(AppFacade.getInstancia().getCurrentUser()));
    }

    private void openHome() {
        openFragment(HomeFragment.newInstance(Games.Players.getCurrentPlayer(mGoogleApiClient)));
    }

    private void openLogin() {
        openFragment(LoginFragment.newInstance());
    }

    private void openLeaderboard() {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                mGoogleApiClient, getString(R.string.leaderboard_id)),
                2);
    }

    private void openSearchPlayer() {
        openFragment(SearchPlayerFragment.newInstance(battleServiceName));
    }

    private void openGreenfield() {
        openFragment(GreenFieldFragment.newInstance());
    }

    @Override
    public void onFloatIconClicked() {
        openMyPokemons();
    }

    public void openPokemonCaptured(PokemonCaptured pokemonCaptured) {
        openFragment(PokemonCapturedFragment.newInstance(pokemonCaptured));
    }

    @Override
    public void onPokemonCapturedSelected(PokemonCaptured pokemonCaptured) {

        openPokemonCaptured(pokemonCaptured);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // The player is signed in
        openHome();

        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        Usuario user = new Usuario(player.getPlayerId(), player.getDisplayName(), player.getPlayerId());
        AppFacade.getInstancia().setCurrentUser(user);

        initBattleService();

//        AppFacade.getInstancia().addTestData();
//        AppFacade.getInstancia().addTestData2();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getResources().getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        openLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(battleServiceRegistered)
            ((NsdManager) getSystemService(Context.NSD_SERVICE)).unregisterService(this);
        battleServiceRegistered = false;
        battleServer.stop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(battleServiceRegistered)
            ((NsdManager) getSystemService(Context.NSD_SERVICE)).unregisterService(this);
        battleServiceRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!battleServiceRegistered)
//            ((NsdManager) getSystemService(Context.NSD_SERVICE)).registerService(battleServiceInfo, NsdManager.PROTOCOL_DNS_SD, this);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed.
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Override
    public void onLoginClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onPokemonCaptured(PokemonCaptured pokemonCaptured) {
        openMyPokemons();
    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
        Toast.makeText(this, "Fail to register battle service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {

    }

    @Override
    public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
        battleServiceName = nsdServiceInfo.getServiceName();
        Log.d("PokeApp", "Battle service registered as: " + battleServiceName);
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {

    }

    @Override
    public void onRemotePlayerSelected(RemotePlayerClient remotePlayerClient) {

    }
}
