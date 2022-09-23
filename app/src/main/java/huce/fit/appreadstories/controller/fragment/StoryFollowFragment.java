package huce.fit.appreadstories.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.model.TruyenTheoDoi;
import huce.fit.appreadstories.controller.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.controller.adapters.StoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryFollowFragment extends Fragment {

    private StoryAdapter storyFollowAdapter;
    private RecyclerView rcViewStory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pbReload;

    private int idAccount;
    private List<Truyen> listStoryFollow = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_follow, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        pbReload = view.findViewById(R.id.pbReLoad);
        rcViewStory = view.findViewById(R.id.rcViewStory);

        getSharedPreferences();
        getData();
        rcView(listStoryFollow);
        processEvents();

        return view;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private void processEvents() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setRefreshing(false);
            getData();
        });
    }

    private void rcView(List<Truyen> listStoryFollow) {
        storyFollowAdapter = new StoryAdapter(listStoryFollow, (position, view1) -> {
            Truyen ttd = listStoryFollow.get(position);
            int idStoryFollow = ttd.getMatruyen();
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", idStoryFollow);
            startActivity(intent);
        });
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStory.setAdapter(storyFollowAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewStory.addItemDecoration(itemDecoration);
    }

    private void getData() {
        pbReload.setVisibility(View.VISIBLE);
        Api.apiInterface().getListStoriesFollow(idAccount).enqueue(new Callback<List<TruyenTheoDoi>>() {
            @Override
            public void onResponse(Call<List<TruyenTheoDoi>> call, Response<List<TruyenTheoDoi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getMatruyen() != 0) {
                        listStoryFollow.clear();
                        listStoryFollow.addAll(response.body());
                        storyFollowAdapter.notifyDataSetChanged();
                        pbReload.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "Không có truyện theo dõi!", Toast.LENGTH_SHORT).show();
                        pbReload.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TruyenTheoDoi>> call, Throwable t) {
                Log.e("story_follow: ", t.toString());
                Toast.makeText(getActivity(), "Lỗi truyện theo dõi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}