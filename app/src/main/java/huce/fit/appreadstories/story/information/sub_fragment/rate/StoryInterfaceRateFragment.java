package huce.fit.appreadstories.story.information.sub_fragment.rate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.RateAdapter;
import huce.fit.appreadstories.model.Rate;

public class StoryInterfaceRateFragment extends Fragment implements StoryInterfaceRateFragmentView {

    private RateAdapter mRateAdapter;

    public StoryInterfaceRateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_rate, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rcViewRate);

        mRateAdapter = new RateAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRateAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        StoryInterfaceRateFragmentPresenter storyInterfaceRateFragmentPresenter
                = new StoryInterfaceRateFragmentImpl(this, getActivity());
        storyInterfaceRateFragmentPresenter.getRateList();
    }

    @Override
    public void setData(List<Rate> rateList) {
        mRateAdapter.setDataRate(rateList);
    }

    @Override
    public void noNetwork(){
        Toast.makeText(getActivity(), "\tChưa kết nối mạng!\nKhông xem được đánh giá!", Toast.LENGTH_SHORT).show();
    }
}