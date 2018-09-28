package com.example.android.unipin.ReviewInfo;

import android.content.Intent;
import android.net.Uri;
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

import com.example.android.unipin.ContactUsActivity;
import com.example.android.unipin.HelpActivity;
import com.example.android.unipin.HomeActivity;
import com.example.android.unipin.MainActivity;
import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.example.android.unipin.SectionsPageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Review review1;
    private String address;
    private String phone;
    private String title;
    private Uri website;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        databaseReference = FirebaseDatabase.getInstance().getReference("reviews");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        viewPager = findViewById(R.id.pager2);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs3);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();

        placeId = intent.getExtras().getString("id");
        address = intent.getExtras().getString("address");
        phone = intent.getExtras().getString("phone");
        website = (Uri) intent.getExtras().get("website");
        title = intent.getExtras().getString("title");

        databaseReference.orderByKey().equalTo(placeId+firebaseUser.getUid()).limitToFirst(1)
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

                    }
                });

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PlaceInfoFragment(), "Info");
        adapter.addFragment(new ReviewFragment(),"Reviews");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preference,menu);
        menuInflater.inflate(R.menu.review_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this,HelpActivity.class));
                break;
            case R.id.contact_us:
                startActivity(new Intent(ReviewActivity.this,ContactUsActivity.class));
                break;
            case R.id.review_menu:
                Intent intent = new Intent(this,WriteReviewActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("id",placeId);
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
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReviewActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
