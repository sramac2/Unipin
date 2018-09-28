package com.example.android.unipin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.unipin.ReviewInfo.ReviewActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserReviewsFragment extends Fragment {

    private ListView listView;
    private TextView usernameTextView;
    private List<Review> reviewList;
    private ProgressDialog progressDialog;
    private ArrayList<String> keys;
    private TextView placeNameTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Context context;
    private AdView adView;
    private Button lookButton;
    private Button writeButton;
    private TextView silentText;
    private ImageView searchImage;
    private static final int PLACE_PICKER_REQ_CODE = 12;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("reviews");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_reviews_fragment,container,false);
        reviewList = new ArrayList<Review>();
        listView = view.findViewById(R.id.list_user_reviews);
        progressDialog = new ProgressDialog(getActivity());
        keys = new ArrayList<String>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        adView = view.findViewById(R.id.adView5);
        searchImage = view.findViewById(R.id.search_image);
        silentText = view.findViewById(R.id.silent);
        writeButton = view.findViewById(R.id.write_user_reviews);
        lookButton  = view.findViewById(R.id.look_user_reviews);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        //placeNameTextView = findViewById(R.id.list_place_name);
        //usernameTextView = findViewById(R.id.list_username);

        //        final Intent intent = getIntent();
//        String uid = intent.getExtras().getString("uId");
        //      String username = intent.getExtras().getString("username");
        //    String toast = intent.getExtras().getString("toast");
        String toast = "";
        if (!TextUtils.isEmpty(toast)) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }
        //usernameTextView.setText(username+"'s reviews");
        progressDialog.show();
        fillData();
        progressDialog.hide();

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacePicker();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Review review = reviewList.get(position);
                String key = keys.get(position);
                Intent intent1 = new Intent(getActivity(),
                        ReviewTransition.class);

                intent1.putExtra("image", R.drawable.material4);
                intent1.putExtra("key", key);
                intent1.putExtra("noise", review.getNoise());
                intent1.putExtra("smell", review.getSmell());
                intent1.putExtra("visual", review.getVisualInput());
                intent1.putExtra("crowd", review.getCrowd());
                intent1.putExtra("light", review.getLight());
                intent1.putExtra("placeName", review.getPlaceName());
                intent1.putExtra("placeID", review.getPlaceID());
                intent1.putExtra("occasion", review.getOccasion());
                intent1.putExtra("uId", review.getuId());
                intent1.putExtra("username", review.getUsername());
                intent1.putExtra("description", review.getDescription());
                intent1.putExtra("date", review.getDate().toString());
                startActivity(intent1);
            }
        });
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        fillData();
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQ_CODE && resultCode == RESULT_OK){
            com.google.android.gms.location.places.Place place =
                    PlacePicker.getPlace(getActivity(), data);
            Intent intent = new Intent(getActivity(),ReviewActivity.class);
            intent.putExtra("id",place.getId());
            intent.putExtra("address",place.getAddress());
            intent.putExtra("phone",place.getPhoneNumber());
            intent.putExtra("website",place.getWebsiteUri());
            intent.putExtra("title",place.getName());
            startActivity(intent);
        }
    }

    public void fillData(){
        databaseReference.orderByChild("uId").equalTo(firebaseUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        //progressDialog.setMessage("Loading Reviews...");
                        //progressDialog.show();
                        searchImage.setVisibility(View.INVISIBLE);
                        lookButton.setVisibility(View.VISIBLE);
                        writeButton.setVisibility(View.INVISIBLE);
                        silentText.setVisibility(View.INVISIBLE);
                        keys.add(dataSnapshot.getKey());
                        Review review = dataSnapshot.getValue(Review.class);
                        if(!reviewList.contains(review)){reviewList.add(review);}
                        ReviewList r = new ReviewList(getActivity(), reviewList);
                        //placeNameTextView.setText(review.getPlaceName());
                        listView.setAdapter(r);
                        progressDialog.hide();
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
                        if(getActivity()!=null){
                            Toast.makeText(getActivity(), databaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();}
                    }
                });
    }
}
