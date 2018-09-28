package com.example.android.unipin;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountFragment extends Fragment {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment,container,false);

        sectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.account_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_track_changes_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_outline_black_24dp);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new RewardFragment(), "Rewards");
        adapter.addFragment(new AccountInfoFragment(), "Info");
        viewPager.setAdapter(adapter);
    }
}
