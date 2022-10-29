package huce.fit.appreadstories.controller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceIntroduceFagment extends Fragment {
    private TextView tvIntroduce;
    private int idStory;

    public StoryInterfaceIntroduceFagment(int idStory) {
        this.idStory = idStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_introduce, container, false);

        tvIntroduce = view.findViewById(R.id.tvIntroduce);
        getData();

        return view;
    }

    private void getData() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen tc = response.body();
                tvIntroduce.setText(tc.getGioithieu());
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterfaceF", t.toString());
            }
        });
    }
}
