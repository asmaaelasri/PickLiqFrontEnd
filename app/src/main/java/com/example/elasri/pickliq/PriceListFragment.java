package com.example.elasri.pickliq;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriceListFragment extends Fragment {

    public PriceListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_price_list, container, false);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new BeerFragment(), getResources().getString(R.string.beer));
        adapter.addFragment(new VodkaFragment(), getResources().getString(R.string.vodka));
        adapter.addFragment(new BrandyFragment(), getResources().getString(R.string.brandy));
            adapter.addFragment(new WineFragment(), getResources().getString(R.string.wine));
        adapter.addFragment(new WhiskeyFragment(), getResources().getString(R.string.whiskey));
        adapter.addFragment(new RumFragment(), getResources().getString(R.string.rum));
        adapter.addFragment(new GinFragment(), getResources().getString(R.string.gin));
        adapter.addFragment(new TequilaFragment(), getResources().getString(R.string.tequila));
        adapter.addFragment(new BreezerFragment(), getResources().getString(R.string.breezer));
        adapter.addFragment(new ChampagneFragment(), getResources().getString(R.string.champagne));
        adapter.addFragment(new LiqueurFragment(), getResources().getString(R.string.liqueur));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}


