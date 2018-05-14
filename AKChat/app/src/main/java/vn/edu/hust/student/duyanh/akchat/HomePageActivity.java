package vn.edu.hust.student.duyanh.akchat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SectionPagerAdapter mSectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        mViewPager = (ViewPager) findViewById(R.id.home_page_view);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);

        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("AKChat");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_menu_sign_out) {
            mAuth.signOut();
            Intent sign_in_intent = new Intent(HomePageActivity.this, SignInActivity.class);
            startActivity(sign_in_intent);
            finish();
        } else if(item.getItemId() == R.id.main_menu_settings) {
            Intent intent = new Intent(HomePageActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.main_menu_all_user) {
            Intent intent = new Intent(HomePageActivity.this, AllUserActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
