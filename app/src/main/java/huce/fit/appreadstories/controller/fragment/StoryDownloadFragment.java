package huce.fit.appreadstories.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.controller.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.controller.adapters.StoryDownloadAdapter;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Story;

public class StoryDownloadFragment extends Fragment {
    private StoryDownloadAdapter storyDownloadAdapter;
    private RecyclerView rcViewStoryDownload;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pbReload;

    private List<Story> listStoryDownload = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_download, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        pbReload = view.findViewById(R.id.pbReLoad);
        rcViewStoryDownload = view.findViewById(R.id.rcViewStoryDownload);

        getData();
        rcView();
        processEvents();

        return view;
    }

    private void processEvents() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
            getData();
            pbReload.setVisibility(View.GONE);
        });
    }

    private void rcView() {
        pbReload.setVisibility(View.VISIBLE);
        storyDownloadAdapter = new StoryDownloadAdapter(listStoryDownload, (position, view1) -> {
            int idStoryDownload = position;
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", idStoryDownload);
            startActivity(intent);
        });
        rcViewStoryDownload.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStoryDownload.setAdapter(storyDownloadAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewStoryDownload.addItemDecoration(itemDecoration);

        pbReload.setVisibility(View.GONE);
    }

    private void getData(){
        listStoryDownload = AppDatabase.getInstance(getActivity()).appDao().getAllStory();
    }
}
