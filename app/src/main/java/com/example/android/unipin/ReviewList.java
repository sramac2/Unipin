package com.example.android.unipin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewList extends ArrayAdapter<Review> {

    private Activity context;
    private List<Review> reviewList;
    private GeoDataClient mGeoDataClient;
    private CircleImageView placePhoto;
    private ProgressDialog progressDialog;
    public ReviewList(Activity context, List<Review> reviewList) {

        super(context, R.layout.user_reviews_fragment, reviewList);
        this.context = context;

        this.reviewList = reviewList;
    }
    /*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem  = inflater.inflate(R.layout.review_list,null, true);

        TextView nameTextView = listViewItem.findViewById(R.id.username_result);
        TextView placeNameTextView = listViewItem.findViewById(R.id.list_place_name);
        TextView dateTextView = listViewItem.findViewById(R.id.date);
        TextView noiseTextView = listViewItem.findViewById(R.id.noiseTextView_result);
        TextView smellTextView = listViewItem.findViewById(R.id.smellTextView_result);
        TextView crowdTextView = listViewItem.findViewById(R.id.crowdTextView_result);
        TextView lightTextView = listViewItem.findViewById(R.id.lightTextView_result);
        TextView visualTextView = listViewItem.findViewById(R.id.visualTextView_result);
        TextView descriptionTextView = listViewItem.findViewById(R.id.description_display);
        TextView specialOccasionTextView = listViewItem.findViewById(R.id.special_occasion);


        Typeface myFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/SourceSansPro-Bold.otf");
        nameTextView.setTypeface(myFont);
        placeNameTextView.setTypeface(myFont);
        dateTextView.setTypeface(myFont);
        noiseTextView.setTypeface(myFont);
        smellTextView.setTypeface(myFont);
        crowdTextView.setTypeface(myFont);
        lightTextView.setTypeface(myFont);
        visualTextView.setTypeface(myFont);
        descriptionTextView.setTypeface(myFont);


        RatingBar smellRatingBar = listViewItem.findViewById(R.id.ratingBar4);
        smellRatingBar.setIsIndicator(true);
        RatingBar visualRatingBar = listViewItem.findViewById(R.id.visual_ratingbar_result);
        visualRatingBar.setIsIndicator(true);
        RatingBar lightRatingBar = listViewItem.findViewById(R.id.light_ratingbar_result);
        lightRatingBar.setIsIndicator(true);
        RatingBar noiseRatingBar = listViewItem.findViewById(R.id.noise_ratingbar_result);
        noiseRatingBar.setIsIndicator(true);
        RatingBar crowdRatingBar = listViewItem.findViewById(R.id.crowd_ratingbar_result);
        crowdRatingBar.setIsIndicator(true);

        LayerDrawable stars = (LayerDrawable) visualRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0,255,173), PorterDuff.Mode.SRC_ATOP);


        Review review = reviewList.get(position);
        specialOccasionTextView.setText(review.getOccasion());
        nameTextView.setText(review.getUsername());
        placeNameTextView.setText(review.getPlaceName());
        dateTextView.setText(review.getDate().toString());
        descriptionTextView.setText(review.getDescription());

        smellRatingBar.setRating(review.getSmell());
        visualRatingBar.setRating(review.getVisualInput());
        lightRatingBar.setRating(review.getLight());
        crowdRatingBar.setRating(review.getCrowd());
        noiseRatingBar.setRating(review.getNoise());

        return listViewItem;

    <TextView
        android:id="@+id/username_result"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="4dp"
        android:text="Name"
        android:textSize="20dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/date"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="4dp"
        android:layout_marginTop="32dp"
        android:text="date"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/username_result" />


    <TextView
        android:id="@+id/noiseTextView_result"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="4dp"
        android:layout_marginTop="24dp"
        android:text="Noise"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/date" />

    <TextView
        android:id="@+id/description_display"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="4dp"
        android:layout_marginTop="48dp"
        android:text="Description"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/ratingBar4" />

    <RatingBar
        android:id="@+id/noise_ratingbar_result"
        style="?android:attr/ratingBarStyleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_marginStart="52dp"
        android:layout_marginTop="24dp"
        android:Rating="5"
        android:scaleX="0.5"
        android:scaleY="0.5"
        android:theme="@style/MyRatingBar"
        android:transformPivotX="0dp"
        android:transformPivotY="0dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/date" />

    <TextView
        android:id="@+id/visualTextView_result"
        android:layout_width="wrap_content"
        android:layout_height="16dp"
        android:layout_marginTop="100dp"
        android:text="Visual Input"
        app:layout_constraintEnd_toStartOf="@+id/visual_ratingbar_result"
        app:layout_constraintTop_toTopOf="parent" />

    <RatingBar
        android:id="@+id/visual_ratingbar_result"
        style="?android:attr/ratingBarStyleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:Rating="5"
        android:scaleX="0.5"
        android:scaleY="0.5"
        android:theme="@style/MyRatingBar"
        android:transformPivotX="0dp"
        android:transformPivotY="0dp"
        app:layout_constraintStart_toEndOf="@+id/noise_ratingbar_result"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/smellTextView_result"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="smell"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/noiseTextView_result" />

    <RatingBar
        android:id="@+id/ratingBar4"
        style="?android:attr/ratingBarStyleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:isIndicator="false"
        android:Rating="5"
        android:scaleX="0.5"
        android:scaleY="0.5"
        android:theme="@style/MyRatingBar"
        android:transformPivotX="0dp"
        android:transformPivotY="0dp"
        app:layout_constraintStart_toEndOf="@+id/smellTextView_result"
        app:layout_constraintTop_toBottomOf="@+id/noise_ratingbar_result" />

    <TextView
        android:id="@+id/crowdTextView_result"
        android:layout_width="wrap_content"
        android:layout_height="18dp"
        android:layout_marginStart="188dp"
        android:layout_marginTop="4dp"
        android:text="Crowd"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/noise_ratingbar_result" />

    <RatingBar
        android:id="@+id/crowd_ratingbar_result"
        style="?android:attr/ratingBarStyleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:Rating="5"
        android:scaleX="0.5"
        android:scaleY="0.5"
        android:theme="@style/MyRatingBar"
        android:transformPivotX="0dp"
        android:transformPivotY="0dp"

        app:layout_constraintStart_toEndOf="@+id/ratingBar4"
        app:layout_constraintTop_toBottomOf="@+id/visual_ratingbar_result" />

    <TextView
        android:id="@+id/lightTextView_result"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="14dp"
        android:layout_marginStart="72dp"
        android:layout_marginTop="12dp"
        android:text="Light"
        app:layout_constraintEnd_toStartOf="@+id/light_ratingbar_result"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/ratingBar4" />

    <RatingBar
        android:id="@+id/light_ratingbar_result"
        style="?android:attr/ratingBarStyleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="87dp"
        android:layout_marginTop="12dp"
        android:Rating="2"
        android:scaleX="0.5"
        android:scaleY="0.5"
        android:theme="@style/MyRatingBar"
        android:transformPivotX="0dp"

        android:transformPivotY="0dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toEndOf="@+id/lightTextView_result"
        app:layout_constraintTop_toBottomOf="@+id/ratingBar4" />

    <TextView
        android:id="@+id/list_place_name"
        android:layout_width="wrap_content"
        android:layout_height="0dp"
        android:layout_marginStart="4dp"
        android:textSize="20dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/username_result" />

    <TextView
        android:id="@+id/special_occasion"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="18dp"
        android:textSize="20dp"
        app:layout_constraintStart_toEndOf="@+id/username_result"
        app:layout_constraintTop_toTopOf="parent" />

    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.review_list,null,true);

        mGeoDataClient = Places.getGeoDataClient(context,null);
        progressDialog = new ProgressDialog(context);


        RatingBar overallRatingBar = listViewItem.findViewById(R.id.review_edit_ratingbar);
        overallRatingBar.setIsIndicator(true);
        LayerDrawable stars = (LayerDrawable) overallRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0,255,173), PorterDuff.Mode.SRC_ATOP);

        TextView descriptionTextView = listViewItem.findViewById(R.id.review_edit_description);
        TextView dateTextView = listViewItem.findViewById(R.id.review_edit_date);
        TextView occasionTextView = listViewItem.findViewById(R.id.review_edit_occasion);
        //CircleImageView imageView = listViewItem.findViewById(R.id.review_edit_place);
        TextView placeNameTextView = listViewItem.findViewById(R.id.review_edit_place_name);
        placePhoto= listViewItem.findViewById(R.id.review_edit_place);



        Review review = reviewList.get(position);
        //Random random = new Random();

        //int r = random.nextInt(4);

        float rating = (review.getNoise()+review.getSmell()+
                review.getVisualInput()+review.getLight()+review.getCrowd())/5;
        overallRatingBar.setRating(rating);
        String description = review.getDescription();
        String d = "";
        if(description.length()>25){
            for(int i =0;i<=25;i++){
                d = d + description.charAt(i);
            }
            d = d+"...";
            descriptionTextView.setText(d);
        }else{
        descriptionTextView.setText(review.getDescription());}
        occasionTextView.setText(review.getOccasion());
        placeNameTextView.setText(review.getPlaceName());
        if(review.getDate()!=null){
        dateTextView.setText(review.getDate().toString());}


        //imageView.setImageResource(R.drawable.material1);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();


        //switch (r){
         //case 0: imageView.setImageResource(R.drawable.spacedesktop);break;
         //case 1: imageView.setImageResource(R.drawable.material3);break;
           // case 2:imageView.setImageResource(R.drawable.material4);break;
         //case 2: imageView.setImageResource(R.drawable.material3);break;
        //case 3: imageView.setImageResource(R.drawable.back4);
        //}
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        listViewItem.startAnimation(animation);
        return listViewItem;
    }

    private void getPhotos(String placeId) {
        progressDialog.show();
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
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
                            placePhoto.setImageBitmap(bitmap);
                            progressDialog.hide();
                        }
                    });
                }
                catch (IllegalStateException e){
                    placePhoto.setImageResource(R.drawable.spacedesktop);
                    progressDialog.hide();
                    return;
                }


            }
        });
    }
}
