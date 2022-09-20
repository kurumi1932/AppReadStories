package huce.fit.appreadstories.controller.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import huce.fit.appreadstories.controller.fragment.AccountFragment;
import huce.fit.appreadstories.controller.fragment.StoryFilterFragment;
import huce.fit.appreadstories.controller.fragment.StoryFollowFragment;
import huce.fit.appreadstories.controller.fragment.StoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
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
                return new AccountFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
