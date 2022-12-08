package huce.fit.appreadstories.fragment;

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

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.adapters.StoryDownloadAdapter;
import huce.fit.appreadstories.service.DeleteStoryService;
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

        processEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        rcView();
    }

    private void processEvents() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
            getData();
            rcView();
            pbReload.setVisibility(View.GONE);
        });
    }

    private void rcView() {
        pbReload.setVisibility(View.VISIBLE);
        storyDownloadAdapter = new StoryDownloadAdapter(listStoryDownload, (position, view1, isLongClick) -> {
            if (AppDatabase.getInstance(getActivity()).appDao().getStory(position) != null) {
                if (isLongClick) {
                    openDialogStoryDownload(position);
                } else {
                    Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
                    intent.putExtra("idStory", position);
                    startActivity(intent);
                }
            }
        });
        rcViewStoryDownload.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStoryDownload.setAdapter(storyDownloadAdapter);

        pbReload.setVisibility(View.GONE);
    }

    private void getData() {
        listStoryDownload = AppDatabase.getInstance(getActivity()).appDao().getAllStory();
        if (storyDownloadAdapter != null) {
            storyDownloadAdapter.notifyDataSetChanged();
        }
    }

    private void openDialogStoryDownload(int idStoryDownload) {
        final Dialog dialogStoryDownload = new Dialog(getActivity());
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

        Story story = AppDatabase.getInstance(getActivity()).appDao().getStory(idStoryDownload);
        tvStoryDownload.setText(story.getNameStory());

        tvReadStory.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", idStoryDownload);
            startActivity(intent);
            dialogStoryDownload.dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DeleteStoryService.class);
            intent.putExtra("idStory", idStoryDownload);

            requireActivity().startService(intent);
            dialogStoryDownload.dismiss();
        });

        dialogStoryDownload.show();
    }
}
