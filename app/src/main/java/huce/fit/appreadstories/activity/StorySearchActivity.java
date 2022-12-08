package huce.fit.appreadstories.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StorySearchActivity extends AppCompatActivity {

    private final List<Truyen> listStory = new ArrayList<>();
    private StoryAdapter storyAdapter;
    private RecyclerView rcViewStory;
    private ImageView ivBack;
    private SearchView svStory;
    private int age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_search);

        getSharedPreferences();

        ivBack = findViewById(R.id.ivBack);
        svStory = findViewById(R.id.svStory);
        rcViewStory = findViewById(R.id.rcViewStory);

        processEvents();
        rcView(listStory);
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        age = sharedPreferences.getInt("age", 0);
        Log.e("age", String.valueOf(age));
    }

    private void rcView(List<Truyen> listStory) {
        rcViewStory.setLayoutManager(new LinearLayoutManager(this));
        storyAdapter = new StoryAdapter(listStory, (position, view1, isLongClick) -> {
            Intent intent = new Intent(StorySearchActivity.this, StoryInterfaceActivity.class);
            intent.putExtra("idStory", position);
            startActivity(intent);
        });//Đổ dữ liệu lên adpter
        rcViewStory.setAdapter(storyAdapter);
        //đường kẻ giữa các icon
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewStory.addItemDecoration(itemDecoration);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());

        svStory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    getData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getData(String name) {
        Api.apiInterface().searchStory(name, age).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Truyen>> call,@NonNull Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listStory.clear();
                    listStory.addAll(response.body());
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Truyen>> call,@NonNull Throwable t) {
                Toast.makeText(StorySearchActivity.this, "Lỗi cập nhật danh sách truyện!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
