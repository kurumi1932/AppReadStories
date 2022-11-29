package huce.fit.appreadstories.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import huce.fit.appreadstories.fragment.AccountFragment;
import huce.fit.appreadstories.fragment.StoryDownloadFragment;
import huce.fit.appreadstories.fragment.StoryFilterFragment;
import huce.fit.appreadstories.fragment.StoryFollowFragment;
import huce.fit.appreadstories.fragment.StoryFragment;

public class ViewPagerMainAdapter extends FragmentStateAdapter {
    public ViewPagerMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StoryFragment();
            case 1:
                return new StoryFilterFragment();
            case 2:
                return new StoryFollowFragment();
            case 3:
                return new StoryDownloadFragment();
            case 4:
                return new AccountFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
