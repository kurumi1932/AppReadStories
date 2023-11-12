package huce.fit.appreadstories.story.filter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.story.information.StoryInformationActivity;
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Truyen;

public class StoryFilterFragment extends Fragment implements StoryFilterView {

    private StoryFilterPresenter mStoryFilterPresenter;
    private LinearLayout llFragmentStoryFilter;
    private StoryAdapter mStoryAdapter;
    private ProgressBar pbReload;
    private TextView tvSpecies0, tvSpecies1, tvSpecies2, tvSpecies3, tvSpecies4, tvSpecies5;
    private TextView tvStatus0, tvStatus1, tvStatus2;
    private Button btCheckNetwork;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_filter, container, false);
        init(view);
        processEvents(view);
        return view;
    }

    private void init(View view) {
        llFragmentStoryFilter = view.findViewById(R.id.llFragmentStoryFilter);
        tvSpecies0 = view.findViewById(R.id.tvSpecies0);
        tvSpecies1 = view.findViewById(R.id.tvSpecies1);
        tvSpecies2 = view.findViewById(R.id.tvSpecies2);
        tvSpecies3 = view.findViewById(R.id.tvSpecies3);
        tvSpecies4 = view.findViewById(R.id.tvSpecies4);
        tvSpecies5 = view.findViewById(R.id.tvSpecies5);
        tvStatus0 = view.findViewById(R.id.tvStatus0);
        tvStatus1 = view.findViewById(R.id.tvStatus1);
        tvStatus2 = view.findViewById(R.id.tvStatus2);
        pbReload = view.findViewById(R.id.pbReLoad);
        RecyclerView rcViewStory = view.findViewById(R.id.rcViewStory);
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

        mStoryFilterPresenter = new StoryFilterImpl(this, getActivity());
        mStoryAdapter = new StoryAdapter((position, view1, isLongClick) -> {
            intent = new Intent(getActivity(), StoryInformationActivity.class);
            intent.putExtra("storyId", position);
            startActivity(intent);
        });//Đổ dữ liệu lên adpter
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStory.setAdapter(mStoryAdapter);

        tvSpecies0.setBackgroundResource(R.drawable.border_filter_click);
        tvStatus0.setBackgroundResource(R.drawable.border_filter_click);
        mStoryFilterPresenter.setSpeciesId(R.id.tvSpecies0);
        mStoryFilterPresenter.setSpecies(tvSpecies0.getText().toString().trim());
        mStoryFilterPresenter.setStatusId(R.id.tvStatus0);
        mStoryFilterPresenter.setStatus(tvStatus0.getText().toString().trim());
        getData();
    }

    private void hide() {
        llFragmentStoryFilter.setVisibility(View.GONE);
        pbReload.setVisibility(View.GONE);
        btCheckNetwork.setVisibility(View.VISIBLE);
    }

    private void getData() {
        if (mStoryFilterPresenter.isNetwork()) {
            pbReload.setVisibility(View.VISIBLE);
            mStoryFilterPresenter.getData();
        } else {
            hide();
        }
    }

    @Override
    public void setData(List<Truyen> listStory) {
        mStoryAdapter.setDataStory(listStory);
        pbReload.setVisibility(View.GONE);
    }

    private void selectSpeciesFilter(View view, int speciesId) {
        view.findViewById(mStoryFilterPresenter.getSpeciesId()).setBackgroundColor(Color.WHITE);
        TextView tvSpecies = view.findViewById(speciesId);
        tvSpecies.setBackgroundResource(R.drawable.border_filter_click);
        mStoryFilterPresenter.setSpeciesId(speciesId);
        mStoryFilterPresenter.setSpecies(tvSpecies.getText().toString());
        getData();
    }

    private void selectStatusFilter(View view, int statusId) {
        view.findViewById(mStoryFilterPresenter.getStatusId()).setBackgroundColor(Color.WHITE);
        TextView tvStatus = view.findViewById(statusId);
        tvStatus.setBackgroundResource(R.drawable.border_filter_click);
        mStoryFilterPresenter.setStatusId(statusId);
        mStoryFilterPresenter.setStatus(tvStatus.getText().toString());
        getData();
    }

    private void processEvents(View view) {
        btCheckNetwork.setOnClickListener(v -> {
            if (mStoryFilterPresenter.isNetwork()) {
                getData();
                llFragmentStoryFilter.setVisibility(View.VISIBLE);
                btCheckNetwork.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        tvSpecies0.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies0);
        });
        tvSpecies1.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies1);
        });
        tvSpecies2.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies2);
        });
        tvSpecies3.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies3);
        });
        tvSpecies4.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies4);
        });
        tvSpecies5.setOnClickListener(v -> {
            selectSpeciesFilter(view, R.id.tvSpecies5);
        });

        tvStatus0.setOnClickListener(v -> {
            selectStatusFilter(view, R.id.tvStatus0);
        });
        tvStatus1.setOnClickListener(v -> {
            selectStatusFilter(view, R.id.tvStatus1);
        });
        tvStatus2.setOnClickListener(v -> {
            selectStatusFilter(view, R.id.tvStatus2);
        });
    }
}