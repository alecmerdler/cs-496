package edu.oregonstate.cs496.merdlera.androidoauth;

import android.accounts.Account;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Acl;
import com.google.api.services.plusDomains.model.Activity;
import com.google.api.services.plusDomains.model.PlusDomainsAclentryResource;
import edu.oregonstate.cs496.merdlera.androidoauth.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends android.app.Activity {

    private static int AUTHORIZE_DOMAINS = 123;
    private GoogleApiClient googleApiClient;
    private Account account;
    private PlusDomains plusDomains;

    private ActivityMainBinding binding;
    private CurrentUser currentUser;
    private String newPost = "";

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
        binding.setCurrentUser(this.currentUser);
        binding.setNewPost(newPost);
        binding.setAuthorize((view) -> this.authorizePlusDomainsAccess());
        binding.setSignOut((view) -> this.signOut());
        binding.setSharePost((view) -> new Thread(() -> createPost(newPost)));
    }

    private void authorizePlusDomainsAccess() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, AUTHORIZE_DOMAINS);
    }

    private void signOut() {
        new Thread(() -> {
            googleApiClient.blockingConnect();
            Auth.GoogleSignInApi.signOut(googleApiClient)
                    .setResultCallback((Status signOutStatus) -> {
                        currentUser = null;
                        binding.setCurrentUser(currentUser);

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
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                account = googleSignInAccount.getAccount();
                GoogleAccountCredential credential =
                        GoogleAccountCredential.usingOAuth2(
                                MainActivity.this,
                                Collections.singleton("https://www.googleapis.com/auth/contacts.readonly"));
                credential.setSelectedAccount(account);
                plusDomains = new PlusDomains.Builder(AndroidHttp.newCompatibleTransport(),
                                                      JacksonFactory.getDefaultInstance(),
                                                      credential).build();

                currentUser = new CurrentUser();
                currentUser.setDisplayName(googleSignInAccount.getDisplayName());
                binding.setCurrentUser(currentUser);
                new Thread(() -> listPosts()).start();
            }
        }
    }

    private void listPosts() {
        try {
            List<String> posts = new ArrayList<>();
            List<Activity> activities = plusDomains.activities().list("me", "user").execute().getItems();
            for (Activity activity : activities) {
                posts.add(activity.getObject().getContent());
            }
            currentUser.setPosts(posts);
        } catch (IOException ioe) {

        }
    }

    private void createPost(String message) {
        try {
            PlusDomainsAclentryResource resource = new PlusDomainsAclentryResource();
            resource.setType("domain");
            List<PlusDomainsAclentryResource> aclEntries = new ArrayList<PlusDomainsAclentryResource>();
            aclEntries.add(resource);
            Acl acl = new Acl();
            acl.setItems(aclEntries);
            acl.setDomainRestricted(true);

            Activity activity = new Activity().setObject(new Activity.PlusDomainsObject().setOriginalContent(message)).setAccess(acl);
            plusDomains.activities().insert("me", activity).execute();
        } catch (IOException ioe) {

        }
    }

    public class CurrentUser extends BaseObservable {
        private String displayName;
        private List<String> posts = new ArrayList<>();
        @Bindable
        public String getDisplayName() {
            return this.displayName;
        }
        @Bindable
        public List<String> getPosts() {
            return this.posts;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
            notifyPropertyChanged(BR.displayName);
        }
        public void setPosts(List<String> posts) {
            this.posts = posts;
            notifyPropertyChanged(BR.posts);
        }
    }
}
