package com.example.android.unipin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.unipin.ReviewInfo.Rating;
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

import static android.app.Activity.RESULT_OK;

public class DiscoverFragment extends Fragment {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int PLACE_PICKER_REQ_CODE = 12;
    private Button button;
    private ProgressDialog progressDialog;
    private AdView adView;
    private Review rating;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment,container,false);
        button = view.findViewById(R.id.map);
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(view.getContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacePicker();
            }
        });


        return view;
    }


    private void showPlacePicker() {
        progressDialog.setMessage("Loading Map...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQ_CODE);
            progressDialog.dismiss();
        } catch (Exception e) {
            Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == PLACE_PICKER_REQ_CODE && resultCode == RESULT_OK){
            com.google.android.gms.location.places.Place place =
                    PlacePicker.getPlace(getActivity(), data);
            Intent intent = new Intent(getActivity(),ReviewActivity.class);
            intent.putExtra("id",place.getId());
            intent.putExtra("address",place.getAddress());
            intent.putExtra("phone",place.getPhoneNumber());
            intent.putExtra("website",place.getWebsiteUri());
            intent.putExtra("title",place.getName());

            databaseReference.orderByChild("placeId").equalTo(place.getId())
                    .limitToLast(1)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Rating rating = dataSnapshot.getValue(Rating.class);
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
            startActivity(intent);
        }
    }
}
