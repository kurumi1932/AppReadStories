package huce.fit.appreadstories.story.download;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.story.information.StoryInformationActivity;
import huce.fit.appreadstories.adapters.StoryDownloadAdapter;
import huce.fit.appreadstories.service.DeleteStoryService;
import huce.fit.appreadstories.sqlite.Story;

public class StoryDownloadFragment extends Fragment implements StoryDownloadView{

    private StoryDownloadPresenter mStoryDownloadPresenter;
    private StoryDownloadAdapter mStoryDownloadAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pbReload;

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
        pbReload = view.findViewById(R.id.pbReLoad);
        RecyclerView recyclerView = view.findViewById(R.id.rcViewStoryDownload);

        mStoryDownloadPresenter = new StoryDownloadImpl(this, getActivity());
        mStoryDownloadAdapter = new StoryDownloadAdapter((position, view1, isLongClick) -> {
            if (isLongClick) {
                openDialogStoryDownload(position);
            } else {
                Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
                intent.putExtra("storyId", position);
                startActivity(intent);
            }
        });
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
            pbReload.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
            getData();
        });
    }

    private void getData(){
        pbReload.setVisibility(View.VISIBLE);
        mStoryDownloadPresenter.getData();
    }

    @Override
    public void setData(List<Story> listStory){
        mStoryDownloadAdapter.setDataStory(listStory);
        pbReload.setVisibility(View.GONE);
    }

    private void openDialogStoryDownload(int storyId) {
        final Dialog dialogStoryDownload = new Dialog(requireActivity());
        dialogStoryDownload.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogStoryDownload.setContentView(R.layout.dialog_story_download);

        Window window = dialogStoryDownload.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogStoryDownload.setCancelable(true);

        TextView tvStoryDownload = dialogStoryDownload.findViewById(R.id.tvStoryDownload);
        TextView tvReadStory = dialogStoryDownload.findViewById(R.id.tvReadStory);
        TextView tvDelete = dialogStoryDownload.findViewById(R.id.tvDelete);

        tvStoryDownload.setText(mStoryDownloadPresenter.getStoryName(storyId));

        tvReadStory.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
            intent.putExtra("storyId", storyId);
            startActivity(intent);
            dialogStoryDownload.dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DeleteStoryService.class);
            intent.putExtra("storyId", storyId);
            requireActivity().startService(intent);
            dialogStoryDownload.dismiss();
        });

        dialogStoryDownload.show();
    }
}
