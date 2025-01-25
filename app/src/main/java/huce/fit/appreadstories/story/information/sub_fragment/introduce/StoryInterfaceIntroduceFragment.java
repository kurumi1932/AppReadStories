package huce.fit.appreadstories.story.information.sub_fragment.introduce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import huce.fit.appreadstories.R;

public class StoryInterfaceIntroduceFragment extends Fragment implements StoryInterfaceIntroduceFragmentView {

    private TextView tvIntroduce;

    public StoryInterfaceIntroduceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_interface_introduce, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        tvIntroduce = view.findViewById(R.id.tvIntroduce);

        StoryInterfaceIntroduceFragmentPresenter mStoryInterfaceIntroduceFragmentPresenter
                = new StoryInterfaceIntroduceFragmentImpl(this, getActivity());
        mStoryInterfaceIntroduceFragmentPresenter.getIntroduce();
    }

    @Override
    public void setIntroduce(String introduce) {
        tvIntroduce.setText(introduce);
    }

}
