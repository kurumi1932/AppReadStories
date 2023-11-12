package huce.fit.appreadstories.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import huce.fit.appreadstories.fragment.StoryInterfaceRateFagment;
import huce.fit.appreadstories.fragment.StoryInterfaceIntroduceFagment;

public class ViewPagerStoryInterfaceAdapter extends FragmentStateAdapter {
    private final int idStory;

    public ViewPagerStoryInterfaceAdapter(@NonNull FragmentActivity fragmentActivity, int idStory) {
        super(fragmentActivity);
        this.idStory = idStory;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StoryInterfaceIntroduceFagment(idStory);
        }
        return new StoryInterfaceRateFagment(idStory);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
