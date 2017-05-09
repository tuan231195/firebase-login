package com.tuannguyen.activities.authorization.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.tuannguyen.activities.authorization.AuthPresenter;
import com.tuannguyen.activities.authorization.AuthView;
import com.tuannguyen.loginapp.R;
import com.tuannguyen.model.User;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;

public class LoginPresenter extends AuthPresenter implements GoogleApiClient.OnConnectionFailedListener {

    @Inject
    LoginManager mLoginManager;

    @Inject
    Context mContext;

    @Inject
    CallbackManager mCallbackManager;

    private GoogleApiClient mGoogleApiClient;


    private static final int RC_SIGN_IN = 9001;

    public LoginPresenter(AuthView baseView) {
        super(baseView);
    }

    @Override
    protected void onBind() {
        super.onBind();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(view().asActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void login(String email, String password) {
        if (validate()) {
            toggleBtnAndDialog(true);
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        view().goToMain();
                    } else {
                        view().showError(task.getException().getMessage());
                    }
                    toggleBtnAndDialog(false);
                }
            });
        }
    }


    public void facebookLogin() {
        mLoginManager.logInWithReadPermissions(view().asActivity(), Arrays.asList("email", "public_profile"));
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                view().showError(error.getMessage());
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(view().asActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            saveFacebookUser(token);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            view().showError("Authentication failed.");
                        }
                    }
                });
    }

    private void saveFacebookUser(AccessToken accessToken) {
        final FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "saveFacebookUser:failure: User cannot be null");
            return;
        }

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject result,
                            GraphResponse response) {
                        if (result != null) {
                            Log.d(TAG, "saveFacebookUser:success: Result is:" + response.getRawResponse());
                            User user = new User();
                            user.setName(result.optString("name", "Unknown"));
                            user.setEmail(result.optString("email", "Unknown"));
                            user.setUid(firebaseUser.getUid());
                            saveUser(user);
                            view().goToMain();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        view().asActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        view().showError("Google Play Service Error");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                view().showError("Google sign in failed");
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        view().showDialog();
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(view().asActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            saveGoogleUser(account);
                            view().goToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            view().showError("Authentication failed.");
                        }
                        view().hideDialog();
                    }
                });
    }

    private void saveGoogleUser(GoogleSignInAccount account) {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "saveGoogleUser:failure: User cannot be null");
            return;
        }

        User user = new User();
        user.setName(account.getDisplayName());
        user.setEmail(account.getEmail());
        user.setUid(firebaseUser.getUid());
        saveUser(user);
    }
}
