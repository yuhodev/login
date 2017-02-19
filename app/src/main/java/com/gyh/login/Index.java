package com.gyh.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.gyh.login.db.Ad;
import com.gyh.login.util.AdLab;
import com.gyh.login.util.BannerViewPager;
import com.gyh.login.util.MyFragmentPagerAdapter;
import com.gyh.login.util.OnPageClickListener;
import com.gyh.login.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Index extends AppCompatActivity {

    private List<ImageView> mViews = new ArrayList<>();

    private ViewPagerAdapter mAdapter;
    private BannerViewPager mBannerViewPager;
    private DrawerLayout mDrawerLayout;
    private FloatingSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        createViews();

        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport (true);

        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner);
        mAdapter = new ViewPagerAdapter(mViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
            }
        });
        mBannerViewPager.setAdapter(mAdapter);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSearchView= (FloatingSearchView) findViewById(R.id.search_view);

        //搜索逻辑
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                //mSearchView.swapSuggestions(newSuggestions);
            }
        });
        // 添加侧边栏
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        // 侧边栏点击逻辑
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.nav_information:
                        break;
                    case R.id.nav_logout:
                        SharedPreferences sharedPreferences = getSharedPreferences("config", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", "");
                        editor.putString("password", "");
                        editor.apply();
                        Intent intent = new Intent(Index.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }
                return true;
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        final String name = getIntent().getStringExtra("name");
        final String intro = getIntent().getStringExtra("intro");

        NavigationView header = (NavigationView) findViewById(R.id.nav_view);
        View headerView = header.getHeaderView(0);

        CircleImageView mIcon;
        mIcon = (CircleImageView) headerView.findViewById(R.id.icon_image);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Index.this, UserIndex.class);
                intent.putExtra("name", name);
                intent.putExtra("intro", intro);
                startActivity(intent);
            }
        });
        TextView user_name = (TextView) headerView.findViewById(R.id.name);
        user_name.setText(name);
        TextView user_intro = (TextView) headerView.findViewById(R.id.intro);
        user_intro.setText(intro);
    }


    private void createViews() {
        AdLab adLab = AdLab.get(Index.this);
        List<Ad> ads = adLab.getAds();

        // 添加最后一个到第一位
        addView(ads, ads.size() - 1);

        for (int i = 0; i < ads.size(); i++) {
            addView(ads, i);
        }

        // 添加第一个到最后一位
        addView(ads, 0);
    }

    private void addView(List<Ad> ads, int index) {
        ImageView iv = (ImageView) LayoutInflater.from(Index.this).inflate(R.layout.ad_item, mBannerViewPager, false);
        Glide.with(Index.this).load(ads.get(index).getImageId()).into(iv);
        mViews.add(iv);
    }
}
