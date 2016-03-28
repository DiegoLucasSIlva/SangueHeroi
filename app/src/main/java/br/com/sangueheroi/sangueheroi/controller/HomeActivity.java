package br.com.sangueheroi.sangueheroi.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import android.content.Intent;

import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;

import br.com.sangueheroi.sangueheroi.R;


public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private ShareDialog mShareDialog;
    GoogleApiClient mGoogleApiClient;

    private IProfile profile;

    private AccountHeader headerResult = null;
    private Drawer resultDrawer = null;
    Context contexto;

    private final String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "HomeActivity- onCreate:");

        super.onCreate(savedInstanceState);
        final String TAG = "LOG";
        this.contexto = this;

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HomeActivity");
        // mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mShareDialog = new ShareDialog(this);

        profile = new ProfileDrawerItem();

        Bundle inBundle = getIntent().getExtras();
        if(inBundle.isEmpty()) {
            Log.d(TAG, "    inBundle esta nulo -Exibir mensagem de erro e fechar :");
            finish();
        }
        String name = inBundle.get("name").toString();
        String imageUrl = inBundle.get("imageUrl").toString();
        if(!inBundle.get("email").toString().isEmpty()) {
            String email = inBundle.get("email").toString();
            profile.withEmail(email);
        }
        //profile.withIcon(FontAwesome.Icon.faw_cart_plus);
        profile.withName(name);

        new GerarBitmap().execute(imageUrl);

        // Create the AccountHeader
        buildHeader(false, savedInstanceState);
        buildDrawer(false, savedInstanceState, toolbar);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "HomeActivity- onSaveInstanceState:");
        //add the values which need to be saved from the drawer to the bundle
        outState = resultDrawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "HomeActivity- onBackPressed:");

        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (resultDrawer != null && resultDrawer.isDrawerOpen()) {
            resultDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "HomeActivity- onOptionsItemSelected:");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            signOutFacebook();
            signOutGoogle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void signOutFacebook() {
        Log.d(TAG, "HomeActivity- signOutFacebook:");
        LoginManager.getInstance().logOut();
        Intent login = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }


    private void signOutGoogle() {
        Log.d(TAG, "HomeActivity- signOutGoogle:");
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent login = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }

    //Create the drawer
    private void buildDrawer(boolean compact, Bundle savedInstanceState, Toolbar toolbar) {
        Log.d(TAG, "HomeActivity- buildDrawer:");

        resultDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Item 1").withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName("Item 2").withDescription("This is a description").withIcon(FontAwesome.Icon.faw_eye),
                        new SectionDrawerItem().withName("Item 3"),
                        new SecondaryDrawerItem().withName("Item 4").withIcon(FontAwesome.Icon.faw_cart_plus),
                        new SecondaryDrawerItem().withName("Item 5").withIcon(FontAwesome.Icon.faw_database).withEnabled(false),
                        new SecondaryDrawerItem().withName("Item 6").withSelectedIconColor(Color.RED).withIconTintingEnabled(true).withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_dark_primary_text)).withTag("Bullhorn"),
                        new SecondaryDrawerItem().withName("Item 7").withIcon(FontAwesome.Icon.faw_question).withEnabled(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        HomeActivity.this.finish();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Item 9").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(10),
                        new SecondaryDrawerItem().withName("Item 10").withIcon(FontAwesome.Icon.faw_github)
                )
                .withSavedInstance(savedInstanceState)
                .build();


    }

    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        Log.d(TAG, "HomeActivity- buildHeader:");

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*Essa classe eh resposnsavel por fazer o downoload da imagem fora da mainThread*/
    private class GerarBitmap extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Log.d(TAG, "    DownloadImage-doInBackground():");

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.d(TAG, "    onPostExecute():");
            Drawable mDrawable = new BitmapDrawable(contexto.getResources(), result);
            profile.withIcon(mDrawable);

        }
    }


}
