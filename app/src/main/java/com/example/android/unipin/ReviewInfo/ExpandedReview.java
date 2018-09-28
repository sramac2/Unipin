package com.example.android.unipin.ReviewInfo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandedReview extends AppCompatActivity {
    private RatingBar smellRatingBar;
    private RatingBar crowdRatingBar;
    private TextView usernameTextView;
    private List<Review> reviewList;
    private RatingBar visualRatingBar;
    private RatingBar noiseRatingBar;
    private RatingBar lightRatingBar;
    private CircleImageView circleImageView;
    private ListView listView;
    private TextView occasionTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_review);

        descriptionTextView = findViewById(R.id.description_expanded);
        //usernameTextView  = findViewById(R.id.username_text_expanded);
        //dateTextView = findViewById(R.id.date_expanded);
        occasionTextView = findViewById(R.id.occasion_text_expanded);
        listView = findViewById(R.id.list_expanded);
        reviewList = new ArrayList<Review>();

        visualRatingBar = findViewById(R.id.visual_ratingbar_expanded);
        noiseRatingBar = findViewById(R.id.noise_ratingbar_expanded);
        smellRatingBar  = findViewById(R.id.smell_ratingbar_expanded);
        lightRatingBar = findViewById(R.id.light_ratingbar_expanded);
        crowdRatingBar = findViewById(R.id.crowd_ratingbar_expanded);

        //circleImageView = findViewById(R.id.profile_image_expanded);

        Intent intent = getIntent();
        final float smell = intent.getExtras().getFloat("smell");
        final float visual = intent.getExtras().getFloat("visual");
        final float light = intent.getExtras().getFloat("light");
        final float crowd = intent.getExtras().getFloat("crowd");
        final float noise = intent.getExtras().getFloat("noise");
        final String description = intent.getExtras().getString("description");
        final String key = intent.getExtras().getString("key");
        final String placeName = intent.getExtras().getString("placeName");
        final String placeID = intent.getExtras().getString("placeID");
        final String username = intent.getExtras().getString("username");
        final String date = intent.getExtras().getString("date");
        final String uID = intent.getExtras().getString("uId");
        final String occasion = intent.getExtras().getString("occasion");
        String identification = intent.getExtras().getString("identification");

        Review review = new Review(placeID,placeName,identification,username,noise,visual,crowd
                ,smell,light,"a",uID,"");
        reviewList.add(review);
        ReviewViewList reviewViewList = new ReviewViewList(ExpandedReview.this,reviewList);
        listView.setAdapter(reviewViewList);

        smellRatingBar.setRating(smell);
        visualRatingBar.setRating(visual);
        noiseRatingBar.setRating(noise);
        lightRatingBar.setRating(light);
        crowdRatingBar.setRating(crowd);
        descriptionTextView.setText(description);
//        usernameTextView.setText(username);
        occasionTextView.setText(occasion);
       // dateTextView.setText(date);

        smellRatingBar.setIsIndicator(true);
        visualRatingBar.setIsIndicator(true);
        noiseRatingBar.setIsIndicator(true);
        crowdRatingBar.setIsIndicator(true);
        lightRatingBar.setIsIndicator(true);

        LayerDrawable stars = (LayerDrawable) smellRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0,255,173),
                PorterDuff.Mode.SRC_ATOP);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(
                uID+".jpg");

        //Glide.with(ExpandedReview.this)
          //      .using(new FirebaseImageLoader())
            //    .load(storageReference)
              //  .into(circleImageView);
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
