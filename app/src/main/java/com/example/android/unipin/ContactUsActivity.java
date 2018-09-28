package com.example.android.unipin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactUsActivity extends AppCompatActivity {
    private EditText subject;
    private EditText message;
    private Button emailButton;
    private FirebaseUser firebaseUser;
    private String name;
    private String uId;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        emailButton = findViewById(R.id.send_email);
        subject =findViewById(R.id.subject);
        message = findViewById(R.id.message);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectText = subject.getText().toString();
                String messageText = message.getText().toString();
                if(firebaseUser!=null){
                    name = firebaseUser.getDisplayName();
                    uId = firebaseUser.getUid();
                }
                String [] TO = {"spapetti1998@gmail.com","ramnkl8219@gmail.com"
                ,"unifiedaccessreview@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");

                emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,subjectText);
                emailIntent.putExtra(Intent.EXTRA_TEXT,"Name: "+name+"\nUnique Id: "+uId
                        +"\n\n"+messageText);

                try {
                    startActivity(Intent.createChooser(emailIntent,"Select a client..."));
                    finish();
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(ContactUsActivity.this
                            ,"There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
