package huce.fit.appreadstories.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.fragment.AccountFragment;
import huce.fit.appreadstories.fragment.StoryDownloadFragment;
import huce.fit.appreadstories.fragment.StoryFilterFragment;
import huce.fit.appreadstories.fragment.StoryFollowFragment;
import huce.fit.appreadstories.fragment.StoryFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView btNavigationView;
    private HashMap<Integer, Integer> fragmentOld = new HashMap<>();
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNavigationView = findViewById(R.id.btNavigationView);

        if (isNetwork()) {
            addFragment(new StoryFragment(), R.id.btMenuStory);
        } else {
            addFragment(new StoryDownloadFragment(), R.id.btMenuDownload);
        }
        processEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetwork()){
            changeFragment(R.id.btMenuDownload, new StoryDownloadFragment());
        }
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    private void addFragment(Fragment fragment, int id) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragment, null);
        fragmentTransaction.commit();

        btNavigationView.getMenu().findItem(id).setChecked(true);
        fragmentOld.put(count, id);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.fragment, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }

    private void changeFragment(int id, Fragment fragment) {
        if (!btNavigationView.getMenu().findItem(id).isChecked()) {
            if (count == 0) {
                count = 1;
            }
            count++;
            btNavigationView.getMenu().findItem(id).setChecked(true);
            fragmentOld.put(count, id);
            replaceFragment(fragment);
        }
    }

    public void closeMainActivity() {
        finish();
    }

    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.btMenuStory) {
                changeFragment(id, new StoryFragment());
            } else if (id == R.id.btMenuFilter) {
                changeFragment(id, new StoryFilterFragment());
            } else if (id == R.id.btMenuFollow) {
                changeFragment(id, new StoryFollowFragment());
            } else if (id == R.id.btMenuDownload) {
                changeFragment(id, new StoryDownloadFragment());
            } else if (id == R.id.btMenuAccount) {
                changeFragment(id, new AccountFragment());
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        count--;
        if (count < 0) {
            finish();
        }
        if (count == 0) {
            Toast.makeText(MainActivity.this, "Nhấn lần nữa để thoát ứng dụng!", Toast.LENGTH_SHORT).show();
        }
        if (count > 0) {
            int id = fragmentOld.get(count);
            backFragment(id);
            super.onBackPressed();
        }
    }

    private void backFragment(int id) {
        btNavigationView.getMenu().findItem(id).setChecked(true);
        if (count != 1) {
            fragmentOld.remove(count);
        }
    }
}