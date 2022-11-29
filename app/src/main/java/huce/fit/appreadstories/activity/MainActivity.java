package huce.fit.appreadstories.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ViewPagerMainAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView btNavigationView;
    private ViewPagerMainAdapter viewPagerMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager2);
        btNavigationView = findViewById(R.id.btNavigationView);

        viewPager();
        processEvents();
    }

    public void viewPager() {
        viewPagerMainAdapter = new ViewPagerMainAdapter(this);
        viewPager2.setAdapter(viewPagerMainAdapter);
        viewPager2.setUserInputEnabled(false);//tắt thao tac vuốt viewpager2
    }

    public void closeMainActivity() {
        finish();
    }

    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.btMenuStory) {
                viewPager2.setCurrentItem(0);
            } else if (id == R.id.btMenuFilter) {
                viewPager2.setCurrentItem(1);
            } else if (id == R.id.btMenuFollow) {
                viewPager();
                viewPager2.setCurrentItem(2);
            } else if (id == R.id.btMenuDownload) {
                viewPager2.setCurrentItem(3);
            } else if (id == R.id.btMenuAccount) {
                viewPager2.setCurrentItem(4);
            }
            return true;
        });
        //chuyển icon
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    btNavigationView.getMenu().findItem(R.id.btMenuStory).setChecked(true);
                } else if (position == 1) {
                    btNavigationView.getMenu().findItem(R.id.btMenuFilter).setChecked(true);
                } else if (position == 2) {
                    btNavigationView.getMenu().findItem(R.id.btMenuFollow).setChecked(true);
                } else if (position == 3) {
                    btNavigationView.getMenu().findItem(R.id.btMenuDownload).setChecked(true);
                } else if (position == 4) {
                    btNavigationView.getMenu().findItem(R.id.btMenuAccount).setChecked(true);
                }
            }
        });
    }

}