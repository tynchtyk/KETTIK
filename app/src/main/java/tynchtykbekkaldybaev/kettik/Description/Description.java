package tynchtykbekkaldybaev.kettik.Description;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tynchtykbekkaldybaev.kettik.Choose_Language;
import tynchtykbekkaldybaev.kettik.MainActivity;
import tynchtykbekkaldybaev.kettik.R;

/**
 * Created by tynchtykbekkaldybaev on 20/12/2018.
 */

public class Description extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TextView next, skip;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.appBarLayout);

        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);

        next.setOnClickListener(mOnKeyClickListener);
        skip.setOnClickListener(mOnKeyClickListener);



        setupViewPager(viewPager, 3);


    }

    private View.OnClickListener mOnKeyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentFragment = viewPager.getCurrentItem();
            if (v.equals(next)) {
                if(currentFragment != 2) {
                    viewPager.setCurrentItem(currentFragment + 1);
                }
                else {
                    finish();
                }
            }
            else if(v.equals(skip)) {
                finish();
            }
        }
    };

    private void setupViewPager(ViewPager viewPager, int fragmentCount) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        Description1 tmp1 = new Description1();
        viewPagerAdapter.addFragment(tmp1);

        Description2 tmp2 = new Description2();
        viewPagerAdapter.addFragment(tmp2);

        Description3 tmp3 = new Description3();
        viewPagerAdapter.addFragment(tmp3);



        viewPager.setAdapter(viewPagerAdapter);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}