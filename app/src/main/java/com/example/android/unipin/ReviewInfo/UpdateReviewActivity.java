package com.example.android.unipin.ReviewInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.android.unipin.HomeActivity;
import com.example.android.unipin.MainActivity;
import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.example.android.unipin.ReviewTransition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateReviewActivity extends AppCompatActivity {
    private RatingBar smellRatingBar;
    private RatingBar crowdRatingBar;
    private RatingBar visualRatingBar;
    private RatingBar noiseRatingBar;
    private RatingBar lightRatingBar;
    private EditText descriptionEditText;
    private EditText occasionEditText;
    private Button updateButton;
    private Button deleteButton;
    private CardView noiseCardView;
    private CardView visualCardView;
    private CardView smellCardView;
    private CardView lightCardView;
    private CardView descriptionCardView;
    private CardView occasionCardView;
    private CardView crowdCardView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);

        noiseCardView = findViewById(R.id.noise_cardview_update);
        crowdCardView = findViewById(R.id.crowd_cardview_update);
        lightCardView = findViewById(R.id.light_cardview_update);
        visualCardView = findViewById(R.id.visual_cardview_update);
        smellCardView = findViewById(R.id.smell_cardview_update);
        occasionCardView = findViewById(R.id.occasion_cardview_update);
        descriptionCardView = findViewById(R.id.description_cardview_update);


        Animation animation = AnimationUtils.loadAnimation(UpdateReviewActivity.this
                , R.anim.slide_in_left);
        noiseCardView.startAnimation(animation);
        crowdCardView.startAnimation(animation);
        lightCardView.startAnimation(animation);
        visualCardView.startAnimation(animation);
        smellCardView.startAnimation(animation);
        occasionCardView.startAnimation(animation);
        descriptionCardView.startAnimation(animation);

        Intent intent = getIntent();
        final float smell = intent.getExtras().getFloat("smell");
        final float visual = intent.getExtras().getFloat("visual");
        float light = intent.getExtras().getFloat("light");
        final float crowd = intent.getExtras().getFloat("crowd");
        final float noise = intent.getExtras().getFloat("noise");
        final String description = intent.getExtras().getString("description");
        final String key = intent.getExtras().getString("key");
        final String placeName = intent.getExtras().getString("placeName");
        final String placeID = intent.getExtras().getString("placeID");
        final String username = intent.getExtras().getString("username");
        final String uID = intent.getExtras().getString("uId");
        final String occasion = intent.getExtras().getString("occasion");

        smellRatingBar = findViewById(R.id.smell_ratingbar_update);
        lightRatingBar = findViewById(R.id.light_ratingbar_update);
        crowdRatingBar  = findViewById(R.id.crowd_ratingbar_update);
        visualRatingBar = findViewById(R.id.visual_ratingbar_update);
        noiseRatingBar = findViewById(R.id.noise_ratingbar_update);
        updateButton = findViewById(R.id.update);
        deleteButton = findViewById(R.id.delete);
        descriptionEditText = findViewById(R.id.description_editText_update);
        occasionEditText = findViewById(R.id.special_occasion_update);

        LayerDrawable stars = (LayerDrawable) visualRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0,255,173), PorterDuff.Mode.SRC_ATOP);



        smellRatingBar.setRating(smell);
        visualRatingBar.setRating(visual);
        noiseRatingBar.setRating(noise);
        lightRatingBar.setRating(light);
        crowdRatingBar.setRating(crowd);
        descriptionEditText.setText(description);
        occasionEditText.setText(occasion);




        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noiseRatingBar.getRating()==0){
                    Toast.makeText(UpdateReviewActivity.this,"Noise rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(smellRatingBar.getRating()==0){
                    Toast.makeText(UpdateReviewActivity.this,"Smell rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(visualRatingBar.getRating()==0){
                    Toast.makeText(UpdateReviewActivity.this,"Visual input rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(crowdRatingBar.getRating()==0){
                    Toast.makeText(UpdateReviewActivity.this,"Crowd rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                if(lightRatingBar.getRating()==0){
                    Toast.makeText(UpdateReviewActivity.this,"Light rating cannot be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = getIntent();
                float smellUpdate = smellRatingBar.getRating();
                float lightUpdate = lightRatingBar.getRating();
                float crowdUpdate = crowdRatingBar.getRating();
                float visualUpdate = visualRatingBar.getRating();
                float noiseUpdate = noiseRatingBar.getRating();

                String placeName = intent.getExtras().getString("placeName");
                String placeID = intent.getExtras().getString("placeID");
                String username = intent.getExtras().getString("username");
                String uID = intent.getExtras().getString("uId");
                String descriptionUpdate = descriptionEditText.getText().toString();
                String occasionUpdate = occasionEditText.getText().toString();

                Review review = new Review(placeID,placeName,"",username,noiseUpdate,visualUpdate
                        ,crowdUpdate,smellUpdate,lightUpdate,
                        descriptionUpdate,uID,occasionUpdate);
                databaseReference.child(key).setValue(review);

                Intent intent1 = new Intent(UpdateReviewActivity.this
                        ,HomeActivity.class);
                intent1.putExtra("uId",uID);
                intent1.putExtra("username",username);
                intent1.putExtra("toast","Your review has been updated");
                startActivity(intent1);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReview(key);
            }
        });
    }

    public void deleteReview(final String key){
        new AlertDialog.Builder(UpdateReviewActivity.this)
                .setTitle("Delete Review")
                .setMessage("Are you sure you want to delete this review?")
                .setIcon(R.drawable.ic_priority_high_black_24dp)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(key).removeValue();
                        Intent intent = getIntent();
                        String username = intent.getExtras().getString("username");
                        String uID = intent.getExtras().getString("uId");
                        finish();
//                        Intent intent1 = new Intent(UpdateReviewActivity.this
//                                ,HomeActivity.class);
//                        intent1.putExtra("uId",uID);
//                        intent1.putExtra("username",username);
//                        intent1.putExtra("toast","Your review has been deleted successfully.");
//                        startActivity(intent1);
                    }
                })
                .setNegativeButton("No",null).show();
    }
}
