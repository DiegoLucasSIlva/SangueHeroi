package br.com.sangueheroi.sangueheroi.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.ksoap2.serialization.SoapPrimitive;

import br.com.sangueheroi.sangueheroi.R;
import br.com.sangueheroi.sangueheroi.model.Usuario;
import br.com.sangueheroi.sangueheroi.network.RequestWS;
import br.com.sangueheroi.sangueheroi.wrapper.FacebookApi;
import br.com.sangueheroi.sangueheroi.wrapper.GoogleApi;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private FacebookApi facebookApi;
    private GoogleApi googleApi;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButtonFacebook;
    private SignInButton loginButtonGoogle;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private EditText etInputEmail;
    private EditText etInputPassword;
    SoapPrimitive     resultString;
    RequestWS         requestWs;
    ProgressBar       mProgress;
    Usuario usuario;



    private static final int SIGN_IN_CODE = 9001;
    private GoogleApiClient mGoogleApiClient;  //Da acesso a googleAPI

    private FacebookCallback<LoginResult> callback;
    private final String TAG = "LOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, " onCreate()");
        super.onCreate(savedInstanceState);
        /*Intanciando os objetos da Wrapper das api*/
        googleApi = new GoogleApi();
        facebookApi = new FacebookApi(callbackManager, accessTokenTracker, profileTracker, this);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleApi.getGso())
                .build();
    }


    private void setupViews() {
        mProgress = (ProgressBar) findViewById(R.id.carregando);
        mProgress.setVisibility(View.INVISIBLE);
        // toolbar.setTitleTextColor(Material.White);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        /*Configurandos os botoes de ambas API's*/
        loginButtonFacebook = (LoginButton) findViewById(R.id.sign_in_button_facebook);
        loginButtonFacebook.setReadPermissions("user_friends");
        loginButtonFacebook.registerCallback(facebookApi.getCallbackManager(), callback);

        findViewById(R.id.sign_in_button_google).setOnClickListener(this);
        loginButtonGoogle = (SignInButton) findViewById(R.id.sign_in_button_google);
        loginButtonGoogle.setSize(SignInButton.SIZE_STANDARD);
        loginButtonGoogle.setScopes(googleApi.getGso().getScopeArray());

        /*Referenciando os edits */
        inputEmail = (TextInputLayout) findViewById(R.id.input_email);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);
        etInputEmail = (EditText) findViewById(R.id.et_input_email);
        etInputPassword = (EditText) findViewById(R.id.et_input_password);
    }

    /*O onStart so executa comandos para o login com o Google*/
    @Override
    public void onStart() {
        Log.v(TAG, " onStartGoogle()");
        super.onStart();
            /*Tenta recuperar os dados caso o login já tenho sido feito anteriormente*/
        OptionalPendingResult<GoogleSignInResult> optPenRes = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (optPenRes.isDone()) {
            Log.d(TAG, "        Recuperou dados");
            GoogleSignInResult resultsign = optPenRes.get();  //Pega o resultado obtido no sucesso
            googleApi.handleSignInResult(resultsign);
            updateUIGoogle(googleApi.getFlagResultSign());
        } else {
            Log.d(TAG, "        Nao Recuperou dados");

            optPenRes.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    googleApi.handleSignInResult(googleSignInResult);
                    updateUIGoogle(googleApi.getFlagResultSign());
                }
            });

        }
    }

    @Override
    protected void onResume() {
        Log.v(TAG, " onResumeFacebook()");

        super.onResume();
        /*Facebook login*/
        Profile profile = Profile.getCurrentProfile();
        updateUIFacebook(profile);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, " onPause()");

        super.onPause();
    }

    protected void onStop() {
        Log.v(TAG, " onStopFacebook()");
        super.onStop();
        /*Facebook login*/
        facebookApi.getAccessTokenTracker().stopTracking();
        facebookApi.getProfileTracker().stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        Log.v(TAG, "    onActivityResult()");

        super.onActivityResult(requestCode, responseCode, intent);
        /*Facebook login*/
        facebookApi.getCallbackManager().onActivityResult(requestCode, responseCode, intent);

        if (requestCode == SIGN_IN_CODE) {   //Se A requisacao foi aceita ,reponde o sign in no result
            Log.v(TAG, "    onActivityResult-Google()");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            googleApi.handleSignInResult(result);  //Passa o result para o nosso handle
            updateUIGoogle(googleApi.getFlagResultSign());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "    onConnectionFailed:" + connectionResult);
    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClickGoogle():");

        startActivityForResult(googleApi.signIn(mGoogleApiClient), SIGN_IN_CODE);  //passa a chave para startActivityForResult com a signInIntent com os dados do usuario
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, " onOptionsItemSelected()");
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.v(TAG, " finish()");
            this.finish();
        }
        if( id == R.id.action_send ){
            boolean hasError = false;

            if( etInputEmail.getText().toString().trim().length() == 0 ){
                inputEmail.setErrorEnabled(true);
                inputEmail.setError("Entre com seu email");
                hasError = true;
            }
            else{
                inputEmail.setErrorEnabled(false);
            }
            if( etInputPassword.getText().toString().trim().length() == 0 ){
                inputPassword.setErrorEnabled(true);
                inputPassword.setError("Entre com a senha");
                hasError = true;
            }
            else{
                inputPassword.setErrorEnabled(false);
            }

            if( !hasError ){
                /*
                contact = new Contact();
                contact.setEmail( UtilTCM.getEmailAccountManager(this) );
                contact.setSubject(etSubject.getText().toString());
                contact.setMessage( etMessage.getText().toString() );
                NetworkConnection.getInstance(this).execute( this, ContactActivity.class.getName() );
                */
                usuario = new Usuario();
                usuario.setNome(etInputEmail.getText().toString().trim());
                usuario.setSenha(etInputPassword.getText().toString().trim());

                mProgress.setVisibility(View.VISIBLE);
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        }

        return true;
    }
    private void updateUIFacebook(Profile profile) {
        if (profile != null) {
            Log.d(TAG, "    updateUIFacebook:");

            Intent main = new Intent(LoginActivity.this, HomeActivity.class);
            main.putExtra("name", profile.getName());
            main.putExtra("email"," ");
            main.putExtra("imageUrl", profile.getProfilePictureUri(200, 200).toString());
            startActivity(main);
        }
    }


    private void updateUIGoogle(boolean signedIn) {
        if (signedIn) {
            Log.d(TAG, "    updateUIGoogle:");

            Intent main = new Intent(this, HomeActivity.class);
            main.putExtra("name", googleApi.getAccount().getDisplayName());
            main.putExtra("email", googleApi.getAccount().getEmail());
            main.putExtra("imageUrl", googleApi.getAccount().getPhotoUrl().toString());
            startActivity(main);
            finish();
        }
    }

    /*Essa AsynTask sera chamada para o login normal*/
    private class AsyncCallWS extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "AsyncCallWS- onPreExecute");
        }


        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "AsyncCallWS -doInBackground");
            requestWs = new RequestWS();
            while (!isCancelled()) {
                SoapPrimitive result = requestWs.callServiceLogin(usuario);
                if (result == null) {
                    return "false";
                }
                return result.toString();

            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.setVisibility(View.INVISIBLE);
            Log.i(TAG, "AsyncCallWS -onPostExecute");
            if(result.toString().equals("false")) {
                Toast.makeText(LoginActivity.this, "Usuario ou senha inválido(s)", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Login feito com Sucesso!", Toast.LENGTH_SHORT).show();
              //  Intent main = new Intent(LoginActivity.this, HomeActivity.class);
              //  startActivity(main);
            }


        }

    }


}