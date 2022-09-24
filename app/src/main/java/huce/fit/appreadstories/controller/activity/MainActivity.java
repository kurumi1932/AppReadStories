package huce.fit.appreadstories.controller.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.controller.adapters.ViewPagerMainAdapter;

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

        viewPagerMainAdapter = new ViewPagerMainAdapter(this);
        viewPager2.setAdapter(viewPagerMainAdapter);
        viewPager2.setUserInputEnabled(false);//tắt thao tac vuốt viewpager2

        processEvents();
    }

    public void closeMainActivity(){
        finish();
    }

    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.btMenuStory:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.btMenuFilter:
                    viewPager2.setCurrentItem(1);
                    break;
                case R.id.btMenuStoryFollow:
                    viewPager2.setCurrentItem(2);
                    break;
                case R.id.btMenuAccount:
                    viewPager2.setCurrentItem(3);
                    break;
            }
            return true;
        });
        //chuyển icon
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        btNavigationView.getMenu().findItem(R.id.btMenuStory).setChecked(true);
                        break;
                    case 1:
                        btNavigationView.getMenu().findItem(R.id.btMenuFilter).setChecked(true);
                        break;
                    case 2:
                        btNavigationView.getMenu().findItem(R.id.btMenuStoryFollow).setChecked(true);
                        break;
                    case 3:
                        btNavigationView.getMenu().findItem(R.id.btMenuAccount).setChecked(true);
                        break;
                }
            }
        });
    }

}