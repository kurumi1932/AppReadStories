package huce.fit.appreadstories.controller.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import huce.fit.appreadstories.controller.fragment.StoryInterfaceRateFagment;
import huce.fit.appreadstories.controller.fragment.StoryInterfaceContentFagment;

public class ViewPagerStoryInterfaceAdapter extends FragmentStateAdapter {
    private int idStory;
    public ViewPagerStoryInterfaceAdapter(@NonNull FragmentActivity fragmentActivity, int idStory) {
        super(fragmentActivity);
        this.idStory = idStory;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StoryInterfaceContentFagment(idStory);
            case 1:
                return new StoryInterfaceRateFagment(idStory);

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
