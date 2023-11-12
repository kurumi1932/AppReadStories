package huce.fit.appreadstories.story.follow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.story.information.StoryInformationActivity;
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Truyen;


public class StoryFollowFragment extends Fragment implements StoryFollowView{

    private StoryFollowPresenter mStoryFollowPresenter;
    private StoryAdapter mStoryAdapter;
    private LinearLayout llFragmentStoryFollow;
    private ProgressBar pbReload;
    private Button btCheckNetwork;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_follow, container, false);
        init(view);
        processEvent();
        return view;
    }

    private void init(View view){
        llFragmentStoryFollow = view.findViewById(R.id.llFragmentStoryFollow);
        pbReload = view.findViewById(R.id.pbReLoad);
        RecyclerView rcViewStory = view.findViewById(R.id.rcViewStory);
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

        mStoryFollowPresenter = new StoryFollowImpl(this, getActivity());
        mStoryAdapter = new StoryAdapter((position, view1, isLongClick) -> {
            Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
            intent.putExtra("storyId", position);
            startActivity(intent);
        });
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStory.setAdapter(mStoryAdapter);

        getData();
    }

    private void processEvent(){
        btCheckNetwork.setOnClickListener(v -> {
            if (mStoryFollowPresenter.isNetwork()) {
                getData();
                llFragmentStoryFollow.setVisibility(View.VISIBLE);
                btCheckNetwork.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void hide() {
        llFragmentStoryFollow.setVisibility(View.GONE);
        pbReload.setVisibility(View.GONE);
        btCheckNetwork.setVisibility(View.VISIBLE);
    }

    private void getData() {
        if (mStoryFollowPresenter.isNetwork()) {
            pbReload.setVisibility(View.VISIBLE);
            mStoryFollowPresenter.getData();
        } else {
            hide();
        }
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Truyen> listStory) {
        mStoryAdapter.setDataStory(listStory);
        pbReload.setVisibility(View.GONE);
    }
}