package huce.fit.appreadstories.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

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

    private List<Truyen> listStory = new ArrayList<>(); //data source
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
        SharedPreferences sharedPreferences =  getSharedPreferences("CheckLogin", MODE_PRIVATE);
        age = sharedPreferences.getInt("age", 0);
        Log.e("age", String.valueOf(age));
    }

    private void rcView(List<Truyen> listStory) {
        rcViewStory.setLayoutManager(new LinearLayoutManager(this));
        storyAdapter = new StoryAdapter(listStory, (position, view1) -> {
            Truyen tc = listStory.get(position);
            int idStory = tc.getMatruyen();

            Intent intent = new Intent(StorySearchActivity.this, StoryInterfaceActivity.class);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
        });//Đổ dữ liệu lên adpter
        rcViewStory.setAdapter(storyAdapter);
        //đường kẻ giữa các icon
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewStory.addItemDecoration(itemDecoration);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        svStory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getData(query);
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
            public void onResponse(Call<List<Truyen>> call, Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listStory.clear();
                    listStory.addAll(response.body());
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Truyen>> call, Throwable t) {
                Toast.makeText(StorySearchActivity.this, "Lỗi cập nhật danh sách truyện!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
