package com.example.android.unipin;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.unipin.ReviewInfo.Rating;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity implements RewardedVideoAdListener{

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private Dialog dialog;
    private TextView rewardVideo;
    private ImageButton imageButton;
    private RewardedVideoAd mRewardedVideoAd;
    private DatabaseReference databaseReference;
    private int currentPoints;
    private Review review1;
    private Rating rating;
    private String uId;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.reward_video_description);
        rewardVideo = dialog.findViewById(R.id.reward_video);
        imageButton = dialog.findViewById(R.id.close);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        rewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRewardedVideoAd.isLoaded()){
                    mRewardedVideoAd.show();
                }
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager()   );

        viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_explore_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_gesture_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings_black_24dp);

        databaseReference.orderByChild("uId").equalTo(firebaseUser.getUid()).limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User user = dataSnapshot.getValue(User.class);
                        currentPoints = user.getPoints();
                        user.getuId();
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

        Intent intent = getIntent();
        if (intent.hasExtra("toast")){
            Toast.makeText(HomeActivity.this,intent.getExtras().getString("toast")
            ,Toast.LENGTH_SHORT).show();
        }


    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiscoverFragment(), "Discover");
        adapter.addFragment(new UserReviewsFragment(), "Your Reviews");
        adapter.addFragment(new AccountFragment(), "Me");
        adapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preference,menu);
        menuInflater.inflate(R.menu.reward_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this,HelpActivity.class));
                break;
            case R.id.contact_us:
                startActivity(new Intent(this,ContactUsActivity.class));
                break;
            case R.id.reward_menu:
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this,"You have received 30 points. Congrats."
                ,Toast.LENGTH_SHORT).show();
        databaseReference.child(firebaseUser.getUid()).child("points").setValue(currentPoints+30);
        dialog.dismiss();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
