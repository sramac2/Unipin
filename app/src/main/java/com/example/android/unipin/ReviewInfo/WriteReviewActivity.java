package com.example.android.unipin.ReviewInfo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.example.android.unipin.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteReviewActivity extends AppCompatActivity {

    private Button submit;
    private Review review1;
    private TextView mTitleTextView;
    private TextView noiseTextView;
    private TextView lightTextView;
    private String identfication;
    private TextView visualTextView;
    private TextView crowdTextView;
    private RatingBar visualRatingBar;
    private EditText occasionEditText;
    private RatingBar lightRatingBar;
    private FirebaseUser mUser;
    private RatingBar crowdRatingBar;
    private RatingBar noiseRatingBar;
    DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReferenceUsers;
    private EditText descriptionEditText;
    private RatingBar smellRatingBar;
    private User user1;
    private Dialog dialog;
    private String description;
    private String levelChangeOld;
    private String levelChangeNew;
    private boolean levelchanged;
    private Drawable trophy;
    private String id;
    private Uri website;
    private String title;
    private String address;
    private String phone;
    private float overall2=0;
    private DatabaseReference databaseReference2;
    private float smell;
    private float visual;
    private float noise;
    private float light;
    private float crowd;
    private String occasion;
    private String  description1;
    private Rating rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__write_review);

        dialog = new Dialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("ratings");

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        databaseReference2.orderByChild("placeId").equalTo(id).limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        rating = dataSnapshot.getValue(Rating.class);
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

        submit = findViewById(R.id.submit);

        visualRatingBar = findViewById(R.id.visualRatingBar);
        lightRatingBar =  findViewById(R.id.lightRatingBar);
        crowdRatingBar = findViewById(R.id.crowdRatingBar);
        noiseRatingBar = findViewById(R.id.noiseRatingBar);
        smellRatingBar  = findViewById(R.id.smellRatingBar);
        mTitleTextView = findViewById(R.id.place_name);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.orderByKey().equalTo(id+mUser.getUid()).limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        review1 = dataSnapshot.getValue(Review.class);
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

        descriptionEditText = findViewById(R.id.ReviewDescription);
        occasionEditText = findViewById(R.id.occasion);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users");


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        visualRatingBar.startAnimation(animation);
        lightRatingBar.startAnimation(animation);
        crowdRatingBar.startAnimation(animation);
        noiseRatingBar.startAnimation(animation);
        smellRatingBar.startAnimation(animation);
        descriptionEditText.startAnimation(animation);
        occasionEditText.startAnimation(animation);



        title = intent.getExtras().getString("title");

        address =  intent.getExtras().getString("title");
        website =(Uri) intent.getExtras().get("website");
        phone= intent.getExtras().getString("phone");
        smell = intent.getExtras().getFloat("smell");
        visual = intent.getExtras().getFloat("visual");
        noise = intent.getExtras().getFloat("noise");
        light = intent.getExtras().getFloat("light");
        crowd  =intent.getExtras().getFloat("crowd");
        description1 = intent.getExtras().getString("description");
        occasion  = intent.getExtras().getString("occasion");

        noiseRatingBar.setRating(noise);
        smellRatingBar.setRating(smell);
        visualRatingBar.setRating(visual);
        crowdRatingBar.setRating(crowd);
        lightRatingBar.setRating(light);
        descriptionEditText.setText(description1);
        occasionEditText.setText(occasion);




        mTitleTextView.setText(title);


        databaseReference1.orderByChild("uId").equalTo(mUser.getUid())
                .limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                user1 = user;
                identfication = user.getIdentification();
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



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noiseRatingBar.getRating()==0){
                    Toast.makeText(WriteReviewActivity.this,"Noise rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(crowdRatingBar.getRating()==0){
                    Toast.makeText(WriteReviewActivity.this,"Crowd rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lightRatingBar.getRating()==0){
                    Toast.makeText(WriteReviewActivity.this,"Light rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(visualRatingBar.getRating()==0){
                    Toast.makeText(WriteReviewActivity.this,"Visual input rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(smellRatingBar.getRating()==0){
                    Toast.makeText(WriteReviewActivity.this,"Smell rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }



                else
                if(review1==null){
                    updateUser(user1);
                    showPopup();}
                else {
                    submitReview();
                    Intent intent1 = new Intent(WriteReviewActivity.this
                            ,ReviewActivity.class);
                    intent1.putExtra("id",id);
                    intent1.putExtra("title",title);
                    intent1.putExtra("address",address);
                    intent1.putExtra("website",website);
                    intent1.putExtra("phone",phone);
                    intent1.putExtra("smell",smell);
                    intent1.putExtra("visual",visual);
                    intent1.putExtra("crowd",crowd);
                    intent1.putExtra("light",light);
                    intent1.putExtra("noise",noise);
                    intent1.putExtra("occasion",occasion);
                    intent1.putExtra("description",description1);
                    startActivity(intent1);
                }
                levelChangeOld = user1.getLevelName();

                levelChange(user1);

            }
        });
    }

    private void updateUser(User user){
        int points = 0;
            String uId = user.getuId();
            if(!TextUtils.isEmpty(description)){
                points  =5;
            }
            databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users").child(uId);
            databaseReferenceUsers.child("points").setValue(user.getPoints()+5+5);
            databaseReferenceUsers.child("reviewCount").setValue(user.getReviewCount()+1);
            points = user.getPoints()+5+5;
            if(points >= 10&&points<50){
                databaseReferenceUsers.child("levelName").setValue("Beginner");
                databaseReferenceUsers.child("levelNumber").setValue(1);
                databaseReferenceUsers.child("trophy").setValue("bronze2");
                return;
            }
            if(points>=50&&points<100){
                databaseReferenceUsers.child("levelName").setValue("Developing reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(2);
                databaseReferenceUsers.child("trophy").setValue("bronze3");
                return;
            }
            else if(points>=100&&points<250){
                databaseReferenceUsers.child("levelName").setValue("Active reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(3);
                databaseReferenceUsers.child("trophy").setValue("silver1");
                trophy = ContextCompat.getDrawable(this, R.drawable.active_reviewer);
                return;
            }
            else if(points>=250&&points<500){
                databaseReferenceUsers.child("levelName").setValue("Elite Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(4);
                databaseReferenceUsers.child("trophy").setValue("silver2");
                trophy = ContextCompat.getDrawable(this, R.drawable.elite_reviewer);
                return;
            }
            else if (points>=500&&points<750){
                databaseReferenceUsers.child("levelName").setValue("The Seeker");
                databaseReferenceUsers.child("levelNumber").setValue(5);
                databaseReferenceUsers.child("trophy").setValue("silver3");
                trophy = ContextCompat.getDrawable(this, R.drawable.seeker);
                return;
            }
            else if(points>=750&&points<1000){
                databaseReferenceUsers.child("levelName").setValue("Real reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(6);
                databaseReferenceUsers.child("trophy").setValue("gold1");
                trophy = ContextCompat.getDrawable(this, R.drawable.real_reviewer);
                return;
            }
            else if(points>=1000&&points<1500){
                databaseReferenceUsers.child("levelName").setValue("Review Veteran");
                databaseReferenceUsers.child("levelNumber").setValue(7);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.review_veteran);
                return;
            }
            else if(points>=1500&&points<2000){
                databaseReferenceUsers.child("levelName").setValue("Elite Review Veteran");
                databaseReferenceUsers.child("levelNumber").setValue(8);
                databaseReferenceUsers.child("trophy").setValue("gold3");
                trophy = ContextCompat.getDrawable(this, R.drawable.elite_review_veteran);
                return;
            }

            else if (points>=2000&&points<3000){
                databaseReferenceUsers.child("levelName").setValue("Rigorous Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(9);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.rigorous_reviewer);
                return;
            }

            else if(points>=3000&&points<4000){
                databaseReferenceUsers.child("levelName").setValue("Primary Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(10);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.primary_reviewer);
                return;
            }

            else if(points>=4000&&points<5000){
                databaseReferenceUsers.child("levelName").setValue("Professional Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(11);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.professional_reviewer);
                return;
            }

            else if(points>=5000&&points<10000){
                databaseReferenceUsers.child("levelName").setValue("Best Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(12);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable( this, R.drawable.best_reviewer);
                return;
            }
            else if(points>=10000&&points<50000){
                databaseReferenceUsers.child("levelName").setValue("Advanced Reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(13);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.advanced_reviewer);
                return;
            }
            else if(points>=50000&&points<100000){
                databaseReferenceUsers.child("levelName").setValue("All time reviewer");
                databaseReferenceUsers.child("levelNumber").setValue(14);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                return;
            }

            else if(points>=100000){
                databaseReferenceUsers.child("levelName").setValue("The incomparable");
                databaseReferenceUsers.child("levelNumber").setValue(15);
                databaseReferenceUsers.child("trophy").setValue("gold2");
                trophy = ContextCompat.getDrawable(this, R.drawable.the_incomparable);
                return;

        }
    }

    private void levelChange(User user){
        levelChangeNew = user.getLevelName();

        if(!levelChangeNew.equals(levelChangeOld)){
            levelchanged = true;
        }
    }

    private void showPopup(){
        if(levelchanged){
        dialog.setContentView(R.layout.points_popup);
        ImageView imageView  = dialog.findViewById(R.id.trophy_popup);
        imageView.setImageDrawable(trophy);
        ImageButton imageButton= dialog.findViewById(R.id.check);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.onBackPressed();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                submitReview();
                Intent intent1 = new Intent(WriteReviewActivity.this
                        ,ReviewActivity.class);
                startActivity(intent1);
            }
        });
    }
    else {
            dialog.setContentView(R.layout.points_popup2);
            TextView descriptionPopup = dialog.findViewById(R.id.description_text_popup);
            TextView points = dialog.findViewById(R.id.description_point_popup);
            TextView total  = dialog.findViewById(R.id.total_popup);
            description = descriptionEditText.getText().toString().trim();
            ImageButton imageButton = dialog.findViewById(R.id.check2);

            if(TextUtils.isEmpty(description)){
                points.setVisibility(View.INVISIBLE);
                descriptionPopup.setVisibility(View.INVISIBLE);
                total.setText(""+5);
            }
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitReview();
                    dialog.dismiss();
                    Intent intent1 = new Intent(WriteReviewActivity.this
                            ,ReviewActivity.class);
                    intent1.putExtra("id",id);
                    intent1.putExtra("title",title);
                    intent1.putExtra("address",address);
                    intent1.putExtra("website",website);
                    intent1.putExtra("phone",phone);
                    intent1.putExtra("smell",smell);
                    intent1.putExtra("visual",visual);
                    intent1.putExtra("crowd",crowd);
                    intent1.putExtra("light",light);
                    intent1.putExtra("noise",noise);
                    intent1.putExtra("occasion",occasion);
                    intent1.putExtra("description",description1);
                    startActivity(intent1);
                }
            });

        }
    }
    private void submitReview(){
        float noise = noiseRatingBar.getRating();
        float light = lightRatingBar.getRating();
        float visual = visualRatingBar.getRating();
        float smell = smellRatingBar.getRating();
        float crowd = crowdRatingBar.getRating();
        float overall = (crowd+smell+visual+noise+light)/5;
        description = descriptionEditText.getText().toString();
        String occasion = occasionEditText.getText().toString();
        Intent intent = getIntent();
        String uId = mUser.getUid();
        String username = mUser.getDisplayName();
        id = intent.getExtras().getString("id");
        String title = intent.getExtras().getString("title");
        final Review review = new Review(id,title,identfication,username,noise,visual,crowd,smell,light,
                description,uId,occasion);

        databaseReference.child(id+uId).setValue(review);

        if(rating !=null){
            if(review1 ==null){
            Rating rating1 = new Rating((overall+overall2)/2,
                    (rating.getSmell()+smell)/2,(rating.getNoise()+noise)/2
                    ,(rating.getVisual()+visual)/2,(rating.getCrowd()+crowd)/2,
                    (rating.getLight()+light)/2,id);
        databaseReference2.child(id).setValue(rating1);}

        else if(!review1.equals(review)){
                Rating rating1 = new
                        Rating(((rating.getOverall()*2-review1.getOverall())+overall)/2
                        ,((rating.getSmell()*2-review1.getSmell())+smell)/2,
                        ((rating.getNoise()*2-review1.getNoise())+noise)/2,
                        ((rating.getVisual()*2-review1.getVisualInput())+visual)/2,
                        ((rating.getCrowd()*2-review1.getCrowd())+crowd)/2,
                        ((rating.getLight()*2-review1.getLight())+light)/2,
                        id);
                databaseReference2.child(id).setValue(rating1);
            }
        }
        else if(rating==null){
            Rating rating1 = new Rating(overall,smell,noise,visual,crowd,light,id);
            databaseReference2.child(id).setValue(rating1);
        }
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
