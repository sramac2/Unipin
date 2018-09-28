package com.example.android.unipin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mLogin;//standard login button
    private ImageView imageView;
    private CardView usernameCardView;
    private CardView passCardView;
    SignInButton signInButton;
    private DatabaseReference databaseReference;
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth mAuth; //Firebase authentication Instance
    LoginButton loginButton; // facebook sign in button
    private TextView signup;//Don't have an account,create one
    private TextView forgotPassText;
    private GoogleSignInClient mGoogleSignInClient; //google signing in
    private EditText username;// standard login email input
    private EditText password;//standard login password input
    private String identification;
    private CallbackManager mCallbackManager;//For facebook connectivity
    private static final String TAG = "MainActivity";//To write logs
    private Toolbar toolbar;

    private ProgressDialog progressDialog;//to show progress while logging in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //writes log on facebook to review analytics
        AppEventsLogger.activateApp(this);
        //username = (EditText) findViewById(R.id.username);
        mLogin = findViewById(R.id.login_main);//standard login button
        username  = findViewById(R.id.username);
        //usernameCardView = findViewById(R.id.cardView4);
        // passCardView = findViewById(R.id.cardView5);
        signup = (TextView) findViewById(R.id.signup);//Don't have an account,create one
        password = (EditText) findViewById(R.id.editText2);//standard password input
        mAuth = FirebaseAuth.getInstance();// Receiving instance of Firebase
        forgotPassText = findViewById(R.id.forgot_password);

        Animation animation = AnimationUtils
                .loadAnimation(MainActivity.this,R.anim.slide_up);
        // usernameCardView.startAnimation(animation);
        //passCardView.startAnimation(animation);


        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                final AlertDialog dialog =new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Type your email")
                        .setView(input)
                        .setPositiveButton("DONE",null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = ((AlertDialog)dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = input.getText().toString().trim();
                                if(TextUtils.isEmpty(email)){
                                    Toast.makeText(MainActivity.this
                                            ,"Please type your email",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mAuth.sendPasswordResetEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(MainActivity.this
                                                                ,"Password reset link has been"
                                                                        +" sent to your email"
                                                                ,Toast.LENGTH_SHORT)
                                                                .show();
                                                        dialog.dismiss();
                                                    }
                                                    else {
                                                        Toast.makeText(MainActivity.this
                                                                ,task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                });
                dialog.show();


            }
        });



        progressDialog = new ProgressDialog(this);//progress dialog
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Please type your email"
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this,"Please type your password"
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener
                                (MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(MainActivity.this
                                                    ,HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this,task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Toast.makeText(MainActivity.this,"Authentication Failed"
                            ,Toast.LENGTH_SHORT);
                }
            }
        });
        //Google sign in Code
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                DEFAULT_SIGN_IN)
                .requestIdToken
                        ("702677583377-1r5rc3ign4ed2d17f7gqunl1n83nrpk0.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton = (SignInButton) findViewById(R.id.google_signin_main);
        signInButton.setOnClickListener((View.OnClickListener) this);


        //Facebook login connection codes
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login_main);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //If the login to facebook is successsfull
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                progressDialog.hide();
            }

            @Override
            public void onError(FacebookException error) {
                progressDialog.hide();
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d(TAG, "facebook:onError", error);
            }
        });

        Typeface myFont = Typeface.createFromAsset(getAssets(),"fonts/SourceSansPro-Bold.otf");
        //changing font to sansPro from fonts directory
        mLogin.setTypeface(myFont);
        password.setTypeface(myFont);
        //username.setTypeface(myFont);

        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View d) {
                Context context = MainActivity.this;

                Intent intent = new Intent(context,SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Facebook code
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode,resultCode,data);

        //Google sign in code
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(MainActivity.this,e.getMessage()
                        ,Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this,
                    "You are signed in!",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("users");
                            databaseReference.child(user.getUid()).child("email")
                                    .setValue(user.getEmail());
                            databaseReference.child(user.getUid()).child("name")
                                    .setValue(user.getDisplayName());
                            databaseReference.child(user.getUid()).child("uId")
                                    .setValue(user.getUid());

                            databaseReference.orderByChild("uId").equalTo(user.getUid())
                                    .limitToFirst(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot
                                        , @Nullable String s) {
                                    User user2= dataSnapshot.getValue(User.class);
                                    identification = user2.getIdentification();
                                    if(identification == null){
                                        Intent intent = new Intent(MainActivity.this
                                                ,InfoActivity.class);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(MainActivity.this
                                    ,HomeActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                        // ...
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preference,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
                break;
            case R.id.contact_us:
                startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
                break;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        signIn();
    }

    public void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(user.getUid()).child("email").setValue(user.getEmail());
                            databaseReference.child(user.getUid()).child("name").setValue(user.getDisplayName());
                            databaseReference.child(user.getUid()).child("uId").setValue(user.getUid());
                            databaseReference.orderByChild("uId").equalTo(user.getUid())
                                    .limitToFirst(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    User user2= dataSnapshot.getValue(User.class);
                                    identification = user2.getIdentification();
                                    if(identification == null){
                                        Intent intent = new Intent(MainActivity.this
                                                ,InfoActivity.class);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(MainActivity.this
                                    , HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"Authentication Failed"
                                    ,Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                        // [START_EXCLUDE]
                        progressDialog.hide();
                        // [END_EXCLUDE]
                    }
                });
    }
}