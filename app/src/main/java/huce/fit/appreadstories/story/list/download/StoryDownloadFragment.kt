package huce.fit.appreadstories.story.list.download;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.StoryDownloadAdapter;
import huce.fit.appreadstories.dialog.story_download.StoryDownloadDialog;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.story.information.StoryInformationActivity;

public class StoryDownloadFragment extends Fragment implements StoryDownloadView {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StoryDownloadPresenter mStoryDownloadPresenter;
    private StoryDownloadAdapter mStoryDownloadAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_download, container, false);
        init(view);
        processEvents();
        return view;
    }

    private void init(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.pbReLoad);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewStoryDownload);

        mStoryDownloadPresenter = new StoryDownloadImpl(this, getActivity());
        mStoryDownloadAdapter = new StoryDownloadAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mStoryDownloadAdapter);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void processEvents() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
            getData();
        });

        mStoryDownloadAdapter.setClickListener((position, view1, isLongClick) -> {
            mStoryDownloadPresenter.setStoryId(position);
            if (isLongClick) {
                StoryDownloadDialog storyDownloadDialog = new StoryDownloadDialog(this, getActivity());
                storyDownloadDialog.show();
            } else {
                Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        mStoryDownloadPresenter.getData();
    }

    @Override
    public void setData(List<Story> storyList) {
        mStoryDownloadAdapter.setDataStory(storyList);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void openStoryDownload() {
        Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteStoryDownload() {
        Intent intent = new Intent(getActivity(), DeleteStoryService.class);
        intent.putExtra("storyId", mStoryDownloadPresenter.getStoryId());
        requireActivity().startService(intent);
    }
}
