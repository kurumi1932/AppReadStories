package huce.fit.appreadstories.story.story;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.story.information.StoryInformationActivity;
import huce.fit.appreadstories.story.search.StorySearchActivity;
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.pagination_croll.PaginationScrollListener;


public class StoryFragment extends Fragment implements StoryView{

    private LinearLayout llFragmentStory;
    private RecyclerView rcViewStory;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pbReload;
    private ImageView ivSearch;
    private Button btCheckNetwork;
    private StoryAdapter mStoryAdapter;
    private StoryPresenter mStoryPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        init(view);
        processEvent();
        return view;
    }

    private void init(View view){
        llFragmentStory = view.findViewById(R.id.llFragmentStory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        pbReload = view.findViewById(R.id.pbReLoad);
        ivSearch = view.findViewById(R.id.ivSearch);
        rcViewStory = view.findViewById(R.id.rcViewStory);
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

        mStoryPresenter = new StoryImpl(this, getActivity());
        mStoryAdapter = new StoryAdapter((position, view1, isLongClick) -> {
            Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
            intent.putExtra("storyId", position);
            startActivity(intent);
        });
        //các Item có cùng chiều cao và độ rộng có thể tối ưu hiệu năng để khi cuộn danh sách được mượt mà hơn
        rcViewStory.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcViewStory.setLayoutManager(linearLayoutManager);
        rcViewStory.setAdapter(mStoryAdapter);

        mStoryPresenter.totalPage();
        getData(1);
    }

    private void processEvent() {
        btCheckNetwork.setOnClickListener(v -> {
            if (mStoryPresenter.isNetwork()) {
                getData(1);
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        rcViewStory.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                pbReload.setVisibility(View.VISIBLE);
                mStoryPresenter.loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return mStoryPresenter.isLoading();
            }

            @Override
            public boolean isLastPage() {
                return mStoryPresenter.isLastPage();
            }
        });

        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StorySearchActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            mStoryPresenter.swipeData();
        });
    }

    @Override
    public void getData(int page) {
        if (mStoryPresenter.isNetwork()) {
            mStoryPresenter.getData(page);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            llFragmentStory.setVisibility(View.VISIBLE);
            btCheckNetwork.setVisibility(View.GONE);
        } else {
            hide();
        }
    }

    @Override
    public void hide() {
        swipeRefreshLayout.setVisibility(View.GONE);
        llFragmentStory.setVisibility(View.GONE);
        pbReload.setVisibility(View.GONE);
        btCheckNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(List<Truyen> listStory){
        mStoryAdapter.setDataStory(listStory);
        pbReload.setVisibility(View.GONE);
    }
}