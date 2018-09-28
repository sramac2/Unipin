package com.example.android.unipin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText mNameEditText ;
    private EditText mAgeEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button submit;
    private boolean spinner = false;
    private Intent intent;
    private  String identification;
    private Spinner mIdentitySpinner;
    private DatabaseReference databaseReference;
    private int mIdentity;
    private boolean t = false;
    private ProgressDialog progressDialog;
    private String name;
    private FirebaseAuth mAuth;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        submit = (Button) findViewById(R.id.submit);
        mNameEditText = findViewById(R.id.name_signup2);
        progressDialog = new ProgressDialog(this);
        mEmailEditText   = findViewById(R.id.email);
        mPasswordEditText  = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        adView = findViewById(R.id.adView5);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mIdentitySpinner = findViewById(R.id.planets_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.identify_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIdentitySpinner.setAdapter(adapter);

        mIdentitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identification = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                identification = (String) mIdentitySpinner.getItemAtPosition(0);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                finish();
            }

        });



    }

    private void registerUser(){
        final String email = mEmailEditText.getText().toString().trim();
        final String pass = mPasswordEditText.getText().toString().trim();
        name = mNameEditText.getText().toString().trim();


        if(TextUtils.isEmpty(name) || !name.contains(" ")){
            Toast.makeText(this,"Please type your full name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email",Toast.LENGTH_SHORT).show();
            return;
            //If email is not entered
            }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
            //If password is not entered
            return;
            }

        else{
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account created!",
                                    Toast.LENGTH_SHORT).show();
                            signIn(email,pass,name);
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException()
                                    .getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }
                    }
                });
        }
    }

    private void signIn(String email, String password, final String name){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            nameUpdate(name);
                            createUser();
                            intent = new Intent(SignupActivity.this
                                    , HomeActivity.class);
                            if(intent.resolveActivity(getPackageManager())!=null){
                            startActivity(intent);
                            }
                        }

                        else {
                            Toast.makeText(SignupActivity.this,task.getException().getMessage()
                            ,Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
    }
    private void nameUpdate(String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignupActivity.this
                                    ,"Name Added",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference  = FirebaseDatabase.getInstance().getReference("users");
        User user1 = new User(name,user.getUid(),user.getEmail(),identification,0
                ,0,1,"Newbie","bronze1");
        databaseReference.child(user.getUid()).setValue(user1);
    }
}
