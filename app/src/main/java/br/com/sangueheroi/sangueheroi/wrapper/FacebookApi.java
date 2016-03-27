package br.com.sangueheroi.sangueheroi.wrapper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Diego Lucas on 25/03/2016.
 */
public class FacebookApi {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback;
    private final String TAG = "LOG";
    private Context context = null;
    Profile profile;


    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FacebookApi(CallbackManager callbackManager,AccessTokenTracker accessTokenTracker,ProfileTracker profileTracker,final Context context) {
        Log.d(TAG, "    FacebookApi()");
        setCallbackManager(callbackManager);
        setAccessTokenTracker(accessTokenTracker);
        setProfileTracker(profileTracker);
        setContext(context);
        genaratorCallbackManager();
        initializeFacebookCallback();
        initializeTokenAndProfile();
       // setLoginButtonFacebook(loginButtonFacebook, callback);
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public AccessTokenTracker getAccessTokenTracker() {
        return accessTokenTracker;
    }

    public void setAccessTokenTracker(AccessTokenTracker accessTokenTracker) {
        this.accessTokenTracker = accessTokenTracker;
    }

    public ProfileTracker getProfileTracker() {
        return profileTracker;
    }

    public void setProfileTracker(ProfileTracker profileTracker) {
        this.profileTracker = profileTracker;
    }

    public FacebookCallback<LoginResult> getCallback() {
        return callback;
    }

    public void setCallback(FacebookCallback<LoginResult> callback) {
        this.callback = callback;

    }
    public void genaratorCallbackManager(){
        Log.d(TAG, "    genaratorCallbackManagerfacebook()");

        FacebookSdk.sdkInitialize(getContext());
        this.callbackManager = CallbackManager.Factory.create();

    }

    private void initializeFacebookCallback() {
        Log.d(TAG, "    initializeFacebookCallback()");
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                setProfile(profile);
                Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                       /*
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                try {
                                    String id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    String birthday = object.getString("birthday");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();*/

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
    }

    private void initializeTokenAndProfile(){
        Log.d(TAG, "    initializeTokenAndProfileFacebook()");

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                setProfile(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

}
