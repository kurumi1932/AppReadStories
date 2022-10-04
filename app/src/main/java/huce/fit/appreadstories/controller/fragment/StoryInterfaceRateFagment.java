package huce.fit.appreadstories.controller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.RateAdapter;
import huce.fit.appreadstories.model.DanhGia;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceRateFagment extends Fragment {
    private List<DanhGia> listRate = new ArrayList<>(); //data source
    private RateAdapter rateAdapter;
    private RecyclerView rcViewRate;

    private int idStory;

    public StoryInterfaceRateFagment(int idStory) {
        this.idStory = idStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_rate, container, false);

        rcViewRate = view.findViewById(R.id.rcViewRate);

        getDataRate();
        rcView();

        return view;
    }

    private void rcView() {
        rateAdapter = new RateAdapter(listRate, (position, view) -> {
        });//Đổ dữ liệu lên adpter
        rcViewRate.setHasFixedSize(true);
        rcViewRate.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewRate.setAdapter(rateAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewRate.addItemDecoration(itemDecoration);
    }

    public void getDataRate() {
        Api.apiInterface().getListRate(idStory).enqueue(new Callback<List<DanhGia>>() {
            @Override
            public void onResponse(Call<List<DanhGia>> call, Response<List<DanhGia>> response) {
                if (response.isSuccessful()) {
                    listRate.clear();
                    listRate.addAll(response.body());
                    rateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DanhGia>> call, Throwable t) {
                Log.e("Err_StoryInterfaceF", t.toString());
            }
        });
    }
}