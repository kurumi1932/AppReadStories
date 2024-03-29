package huce.fit.appreadstories.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryFilterFragment extends Fragment {

    private final List<Truyen> listStory = new ArrayList<>(); //data source
    private LinearLayout llFragmentStoryFilter;
    private StoryAdapter storyAdapter;
    private RecyclerView rcViewStory;
    private ProgressBar pbReload;
    private TextView tvSpecies, tvSpecies1, tvSpecies2, tvSpecies3, tvSpecies4, tvSpecies5;
    private TextView tvStatus, tvStatus1, tvStatus2;
    private Button btCheckNetwork;
    private String clickSpecies, clickStatus;
    private int age;

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

        getSharedPreferences();

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

        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

        tvSpecies.setBackgroundResource(R.drawable.border_filter_click);
        tvStatus.setBackgroundResource(R.drawable.border_filter_click);

        processEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isNetwork()) {
            show();
        } else {
            hide();
        }
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences =  requireActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        age = sharedPreferences.getInt("age", 0);
        Log.e("age", String.valueOf(age));
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(getActivity());
        return checkNetwork.isNetwork();
    }

    private void show() {
        getData(clickSpecies, clickStatus);
        rcView(listStory);
        llFragmentStoryFilter.setVisibility(View.VISIBLE);
        btCheckNetwork.setVisibility(View.GONE);
    }

    private void hide() {
        llFragmentStoryFilter.setVisibility(View.GONE);
        pbReload.setVisibility(View.GONE);
        btCheckNetwork.setVisibility(View.VISIBLE);

        btCheckNetwork.setOnClickListener(v -> {
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
            Api.apiInterface().getListStoriesFilter(species, status, age).enqueue(new Callback<List<Truyen>>() {
                @Override
                public void onResponse(@NonNull Call<List<Truyen>> call, Response<List<Truyen>> response) {
                    List<Truyen> list = response.body();
                    if (response.isSuccessful() && list != null) {
                        listStory.clear();
                        listStory.addAll(list);
                        storyAdapter.notifyDataSetChanged();
                        pbReload.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Truyen>> call, Throwable t) {
                    Log.e("Err_StoryFilter", "getData", t);
                }
            });
        } else {
            hide();
        }
    }

    private void rcView(List<Truyen> listStory) {
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));

        storyAdapter = new StoryAdapter(listStory, (position, view1, isLongClick) -> {
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", position);
            startActivity(intent);
        });//Đổ dữ liệu lên adpter
        rcViewStory.setAdapter(storyAdapter);
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
            tvSpecies.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Tất cả";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies1.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies1.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Đô thị";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies2.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies2.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Tu tiên";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies3.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies3.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Huyền huyễn";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies4.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies4.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Trùng sinh";
            getData(clickSpecies, clickStatus);
        });
        tvSpecies5.setOnClickListener(v -> {
            colorBackGroundSpecies();
            tvSpecies5.setBackgroundResource(R.drawable.border_filter_click);
            clickSpecies = "Ngôn tình";
            getData(clickSpecies, clickStatus);
        });

        tvStatus.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus.setBackgroundResource(R.drawable.border_filter_click);
            clickStatus = "Tất cả";
            getData(clickSpecies, clickStatus);
        });
        tvStatus1.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus1.setBackgroundResource(R.drawable.border_filter_click);
            clickStatus = "Đang ra";
            getData(clickSpecies, clickStatus);
        });
        tvStatus2.setOnClickListener(v -> {
            colorBackGroundStatus();
            tvStatus2.setBackgroundResource(R.drawable.border_filter_click);
            clickStatus = "Hoàn thành";
            getData(clickSpecies, clickStatus);
        });
    }
}