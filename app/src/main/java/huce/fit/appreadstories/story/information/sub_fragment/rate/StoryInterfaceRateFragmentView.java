package huce.fit.appreadstories.story.information.sub_fragment.rate;

import java.util.List;

import huce.fit.appreadstories.model.Rate;

public interface StoryInterfaceRateFragmentView {
    void setData(List<Rate> rateList);
    void noNetwork();
}
