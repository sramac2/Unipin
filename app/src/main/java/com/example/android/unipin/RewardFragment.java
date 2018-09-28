package com.example.android.unipin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class RewardFragment extends Fragment {
    private TextView pointsText;
    private TextView reviewCountText;
    private TextView levelDescription;
    private TextView levelText;
    private ImageView profileImage;
    private ImageView trophyImage;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private int currentPoints;
    private StorageReference storageReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reward_fragment,container,false);

        pointsText = view.findViewById(R.id.points_reward);
        reviewCountText  = view.findViewById(R.id.review_count_reward);
        levelText = view.findViewById(R.id.level_reward);
        levelDescription = view.findViewById(R.id.level_description_reward);
        profileImage  = view.findViewById(R.id.profile_reward);
        trophyImage = view.findViewById(R.id.trophy_reward);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()
                + ".jpg");
        image();
        updateUI();
        return view;
    }

    private void updateUI(){
        databaseReference.orderByChild("uId").equalTo(firebaseUser.getUid()).limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User user = dataSnapshot.getValue(User.class);
                        currentPoints = user.getPoints();
                        setTrophy(user.getLevelNumber());
                        levelText.setText(""+user.getLevelNumber());
                        levelDescription.setText("You are a "+user.getLevelName());
                        if(user.getLevelName().startsWith("E"))
                            levelDescription.setText("You are an "+user.getLevelName());
                        reviewCountText.setText(user.getReviewCount()+"");
                        pointsText.setText(user.getPoints()+"");
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
    }
    private void setTrophy(int levelNum){
        switch (levelNum){
            case 4: trophyImage.setImageResource(R.drawable.elite_reviewer);break;
            case 5: trophyImage.setImageResource(R.drawable.seeker);break;
            case 6: trophyImage.setImageResource(R.drawable.real_reviewer);break;
            case 7: trophyImage.setImageResource(R.drawable.review_veteran);break;
            case 8: trophyImage.setImageResource(R.drawable.elite_review_veteran);break;
            case 9: trophyImage.setImageResource(R.drawable.rigorous_reviewer);break;
            case 10: trophyImage.setImageResource(R.drawable.primary_reviewer);break;
            case 11: trophyImage.setImageResource(R.drawable.professional_reviewer);break;
            case 12: trophyImage.setImageResource(R.drawable.best_reviewer);break;
            case 13: trophyImage.setImageResource(R.drawable.advanced_reviewer);break;
            case 15: trophyImage.setImageResource(R.drawable.the_incomparable);break;
        }

    }
    private void image(){
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(profileImage);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }
}
