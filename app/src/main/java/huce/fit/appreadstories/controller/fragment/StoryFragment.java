package huce.fit.appreadstories.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.controller.activity.StoryInterfaceActivity;
import huce.fit.appreadstories.controller.activity.StorySearchActivity;
import huce.fit.appreadstories.controller.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.pagination_croll.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryFragment extends Fragment {

    private StoryAdapter storyAdapter;
    private LinearLayout llFragmentStory;
    private RecyclerView rcViewStory;
    private ImageView ivSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pbReload;
    private Button btCheckConnection;
    private List<Truyen> listStory = new ArrayList<>(); //data source
    private boolean isLoading;
    private boolean isLastPage;
    private int curentPage = 1;// page hiện tại
    private int totalPage; // tổng số trang
    private static final int ITEMS_PAGE = 8;// số item có trong trang

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        llFragmentStory = view.findViewById(R.id.llFragmentStory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        pbReload = view.findViewById(R.id.pbReLoad);
        ivSearch = view.findViewById(R.id.ivSearch);
        rcViewStory = view.findViewById(R.id.rcViewStory);
        btCheckConnection = view.findViewById(R.id.btCheckConnection);

        if(isNetwork()){
            show();
        }else {
            hide();
        }

        return view;
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(getActivity());
        return checkNetwork.isNetwork();
    }

    private void show() {
        totalPage();
        getData(1);
        rcView();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        llFragmentStory.setVisibility(View.VISIBLE);
        btCheckConnection.setVisibility(View.GONE);

        processEvents();
    }

    private void hide() {
        swipeRefreshLayout.setVisibility(View.GONE);
        llFragmentStory.setVisibility(View.GONE);
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

    private void totalPage() {
        Api.apiInterface().getListStories(0).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(Call<List<Truyen>> call, Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("number_item:", String.valueOf(response.body().size()));
                    totalPage = (response.body().size() / ITEMS_PAGE);
                    Log.e("number_page:", String.valueOf(totalPage));
                    if ((totalPage * ITEMS_PAGE) < response.body().size()) {
                        totalPage += 1;
                        Log.e("number_page:", String.valueOf(totalPage));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Truyen>> call, Throwable t) {
                Log.e("Err_StoryFagment", "totalPage",t);
            }
        });
    }

    private void getData(int a) {
        pbReload.setVisibility(View.VISIBLE);
        Api.apiInterface().getListStories(0).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(Call<List<Truyen>> call, Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (a * ITEMS_PAGE > response.body().size()) {
                        for (int i = (a - 1) * ITEMS_PAGE; i < response.body().size(); i++) {
                            listStory.add(response.body().get(i));
                        }
                    } else {
                        for (int i = (a - 1) * ITEMS_PAGE; i < (a * ITEMS_PAGE); i++) {
                            listStory.add(response.body().get(i));
                        }
                    }
                    pbReload.setVisibility(View.GONE);
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Truyen>> call, Throwable t) {
                Log.e("Err_StoryFagment", "getData",t);
            }
        });
    }

    private void rcView() {
        //1.Đổ dữ liệu lên adpter
        storyAdapter = new StoryAdapter(listStory, (position, view1) -> {
            Intent intent = new Intent(getActivity(), StoryInterfaceActivity.class);
            intent.putExtra("idStory", position);
            startActivity(intent);
        });
        rcViewStory.setHasFixedSize(true);//các Item có cùng chiều cao và độ rộng có thể tối ưu hiệu năng để khi cuộn danh sách được mượt mà hơn
        //2.set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcViewStory.setLayoutManager(linearLayoutManager);
        // 3.set adapter
        rcViewStory.setAdapter(storyAdapter);

        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcViewStory.addItemDecoration(itemDecoration);

        rcViewStory.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                pbReload.setVisibility(View.VISIBLE);
                curentPage += 1;
                loadNextPage(curentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void loadNextPage(int a) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            getData(a);
            isLoading = false;

            if (curentPage == totalPage) {
                isLastPage = true;
            }
        }, 1500);
    }

    private void processEvents() {
        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StorySearchActivity.class);
            startActivity(intent);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (isNetwork()) {
                listStory.clear();
                isLastPage = false;
                curentPage = 1;
                show();
            }else {
                hide();
            }
        });
    }
}