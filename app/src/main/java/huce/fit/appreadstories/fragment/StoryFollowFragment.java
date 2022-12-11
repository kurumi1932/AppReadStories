package huce.fit.appreadstories.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import huce.fit.appreadstories.model.TruyenTheoDoi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryFollowFragment extends Fragment {

    private StoryAdapter storyFollowAdapter;
    private LinearLayout llFragmentStoryFollow;
    private RecyclerView rcViewStory;
    private ProgressBar pbReload;
    private Button btCheckNetwork;

    private int idAccount;
    private final List<Truyen> listStoryFollow = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_follow, container, false);

        getSharedPreferences();

        llFragmentStoryFollow = view.findViewById(R.id.llFragmentStoryFollow);
        pbReload = view.findViewById(R.id.pbReLoad);
        rcViewStory = view.findViewById(R.id.rcViewStory);
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

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
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(getActivity());
        return checkNetwork.isNetwork();
    }

    private void show() {
        getData();
        rcView();
        llFragmentStoryFollow.setVisibility(View.VISIBLE);
        btCheckNetwork.setVisibility(View.GONE);
    }

    private void hide() {
        llFragmentStoryFollow.setVisibility(View.GONE);
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

    private void getData() {
        Api.apiInterface().getListStoriesFollow(idAccount).enqueue(new Callback<List<TruyenTheoDoi>>() {
            @Override
            public void onResponse(@NonNull Call<List<TruyenTheoDoi>> call, @NonNull Response<List<TruyenTheoDoi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getMatruyen() != 0) {
                        listStoryFollow.clear();
                        listStoryFollow.addAll(response.body());

                        storyFollowAdapter.notifyDataSetChanged();
                    }
                    pbReload.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TruyenTheoDoi>> call, @NonNull Throwable t) {
                Log.e("Err_StoryFollow", "getData", t);
            }
        });
    }

    private void rcView() {
        storyFollowAdapter = new StoryAdapter(listStoryFollow, (position, view1, isLongClick) -> {
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", position);
            startActivity(intent);
        });
        rcViewStory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewStory.setAdapter(storyFollowAdapter);
    }
}