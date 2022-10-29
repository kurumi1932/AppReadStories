package huce.fit.appreadstories.controller.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.controller.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.controller.adapters.StoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryFilterFragment extends Fragment {

    private List<Truyen> listStory = new ArrayList<>(); //data source
    private LinearLayout llFragmentStoryFilter;
    private StoryAdapter storyAdapter;
    private RecyclerView rcViewStory;
    private ProgressBar pbReload;
    private TextView tvSpecies, tvSpecies1, tvSpecies2, tvSpecies3, tvSpecies4, tvSpecies5;
    private TextView tvStatus, tvStatus1, tvStatus2;
    private Button btCheckConnection;
    private String clickSpecies, clickStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickSpecies = "Tất cả";
        clickStatus = "Tất cả";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_filter, container, false);

        llFragmentStoryFilter = view.findViewById(R.id.llFragmentStoryFilter);

        tvSpecies = view.findViewById(R.id.tvSpecies);
        tvSpecies1 = view.findViewById(R.id.tvSpecies1);
        tvSpecies2 = view.findViewById(R.id.tvSpecies2);
        tvSpecies3 = view.findViewById(R.id.tvSpecies3);
        tvSpecies4 = view.findViewById(R.id.tvSpecies4);
        tvSpecies5 = view.findViewById(R.id.tvSpecies5);

        tvStatus = view.findViewById(R.id.tvStatus);
        tvStatus1 = view.findViewById(R.id.tvStatus1);
        tvStatus2 = view.findViewById(R.id.tvStatus2);

        pbReload = view.findViewById(R.id.pbReLoad);
        rcViewStory = view.findViewById(R.id.rcViewStory);

        btCheckConnection = view.findViewById(R.id.btCheckConnection);

        tvSpecies.setBackgroundResource(R.drawable.boder_filter_click);
        tvStatus.setBackgroundResource(R.drawable.boder_filter_click);

        if (isNetwork()) {
            show();
        } else {
            hide();
        }
        processEvents();

        return view;
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(getActivity());
        return checkNetwork.isNetwork();
    }

    private void show() {
        getData(clickSpecies, clickStatus);
        rcView(listStory);
        llFragmentStoryFilter.setVisibility(View.VISIBLE);
        btCheckConnection.setVisibility(View.GONE);
    }

    private void hide() {
        llFragmentStoryFilter.setVisibility(View.GONE);
        pbReload.setVisibility(View.GONE);
        btCheckConnection.setVisibility(View.VISIBLE);

        btCheckConnection.setOnClickListener(v -> {
            if (isNetwork()) {
                show();
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(String species, String status) {
        if (isNetwork()) {
            pbReload.setVisibility(View.VISIBLE);
            Api.apiInterface().getListStoriesFilter(species, status).enqueue(new Callback<List<Truyen>>() {
                @Override
                public void onResponse(Call<List<Truyen>> call, Response<List<Truyen>> response) {
                    List<Truyen> list = response.body();
                    if (response.isSuccessful() && list != null) {
                        listStory.clear();
                        listStory.addAll(response.body());
                        storyAdapter.notifyDataSetChanged();
                        pbReload.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<Truyen>> call, Throwable t) {
                    Log.e("Err_StoryFilter", "getData", t);
                }
            });
        } else {
            hide();
        }
    }

    private void rcView(List<Truyen> listStory) {
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));

        storyAdapter = new StoryAdapter(listStory, (position, view1) -> {
            Truyen tc = listStory.get(position);
            int idStory = tc.getMatruyen();
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
        });//Đổ dữ liệu lên adpter
        rcViewStory.setAdapter(storyAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewStory.addItemDecoration(itemDecoration);
    }

    private void colorBackGroundSpecies() {
        tvSpecies.setBackgroundColor(Color.WHITE);
        tvSpecies1.setBackgroundColor(Color.WHITE);
        tvSpecies2.setBackgroundColor(Color.WHITE);
        tvSpecies3.setBackgroundColor(Color.WHITE);
        tvSpecies4.setBackgroundColor(Color.WHITE);
        tvSpecies5.setBackgroundColor(Color.WHITE);
    }

    private void colorBackGroundStatus() {
        tvStatus.setBackgroundColor(Color.WHITE);
        tvStatus1.setBackgroundColor(Color.WHITE);
        tvStatus2.setBackgroundColor(Color.WHITE);
    }

    private void processEvents() {
        tvSpecies.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Tất cả";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies1.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies1.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Đô thị";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies2.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies2.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Tu tiên";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies3.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies3.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Huyền huyễn";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies4.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies4.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Trùng sinh";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies5.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies5.setBackgroundResource(R.drawable.boder_filter_click);
            clickSpecies = "Ngôn tình";
            getData(clickSpecies, clickStatus);
        });

        tvStatus.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus.setBackgroundResource(R.drawable.boder_filter_click);
            clickStatus = "Tất cả";
            getData(clickSpecies, clickStatus);
        });
        tvStatus1.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus1.setBackgroundResource(R.drawable.boder_filter_click);
            clickStatus = "Đang ra";
            getData(clickSpecies, clickStatus);
        });
        tvStatus2.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus2.setBackgroundResource(R.drawable.boder_filter_click);
            clickStatus = "Hoàn thành";
            getData(clickSpecies, clickStatus);
        });
    }
}