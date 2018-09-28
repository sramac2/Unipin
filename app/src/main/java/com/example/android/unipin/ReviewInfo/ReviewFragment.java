package com.example.android.unipin.ReviewInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
    private AdView adView;
    private float overall;
    private List<Review> reviewList;
    private List<String> keys;
    private int count;
    private ListView listView;
    private ImageView searchImage;
    private TextView silentText;
    private Button reviewButton;
    private String placeId;
    private String website;
    private String title;
    private String address;
    private String phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment,container,false);

        reviewList = new ArrayList<Review>();
        reviewButton = view.findViewById(R.id.write_review_fragment);
        keys = new ArrayList<String>();
        listView  = view.findViewById(R.id.list_fragment);
        silentText = view.findViewById(R.id.silent_review);
        searchImage = view.findViewById(R.id.search_review);
        adView = view.findViewById(R.id.adView6);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        placeId = getActivity().getIntent().getExtras().getString("id");
        address = getActivity().getIntent().getExtras().getString("address");
        phone = getActivity().getIntent().getExtras().getString("phone");
        website = getActivity().getIntent().getExtras().getString("website");
        title = getActivity().getIntent().getExtras().getString("title");

        reviewButton.setText("Review "+title);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WriteReviewActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("id",placeId);
                intent.putExtra("address",address);
                intent.putExtra("website",website);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
        databaseReference.orderByChild("placeID").equalTo(placeId)
                .limitToLast(10).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //String key = dataSnapshot.getKey();
                        //System.out.println(key);
                        searchImage.setVisibility(View.INVISIBLE);
                        silentText.setVisibility(View.INVISIBLE);
                        Review review = dataSnapshot.getValue(Review.class);
                        double o =(review.getNoise()+review.getSmell()+review.getVisualInput()
                                +review.getLight()+review.getCrowd())/5;
                        overall = (float) (overall+o)/count;
                        count++;
                        reviewList.add(review);
                        keys.add(dataSnapshot.getKey());
                        //Intent intent = getIntent();
                        //String placeId = intent.getExtras().getString("id");
                        ReviewViewList r = new ReviewViewList(getActivity(),reviewList);
                        listView.setAdapter(r);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Review review = reviewList.get(position);
                                String key = keys.get(position);
                                Intent intent1  = new Intent(getActivity(),
                                        ExpandedReview.class);
                                //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                  //      .makeSceneTransitionAnimation(getActivity(),listView,
                                     //           ViewCompat
                                       //         .getTransitionName(listView));
                                intent1.putExtra("image",R.drawable.material4);
                                intent1.putExtra("key",key);
                                intent1.putExtra("identification",review.getIdentification());
                                intent1.putExtra("noise",review.getNoise());
                                intent1.putExtra("smell",review.getSmell());
                                intent1.putExtra("visual",review.getVisualInput());
                                intent1.putExtra("crowd",review.getCrowd());
                                intent1.putExtra("light",review.getLight());
                                intent1.putExtra("placeName",review.getPlaceName());
                                intent1.putExtra("placeID",review.getPlaceID());
                                intent1.putExtra("occasion",review.getOccasion());
                                intent1.putExtra("uId",review.getuId());
                                intent1.putExtra("username",review.getUsername());
                                intent1.putExtra("description",review.getDescription());
                                intent1.putExtra("date",review.getDate().toString());
                                startActivity(intent1);
                                }
                                });
                        //
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
                        Toast.makeText(getActivity(),databaseError.getMessage()
                                ,Toast.LENGTH_LONG).show();
                        }
                        });
        return view;
    }
}
