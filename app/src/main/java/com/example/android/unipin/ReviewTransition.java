package com.example.android.unipin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.unipin.R;
import com.example.android.unipin.ReviewInfo.UpdateReviewActivity;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;

public class ReviewTransition extends AppCompatActivity {

    private RatingBar smellRatingBar;
    private RatingBar crowdRatingBar;
    private TextView usernameTextView;
    private RatingBar visualRatingBar;
    private RatingBar noiseRatingBar;
    private RatingBar lightRatingBar;
    private TextView placeNameTextView;
    private ProgressDialog progressDialog;
    private TextView occasionTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private FloatingActionButton editButton;
    private ImageView imageView;
    private GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_transition);

        mGeoDataClient = Places.getGeoDataClient(this);
        progressDialog = new ProgressDialog(this);
        descriptionTextView = findViewById(R.id.description_transition);
        usernameTextView  = findViewById(R.id.username_transition);
        dateTextView = findViewById(R.id.date_transition);
        occasionTextView = findViewById(R.id.occasion_t);
        placeNameTextView = findViewById(R.id.place_name_transition);
        editButton = findViewById(R.id.edit_button);

        visualRatingBar = findViewById(R.id.visual_ratingbar_transition);
        noiseRatingBar = findViewById(R.id.noise_ratingbar_transition);
        smellRatingBar  = findViewById(R.id.smell_ratingbar_transition);
        lightRatingBar = findViewById(R.id.light_ratingbar_transition);
        crowdRatingBar = findViewById(R.id.crowd_ratingbar_transition);
        imageView = findViewById(R.id.place_image_transition);

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

        smellRatingBar.setRating(smell);
        visualRatingBar.setRating(visual);
        noiseRatingBar.setRating(noise);
        placeNameTextView.setText(placeName);
        lightRatingBar.setRating(light);
        crowdRatingBar.setRating(crowd);
        descriptionTextView.setText(description);
        usernameTextView.setText(username);

//        if(occasion==null|| TextUtils.isEmpty(occasion))occasionTextView.setVisibility(View.GONE);
//        else{
//            occasionTextView.setText(occasion);
//        }
        dateTextView.setText(date);


        smellRatingBar.setIsIndicator(true);
        visualRatingBar.setIsIndicator(true);
        noiseRatingBar.setIsIndicator(true);
        crowdRatingBar.setIsIndicator(true);
        lightRatingBar.setIsIndicator(true);
        getPhotos(placeID,placeName);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(ReviewTransition.this
                        ,UpdateReviewActivity.class);
                intent1.putExtra("image",R.drawable.material4);
                intent1.putExtra("key",key);
                intent1.putExtra("noise",noise);
                intent1.putExtra("smell",smell);
                intent1.putExtra("visual",visual);
                intent1.putExtra("crowd",crowd);
                intent1.putExtra("light",light);
                intent1.putExtra("placeName",placeName);
                intent1.putExtra("placeID",placeID);
                intent1.putExtra("occasion",occasion);
                intent1.putExtra("uId",uID);
                intent1.putExtra("username",username);
                intent1.putExtra("description",description);
                intent1.putExtra("date",date);
                startActivity(intent1);
            }
        });
    }
    private void getPhotos(String placeId, String placeName) {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                //Get the list of photos.
                PlacePhotoMetadataResponse photos = null;
                try {
                    photos = task.getResult();
                }
                catch (RuntimeExecutionException e){
                    progressDialog.hide();
                }
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                try {
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions();

                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoMetadataBuffer.release();
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            imageView.setImageBitmap(bitmap);
                            progressDialog.hide();
                        }
                    });

                }
                catch (IllegalStateException e) {
                    imageView.setImageResource(R.drawable.spacedesktop);
                    progressDialog.hide();
                    return;
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }
}
