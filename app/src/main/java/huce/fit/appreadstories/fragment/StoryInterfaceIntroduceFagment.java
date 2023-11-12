package huce.fit.appreadstories.fragment;

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
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceIntroduceFagment extends Fragment {
    private TextView tvIntroduce;
    private final int idStory;

    private Story story;
    private CheckNetwork checkNetwork;

    public StoryInterfaceIntroduceFagment(int idStory) {
        this.idStory = idStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_introduce, container, false);

        tvIntroduce = view.findViewById(R.id.tvIntroduce);
        checkNetwork = new CheckNetwork(getActivity());
        story = AppDatabase.getInstance(getActivity()).appDao().getStory(idStory);
        if (story != null) {
            tvIntroduce.setText(story.getIntroduce());
        } else {
            getData();
        }

        return view;
    }

    private void getData() {
        if (checkNetwork.isNetwork()) {
            Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
                @Override
                public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                    if (response.body() != null) {
                        tvIntroduce.setText(response.body().getGioithieu());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                    Log.e("Err_StoryInterfaceF", t.toString());
                }
            });
        }
    }
}
