package com.example.android.unipin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private AdView adView;
    private TextView signoutText;
    private TextView aboutusText;
    private TextView contactusText;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);
        adView = view.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mAuth = FirebaseAuth.getInstance();
        signoutText = view.findViewById(R.id.sign_out);
        aboutusText = view.findViewById(R.id.about_us);
        contactusText = view.findViewById(R.id.contact_us);

        signoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        aboutusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AboutUs.class));
            }
        });

        contactusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ContactUsActivity.class));
            }
        });
        return view;
    }
}
