package edu.oregonstate.cs496.merdlera.androidoauth;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import edu.oregonstate.cs496.merdlera.androidoauth.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private static int AUTHORIZE_DOMAINS = 123;
    private GoogleApiClient googleApiClient;

    private ActivityMainBinding binding;
    private String userDisplayName;
    private boolean signingOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Google API client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/plus.me"))
                .requestScopes(new Scope("https://www.googleapis.com/auth/plus.stream.read"))
                .requestScopes(new Scope("https://www.googleapis.com/auth/plus.stream.write"))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set up data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setUserDisplayName(this.userDisplayName);
        binding.setAuthorize((view) -> this.authorizePlusDomainsAccess());
        binding.setSignOut((view) -> this.signOut());
    }

    private void authorizePlusDomainsAccess() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, AUTHORIZE_DOMAINS);
    }

    private void signOut() {
        signingOut = true;
        new Thread(() -> {
            ConnectionResult connectionResult = googleApiClient.blockingConnect();

            Auth.GoogleSignInApi.signOut(googleApiClient)
                    .setResultCallback((Status signOutStatus) -> {
                        userDisplayName = null;
                        ((TextView) findViewById(R.id.user_name)).setText("");
                        signingOut = false;

                        Auth.GoogleSignInApi.revokeAccess(googleApiClient)
                                .setResultCallback((Status revokeAccessStatus) -> {});
                    });
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTHORIZE_DOMAINS) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                ((TextView) findViewById(R.id.user_name)).setText(account.getDisplayName());
//                userDisplayName = account.getDisplayName();
//                binding.notifyPropertyChanged(BR._all);
            }
        }
    }

    private void listPosts() {

    }
}
