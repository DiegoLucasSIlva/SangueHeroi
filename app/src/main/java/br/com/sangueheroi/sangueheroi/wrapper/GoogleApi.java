package br.com.sangueheroi.sangueheroi.wrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import br.com.sangueheroi.sangueheroi.R;

/**
 * Created by Diego Lucas on 25/03/2016.
 */
public class GoogleApi {
    private static final String TAG = "LOG";
    private GoogleSignInAccount account;
    private SignInButton loginButtonGoogle;
    private GoogleSignInResult result;
    private GoogleSignInOptions gso;
    private Boolean flagResultSign;


    public GoogleApi() {
        initializeGoogleCallback();
    }

    public Boolean getFlagResultSign() {
        return flagResultSign;
    }

    public void setFlagResultSign(Boolean flagResultSign) {
        this.flagResultSign = flagResultSign;
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public void setGso(GoogleSignInOptions gso) {
        this.gso = gso;
    }

    private void setLoginButtonGoogle(SignInButton loginButtonGoogle) {
        this.loginButtonGoogle = loginButtonGoogle;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }


    private void initializeGoogleCallback() {
        Log.d(TAG, "    initializeGoogleCallback()");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        setGso(gso);


    }


    public void setResult(GoogleSignInResult result) {
        this.result = result;
    }

    /*Lida com o resultado obtido*/
    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "    handleSignInResult():" + result.isSuccess());
        if (result.isSuccess()) {   //O resultado estiver OK
            account = result.getSignInAccount();  // Objeto com dados do usuario
            setAccount(account);
            setFlagResultSign(true); //Seta a flag informando que foi recuperados dados do login
        } else {
            setFlagResultSign(false);
        }
    }

    public Intent signIn(GoogleApiClient mGoogleApiClient) {
        Log.d(TAG, "    signIn()");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient); //Passa mGoogleApiClient com o tipo de sign requisito
        return signInIntent;
    }
}