package com.example.android.unipin.ReviewInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlaceInfoFragment extends android.support.v4.app.Fragment {
    private GeoDataClient mGeoDataClient;
    private ImageView placePhoto;
    private TextView websiteText;
    private float overall=0;
    private TextView phoneText;
    private TextView titleText;
    private TextView addressText;
    private ProgressDialog progressDialog;
    private Button reviewButton;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference2;
    private Review review1;
    private Rating rating;
    private RatingBar smellRatingBar;
    private RatingBar crowdRatingBar;
    private RatingBar lightRatingBar;
    private RatingBar visualRatingbar;
    private RatingBar noiseRatingBar;
    private RatingBar overallRatingBar;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_info_fragment,container,false);



        progressDialog = new ProgressDialog(getActivity());
        mGeoDataClient = Places.getGeoDataClient(getActivity(),null);
        placePhoto = view.findViewById(R.id.photo_place_info);
        websiteText = view.findViewById(R.id.website_place_info);
        phoneText = view.findViewById(R.id.phone_place_info);
        addressText = view.findViewById(R.id.address_place_info);
        titleText = view.findViewById(R.id.title_place_info);
        reviewButton  = view.findViewById(R.id.write_place_info);
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        overallRatingBar = view.findViewById(R.id.overall_place_info);
        smellRatingBar = view.findViewById(R.id.smell_place_info);
        visualRatingbar = view.findViewById(R.id.visual_place_info);
        crowdRatingBar = view.findViewById(R.id.crowd_place_info);
        lightRatingBar = view.findViewById(R.id.light_place_info);
        noiseRatingBar  = view.findViewById(R.id.noise_place_info);


        final String address = getActivity().getIntent().getExtras().getString("address");
        final String phone = getActivity().getIntent().getExtras().getString("phone");
        final Uri website = (Uri) getActivity().getIntent().getExtras().get("website");
        final String title = getActivity().getIntent().getExtras().getString("title");
        reviewButton.setText("review "+title);


        getRef();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("ratings");
        databaseReference2.orderByChild("placeId").equalTo(id).limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        rating = dataSnapshot.getValue(Rating.class);
                        if(rating!=null){
                            overallRatingBar.setRating(rating.getOverall());
                            crowdRatingBar.setRating(rating.getCrowd());
                            lightRatingBar.setRating(rating.getLight());
                            noiseRatingBar.setRating(rating.getNoise());
                            smellRatingBar.setRating(rating.getSmell());
                            visualRatingbar.setRating(rating.getVisual());
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

        overallRatingBar.setIsIndicator(true);
        smellRatingBar.setIsIndicator(true);
        crowdRatingBar.setIsIndicator(true);
        noiseRatingBar.setIsIndicator(true);
        lightRatingBar.setIsIndicator(true);
        visualRatingbar.setIsIndicator(true);


        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WriteReviewActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                intent.putExtra("website",website);
                intent.putExtra("phone",phone);
                if(review1!=null){
                intent.putExtra("smell",review1.getSmell());
                intent.putExtra("visual",review1.getVisualInput());
                intent.putExtra("crowd",review1.getCrowd());
                intent.putExtra("light",review1.getLight());
                intent.putExtra("noise",review1.getNoise());
                intent.putExtra("occasion",review1.getOccasion());
                intent.putExtra("description",review1.getDescription());}
                startActivity(intent);
            }
        });

        SpannableString spannableString =
                new SpannableString(websiteText.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, website));
            }
        };

        spannableString.setSpan(clickableSpan,0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        websiteText.setText(spannableString,TextView.BufferType.SPANNABLE);

        websiteText.setMovementMethod(LinkMovementMethod.getInstance());

        addressText.setText(address);
        phoneText.setText(phone);
        if(address.isEmpty())addressText.setVisibility(View.INVISIBLE);
        if(website==null)
            websiteText.setVisibility(View.INVISIBLE);
        Linkify.addLinks(phoneText,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(addressText,Linkify.MAP_ADDRESSES);
        progressDialog.setMessage("Loading...");

        getPhotos(id,title);

        return view;
    }


    private void getPhotos(String placeId, String placeName) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        titleText.setText(placeName);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
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

    private void getRef(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reviews");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        id = getActivity().getIntent().getExtras().getString("id");
        databaseReference.orderByKey().equalTo(id+firebaseUser.getUid())
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
                        Toast.makeText(getActivity()
                                ,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
