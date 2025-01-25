package huce.fit.appreadstories.story.list.online;

import static huce.fit.appreadstories.checknetwork.NetworkKt.isConnecting;

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
import huce.fit.appreadstories.adapters.StoryAdapter;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.pagination_croll.PaginationScrollListener;
import huce.fit.appreadstories.story.information.StoryInformationActivity;
import huce.fit.appreadstories.story.list.search.StorySearchActivity;


public class StoryFragment extends Fragment implements StoryView{

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private ImageView ivSearch;
    private Button btCheckNetwork;
    private StoryPresenter mStoryPresenter;
    private StoryAdapter mStoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        init(view);
        processEvent();
        return view;
    }

    private void init(View view){
        linearLayout = view.findViewById(R.id.llFragmentStory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.pbReLoad);
        ivSearch = view.findViewById(R.id.ivSearch);
        recyclerView = view.findViewById(R.id.rcViewStory);
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork);

        mStoryPresenter = new StoryImpl(this, getActivity());
        mStoryAdapter = new StoryAdapter();
        //các Item có cùng chiều cao và độ rộng có thể tối ưu hiệu năng để khi cuộn danh sách được mượt mà hơn
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mStoryAdapter);

        getData(1);
    }

    private void processEvent() {
        mStoryAdapter.setClickListener((position, view1, isLongClick) -> {
            mStoryPresenter.setStoryId(position);
            Intent intent = new Intent(getActivity(), StoryInformationActivity.class);
            startActivity(intent);
        });

        btCheckNetwork.setOnClickListener(v -> {
            if (isConnecting(requireContext())) {
                getData(1);
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                progressBar.setVisibility(View.VISIBLE);
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
        if (isConnecting(requireContext())) {
            mStoryPresenter.getStory(page);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            btCheckNetwork.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            btCheckNetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setData(List<Story> storyList){
        mStoryAdapter.setDataStory(storyList);
        progressBar.setVisibility(View.GONE);
    }
}