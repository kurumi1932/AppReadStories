package huce.fit.appreadstories.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.account.manager.AccountManagerFragment;
import huce.fit.appreadstories.story.download.StoryDownloadFragment;
import huce.fit.appreadstories.story.filter.StoryFilterFragment;
import huce.fit.appreadstories.story.follow.StoryFollowFragment;
import huce.fit.appreadstories.story.story.StoryFragment;

public class MainActivity extends AppCompatActivity implements MainView {
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private BottomNavigationView btNavigationView;

    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        processEvents();
    }


    private void initView() {
        mainPresenter = new MainImpl(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        btNavigationView = findViewById(R.id.btNavigationView);

        if (mainPresenter.isNetwork()) {
            addFragment(new StoryFragment(), R.id.btMenuStory);
        } else {
            addFragment(new StoryDownloadFragment(), R.id.btMenuDownload);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mainPresenter.isNetwork()) {
            changeFragment(R.id.btMenuDownload, new StoryDownloadFragment());
        }
    }

    private void addFragment(Fragment fragment, int id) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment, fragment, null);
        mFragmentTransaction.commit();

        btNavigationView.getMenu().findItem(id).setChecked(true);
        mainPresenter.addFragment(id);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.fragment, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    private void changeFragment(int id, Fragment fragment) {
        if (!btNavigationView.getMenu().findItem(id).isChecked()) {
            mainPresenter.changeFragment(id);
            btNavigationView.getMenu().findItem(id).setChecked(true);
            replaceFragment(fragment);
        }
    }

    @Override
    public void closeMain() {
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.btMenuStory:
                    changeFragment(id, new StoryFragment());
                    break;
                case R.id.btMenuFilter:
                    changeFragment(id, new StoryFilterFragment());
                    break;
                case R.id.btMenuFollow:
                    changeFragment(id, new StoryFollowFragment());
                    break;
                case R.id.btMenuDownload:
                    changeFragment(id, new StoryDownloadFragment());
                    break;
                case R.id.btMenuAccount:
                    changeFragment(id, new AccountManagerFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        int count = mainPresenter.backPressed();
        if (count < 0) {
            finish();
        } else if (count == 0) {
            Toast.makeText(MainActivity.this, "Nhấn lần nữa để thoát ứng dụng!", Toast.LENGTH_SHORT).show();
        } else {
            btNavigationView.getMenu().findItem(mainPresenter.getId()).setChecked(true);
            mainPresenter.removeCount();
            super.onBackPressed();
        }
    }
}