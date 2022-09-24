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
import huce.fit.appreadstories.controller.adapters.CommentAdapter;
import huce.fit.appreadstories.model.BinhLuan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceFagmentComment extends Fragment {
    private List<BinhLuan> listComment = new ArrayList<>(); //data source
    private CommentAdapter commentAdapter;
    private RecyclerView rcViewComment;

    private int idStory;

    public StoryInterfaceFagmentComment(int idStory) {
        this.idStory = idStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_comment, container, false);

        rcViewComment = view.findViewById(R.id.rcViewComment);

        getData();
        rcView();

        return view;
    }

    private void rcView() {
        commentAdapter = new CommentAdapter(listComment, (position, view) -> {
        });//Đổ dữ liệu lên adpter
        rcViewComment.setHasFixedSize(true);
        rcViewComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcViewComment.setAdapter(commentAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewComment.addItemDecoration(itemDecoration);
    }

    public void getData() {
        Api.apiInterface().getListComment(idStory).enqueue(new Callback<List<BinhLuan>>() {
            @Override
            public void onResponse(Call<List<BinhLuan>> call, Response<List<BinhLuan>> response) {
                if (response.isSuccessful()) {
                    listComment.clear();
                    listComment.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BinhLuan>> call, Throwable t) {
                Log.e("Err_StoryInterfaceF", t.toString());
            }
        });
    }
}