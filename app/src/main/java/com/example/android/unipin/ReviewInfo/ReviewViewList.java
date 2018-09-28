package com.example.android.unipin.ReviewInfo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.unipin.R;
import com.example.android.unipin.Review;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewViewList extends ArrayAdapter<Review> {

    private Activity context;
    private List<Review> reviewViewList;
    public ReviewViewList(@NonNull Activity context, List<Review> reviewViewList) {
        super(context, R.layout.review_view_list,reviewViewList);
        this.context = context;
        this.reviewViewList = reviewViewList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.review_view_list,null,true);

        RatingBar overallRatingBar = listViewItem.findViewById(R.id.overall_ratingbar_view);
        overallRatingBar.setIsIndicator(true);
        LayerDrawable stars = (LayerDrawable) overallRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0,255,173), PorterDuff.Mode.SRC_ATOP);

        TextView nameTextView = listViewItem.findViewById(R.id.name_text_view);
        TextView identificationTextView = listViewItem.findViewById(R.id.role);
        TextView occasionTextView = listViewItem.findViewById(R.id.occasion_text_view);
        TextView dateTextView = listViewItem.findViewById(R.id.date_text_view);
        TextView descriptionTextView = listViewItem.findViewById(R.id.description_text_view);
        CircleImageView imageView = listViewItem.findViewById(R.id.profile_photo_view);

        Review review = reviewViewList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(
                review.getuId()+".jpg");

        nameTextView.setText(review.getUsername());
        identificationTextView.setText(review.getIdentification());
        occasionTextView.setText(review.getOccasion());
        dateTextView.setText(review.getDate().toString());
        String description = review.getDescription();
        String d = "";
        if(TextUtils.isEmpty(description)){
            descriptionTextView.setVisibility(View.GONE);
        }
        if(description.length()>25){
            for(int i =0;i<=25;i++){
                d = d + description.charAt(i);
            }
            d = d+"...";
            descriptionTextView.setText(d);
        }else{
        descriptionTextView.setText(description);}
        float overall = (review.getCrowd()+review.getLight()+
                review.getVisualInput()+review.getSmell()+review.getNoise())/5;
        overallRatingBar.setRating(overall);


        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);
        if(description.equals("a")){
            descriptionTextView.setText("");
            descriptionTextView.setVisibility(View.GONE);
        }
        if(!description.equals("a")) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            listViewItem.startAnimation(animation);
        }


        return listViewItem;
    }
}
